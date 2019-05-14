package com.aranaira.arcanearchives.data;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.inventory.handlers.ManifestItemHandler;
import com.aranaira.arcanearchives.network.NetworkHandler;
import com.aranaira.arcanearchives.network.PacketNetworks;
import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import com.aranaira.arcanearchives.tileentities.ManifestTileEntity;
import com.aranaira.arcanearchives.tileentities.MonitoringCrystalTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantResonatorTileEntity;
import com.aranaira.arcanearchives.tileentities.unused.MatrixCoreTileEntity;
import com.aranaira.arcanearchives.util.ItemStackConsolidator;
import com.aranaira.arcanearchives.util.LargeItemNBTUtil;
import com.aranaira.arcanearchives.util.types.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.*;

public class ServerNetwork {
	// Actual owner of this network
	private UUID uuid;

	// Per-player items/blocks
	private ManifestList manifestItems = new ManifestList(new ArrayList<>());
	private TileList tiles = new TileList(new ArrayList<>());

	// Set of IDs of contained tiles
	private Set<UUID> tileIdSet = new HashSet<>();

	// Per-player handler
	private ManifestItemHandler manifestHandler;

	// Per-player values
	private int totalCores = 0;
	private int totalResonators = 0;

	// Initial set-up
	public ServerNetwork (UUID id) {
		uuid = id;
		manifestHandler = new ManifestItemHandler(manifestItems);
	}

	public static ServerNetwork fromNBT (NBTTagCompound data) {
		ServerNetwork network = new ServerNetwork(null);
		network.deserializeNBT(data);
		return network;
	}

	public TileList.TileListIterable getTiles () {
		return tiles.filterValid();
	}

	public void addTile (ImmanenceTileEntity tileEntityInstance) {
		tileEntityInstance.tryGenerateUUID();

		if (tiles.containsUUID(tileEntityInstance.uuid)) {
			return;
		}

		tiles.add(new IteRef(tileEntityInstance));
		tileEntityInstance.hasBeenAddedToNetwork = true;

		if (tileEntityInstance instanceof RadiantResonatorTileEntity || tileEntityInstance instanceof MatrixCoreTileEntity) {
			rebuildTotals();
		}
	}

	private void removeTile (ImmanenceTileEntity te) {
		tiles.removeByUUID(te.getUuid());

		if (te instanceof RadiantResonatorTileEntity || te instanceof MatrixCoreTileEntity) {
			rebuildTotals();
		}

	}

	private void removeTile (UUID tileID) {
		ImmanenceTileEntity te = tiles.getByUUID(tileID);
		removeTile(te);
	}

	public boolean NetworkContainsTile (ImmanenceTileEntity tileEntityInstance) {
		return NetworkContainsTile(tileEntityInstance.uuid);
	}

	public boolean NetworkContainsTile (UUID tileID) {
		return tiles.containsUUID(tileID);
	}

	public NBTTagCompound serializeNBT () {
		NBTTagCompound tagCompound = new NBTTagCompound();
		tagCompound.setUniqueId("playerId", uuid);

		return tagCompound;
	}

	public void deserializeNBT (NBTTagCompound nbt) {
		uuid = nbt.getUniqueId("playerId");
	}

	public UUID getUUID () {
		return uuid;
	}

	public int getTotalCores () {
		return totalCores;
	}    // TODO: TODO: CHECK

	public int getTotalResonators () {
		return totalResonators;
	}

	public ManifestItemHandler getManifestHandler () {
		return manifestHandler;
	}

	public void rebuildTotals () {
		int origResonators = totalResonators;
		int origCores = totalCores;

		totalResonators = 0;
		totalCores = 0;

		for (IteRef ite : getTiles()) {
			if (ite.clazz.equals(RadiantResonatorTileEntity.class)) {
				totalResonators++;
			} else if (ite.clazz.equals(MatrixCoreTileEntity.class)) {
				totalCores++;
			}
		}

		if (origCores != totalCores || origResonators != totalResonators) {
			synchroniseData();
		}
	}

	public NBTTagCompound buildSynchroniseManifest () {
		// Step one: iterate loaded chests and get item stacks.
		rebuildManifest();

		NBTTagList manifest = new NBTTagList();

		for (ManifestEntry entry : manifestItems) {
			NBTTagCompound itemEntry = new NBTTagCompound();
			LargeItemNBTUtil.writeToNBT(itemEntry, entry.getStack());
			NBTTagList entries = new NBTTagList();
			for (ManifestEntry.ItemEntry iEntry : entry.getEntries()) {
				entries.appendTag(iEntry.serializeNBT());
			}
			itemEntry.setTag(NetworkTags.ENTRIES, entries);
			itemEntry.setInteger(NetworkTags.DIMENSION, entry.getDimension());
			manifest.appendTag(itemEntry);
		}

		NBTTagCompound result = new NBTTagCompound();
		result.setTag(NetworkTags.MANIFEST, manifest);

		return result;
	}

	public void rebuildManifest () {
		manifestItems.clear();

		List<ManifestItemEntry> preManifest = new ArrayList<>();
		Set<ManifestTileEntity> done = new HashSet<>();
		Set<BlockPosDimension> positions = new HashSet<>();
		EntityPlayer player = getPlayer();

		for (IteRef ref : getManifestTileEntities()) {
			ManifestTileEntity ite = ref.getManifestServerTile();
			if (ite == null) {
				continue;
			}

			if (done.contains(ite)) {
				continue;
			}

			int dimId = ite.getWorld().provider.getDimension();

			if (ite.isSingleStackInventory()) {
				ItemStack is = ite.getSingleStack();
				if (!is.isEmpty()) {
					preManifest.add(new ManifestItemEntry(is.copy(), dimId, new ManifestEntry.ItemEntry(ite.getPos(), ite.getChestName(), is.getCount())));
				}
			} else {
				if (ite instanceof MonitoringCrystalTileEntity) {
					MonitoringCrystalTileEntity mte = (MonitoringCrystalTileEntity) ite;

					BlockPos tar = mte.getTarget();
					if (tar == null) {
						continue;
					}

					BlockPosDimension ttar = new BlockPosDimension(tar, mte.dimension);

					if (positions.contains(ttar)) {
						if (player != null) {
							player.sendMessage(new TextComponentTranslation("arcanearchives.error.monitoring_crystal", tar.getX(), tar.getY(), tar.getZ(), ttar.dimension));
						} else {
							ArcaneArchives.logger.error("Multiple Monitoring Crystals were found for network " + uuid.toString() + " targeting " + String.format("%d/%d/%d in dimension %d", tar.getX(), tar.getY(), tar.getZ(), ttar.dimension));
						}
						continue;
					}

					positions.add(ttar);

					IItemHandler handler = mte.getInventory();
					if (handler != null) {
						for (ItemStack is : new SlotIterable(handler)) {
							if (is.isEmpty()) {
								continue;
							}

							preManifest.add(new ManifestItemEntry(is.copy(), dimId, new ManifestEntry.ItemEntry(mte.getTarget(), mte.getDescriptor(), is.getCount())));
						}
					}
				} else {
					for (ItemStack is : new SlotIterable(ite.getInventory())) {
						if (is.isEmpty()) {
							continue;
						}

						preManifest.add(new ManifestItemEntry(is.copy(), dimId, new ManifestEntry.ItemEntry(ite.getPos(), ite.getChestName(), is.getCount())));
					}
				}
			}

			done.add(ite);
		}

		List<ManifestEntry> consolidated = ItemStackConsolidator.ConsolidateManifest(preManifest);
		manifestItems.addAll(consolidated);
	}

	public TileList.TileListIterable getManifestTileEntities () {
		return tiles.filterAssignableClass(ManifestTileEntity.class);
	}

	public NBTTagCompound buildSynchroniseData () {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger(NetworkTags.TOTAL_SPACE, 0); // GetTotalSpace());
		tag.setInteger(NetworkTags.ITEM_COUNT, 0); //GetItemCount());

		NBTTagList pendingList = new NBTTagList();

		rebuildTotals();

		tag.setTag(NetworkTags.INVITES_PENDING, pendingList);
		tag.setInteger(NetworkTags.TOTAL_RESONATORS, totalResonators);
		tag.setInteger(NetworkTags.TOTAL_CORES, totalCores);

		return tag;
	}

	public void Synchronise (PacketNetworks.SynchroniseType type) {
		switch (type) {
			case DATA:
				synchroniseData();
				break;
		}
	}

	@Nullable
	public EntityPlayer getPlayer () {
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		if (server != null) {
			return server.getPlayerList().getPlayerByUUID(uuid);
		}

		return null;
	}

	public boolean synchroniseData () {
		EntityPlayer player = getPlayer();
		if (player != null) {
			IMessage packet = new PacketNetworks.Response(PacketNetworks.SynchroniseType.DATA, uuid, buildSynchroniseData());
			NetworkHandler.CHANNEL.sendTo(packet, (EntityPlayerMP) player);
			return true;
		}

		return false;
	}

	public UUID generateTileId () {
		UUID newId = UUID.randomUUID();
		while (tileIdSet.contains(newId)) {
			newId = UUID.randomUUID();
		}

		return newId;
	}

	public void handleTileIdChange (UUID oldId, UUID newId) {
		removeTile(oldId);
		tileIdSet.remove(oldId);
		tileIdSet.add(newId);
	}

	public static class ManifestItemEntry {
		public ItemStack stack;
		public int dim;
		public ManifestEntry.ItemEntry entry;

		public ManifestItemEntry (ItemStack stack, int dim, ManifestEntry.ItemEntry entry) {
			this.stack = stack;
			this.dim = dim;
			this.entry = entry;
		}
	}

	public static class BlockPosDimension {
		public BlockPos pos;
		public int dimension;

		public BlockPosDimension (BlockPos pos, int dimension) {
			this.pos = pos;
			this.dimension = dimension;
		}

		@Override
		public boolean equals (Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}
			BlockPosDimension that = (BlockPosDimension) o;
			return dimension == that.dimension && Objects.equals(pos, that.pos);
		}

		@Override
		public int hashCode () {
			return Objects.hash(pos, dimension);
		}
	}


}

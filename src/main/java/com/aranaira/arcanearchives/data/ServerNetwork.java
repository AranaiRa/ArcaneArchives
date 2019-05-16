package com.aranaira.arcanearchives.data;

import com.aranaira.arcanearchives.ArcaneArchives;
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

	// Per-player values
	private int totalCores = 0;
	private int totalResonators = 0;

	// Initial set-up
	public ServerNetwork (UUID id) {
		uuid = id;
	}

	/**
	 * Simply returns the network's uuid.
	 */
	public UUID getUuid () {
		return uuid;
	}

	/**
	 * Attempts to fetch the player associated with this network.
	 * Returns null if they do not exist or are offline.
	 */
	@Nullable
	private EntityPlayer getPlayer () {
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		if (server != null) {
			return server.getPlayerList().getPlayerByUUID(uuid);
		}

		return null;
	}

	/***
	 * Static entrypoint used by AAWorldSavedData
	 */
	public static ServerNetwork fromNBT (NBTTagCompound data) {
		ServerNetwork network = new ServerNetwork(null);
		network.readFromSave(data);
		return network;
	}

	/**
	 * Function for retrieving the current list of valid tiles from the network.
	 */
	public TileList.TileListIterable getValidTiles () {
		return tiles.filterValid();
	}

	/**
	 * Attempts to return a unique tile id.
	 */
	public UUID generateTileUuid () {
		UUID newId = UUID.randomUUID();
		while (tileIdSet.contains(newId)) {
			newId = UUID.randomUUID();
		}

		return newId;
	}

	/**
	 * TODO: Honestly, I'm not sure what this does any more.
	 * Theoretically it removes the tile under its old uuid and
	 * inserts it under its new uuid. I don't know what circumstances
	 * there could be that would involve this actually happening
	 * though.
	 */
	public void handleTileIdChange (UUID oldId, UUID newId) {
		removeTile(oldId);
		tileIdSet.remove(oldId);
		tileIdSet.add(newId);
	}

	/**
	 * Function for adding tile entities to the network
	 */
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

	/**
	 * Functions for removing tile entities from the network.
	 */
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

	/**
	 * Block of functions used for determining if this network contains an ITE or tileId.
	 */
	public boolean containsTile (ImmanenceTileEntity tileEntityInstance) {
		return containsTile(tileEntityInstance.uuid);
	}

	public boolean containsTile (UUID tileID) {
		return tiles.containsUUID(tileID);
	}

	/**
	*	Functions used for interacting with AAWorldSavedData.
	 */
	public NBTTagCompound writeToSave () {
		NBTTagCompound tagCompound = new NBTTagCompound();
		tagCompound.setUniqueId("playerId", uuid);

		return tagCompound;
	}

	public void readFromSave (NBTTagCompound tag) {
		this.uuid = tag.getUniqueId("playerId");
	}

	/**
	 * Entrypoint only used by ContainerManifest previously
	 */
	/*public ManifestItemHandler getManifestHandler () {
		return manifestHandler;
	}*/

	/**
	 * Contains code relating to per-player limits for resonators (and Matrices)
	 */
	public int getTotalCores () {
		return totalCores;
	}

	public int getTotalResonators () {
		return totalResonators;
	}

	public void rebuildTotals () {
		int origResonators = totalResonators;
		int origCores = totalCores;

		totalResonators = 0;
		totalCores = 0;

		// Note this only includes valid tiles.
		for (IteRef ite : getValidTiles()) {
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

	/**
	 * Attempts to synchronsie the rebuilt core total back to the client,
	 * where the information is used to prevent additional placement of
	 * resonators client-side in addition to server-side to prevent
	 * ghosting.
	 */
	private void synchroniseData () {
		EntityPlayer player = getPlayer();
		if (player != null) {
			IMessage packet = new PacketNetworks.Response(PacketNetworks.SynchroniseType.DATA, uuid, buildSynchroniseData());
			NetworkHandler.CHANNEL.sendTo(packet, (EntityPlayerMP) player);
		}
	}

	/**
	 * Function that should be improved for the manifest.
	 * Converts the manifestItems list into an NBT form
	 * for return to the client.
	 */
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

	/**
	 * Rebuilds the manifestItems list.
	 */
	private void rebuildManifest () {
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

	/**
	 * Fetches only manifest tile entites: radiant chests & troves.
	 * TODO: Get rid of additional classes and use the predicate instead.
	 */
	private TileList.TileListIterable getManifestTileEntities () {
		return tiles.filterAssignableClass(ManifestTileEntity.class);
	}

	/**
	 * Code specifically for synchronising data to the player.
	 * Currently this only contains the total number of
	 * resonators and matrix cores.
	 */
	public NBTTagCompound buildSynchroniseData () {
		NBTTagCompound tag = new NBTTagCompound();
		rebuildTotals();

		tag.setInteger(NetworkTags.TOTAL_RESONATORS, getTotalResonators());
		tag.setInteger(NetworkTags.TOTAL_CORES, getTotalCores());

		return tag;
	}

}

package com.aranaira.arcanearchives.data;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.network.NetworkHandler;
import com.aranaira.arcanearchives.network.PacketNetworks;
import com.aranaira.arcanearchives.network.PacketNetworks.SynchroniseType;
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
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.*;

public class ServerNetwork implements IServerNetwork {
	// Actual owner of this network
	private UUID uuid;
	private WeakReference<World> world;

	// TODO: Invalidate this when joining a network or leaving a network
	private Boolean isHiveMember;

	// Per-player items/blocks
	private ManifestList manifestItems = new ManifestList(new ArrayList<>());
	private TileList tiles = new TileList(new ArrayList<>());

	// Set of IDs of contained tiles
	private Set<UUID> tileIdSet = new HashSet<>();
	private Set<UUID> safeLimitedIDs = new HashSet<>();

	// Per-player values
	private int totalCores = 0;
	private int totalResonators = 0;
	private int maxDistance = 0;

	// Initial set-up
	public ServerNetwork (UUID id) {
		uuid = id;
	}

	public int getMaxDistance () {
		return maxDistance * maxDistance;
	}

	public void setMaxDistance (int maxDistance) {
		this.maxDistance = maxDistance;
	}

	/***
	 * Static entrypoint used by NetworkSaveData
	 */
	public static ServerNetwork fromNBT (NBTTagCompound data) {
		ServerNetwork network = new ServerNetwork(null);
		network.readFromSave(data);
		return network;
	}

	@Override
	public World getWorld () {
		if (world == null || world.get() == null) {
			world = new WeakReference<>(FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld());
		}

		return world.get();
	}

	@Override
	public UUID getUuid () {
		return uuid;
	}

	public boolean isSafe (UUID id) {
		return safeLimitedIDs.contains(id);
	}

	/**
	 * Attempts to fetch the player associated with this network.
	 * Returns null if they do not exist or are offline.
	 */
	@Nullable
	public EntityPlayer getPlayer () {
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		if (server != null) {
			return server.getPlayerList().getPlayerByUUID(uuid);
		}

		return null;
	}

	@Override
	public TileList.TileListIterable getValidTiles () {
		return tiles.filterValid();
	}

	@Override
	public UUID generateTileUuid () {
		UUID newId = UUID.randomUUID();
		while (tileIdSet.contains(newId)) {
			newId = UUID.randomUUID();
		}

		return newId;
	}

	@Override
	public void handleTileIdChange (UUID oldId, UUID newId) {
		tileIdSet.remove(oldId);
		tileIdSet.add(newId);
		safeLimitedIDs.remove(oldId);
		safeLimitedIDs.add(newId);
		tiles.updateUUID(oldId, newId);
	}

	@Override
	public void addTile (ImmanenceTileEntity tileEntityInstance) {
		tileEntityInstance.tryGenerateUUID();

		if (tiles.containsUUID(tileEntityInstance.uuid)) {
			return;
		}

		tiles.add(new IteRef(tileEntityInstance));
		tileEntityInstance.hasBeenAddedToNetwork = true;

		if (tileEntityInstance instanceof RadiantResonatorTileEntity || tileEntityInstance instanceof MatrixCoreTileEntity) {
			safeLimitedIDs.add(tileEntityInstance.uuid);
			rebuildTotals();
		}
	}

	/**
	 * Functions for removing tile entities from the network.
	 */
	@Override
	public void removeTile (ImmanenceTileEntity te) {
		tiles.removeByUUID(te.getUuid());

		if (te instanceof RadiantResonatorTileEntity || te instanceof MatrixCoreTileEntity) {
			safeLimitedIDs.remove(te.uuid);
			rebuildTotals();
		}
	}

	@Override
	public void removeTile (UUID tileID) {
		ImmanenceTileEntity te = tiles.getByUUID(tileID);
		removeTile(te);
	}

	@Override
	public boolean containsTile (ImmanenceTileEntity tileEntityInstance) {
		return containsTile(tileEntityInstance.uuid);
	}

	@Override
	public boolean containsTile (UUID tileID) {
		return tiles.containsUUID(tileID);
	}

	@Override
	public NBTTagCompound writeToSave () {
		NBTTagCompound tagCompound = new NBTTagCompound();
		tagCompound.setUniqueId("playerId", uuid);

		return tagCompound;
	}

	@Override
	public void readFromSave (NBTTagCompound tag) {
		this.uuid = tag.getUniqueId("playerId");
	}

	/**
	 * Entrypoint only used by ContainerManifest previously
	 */
	/*public ManifestItemHandler getManifestHandler () {
		return manifestHandler;
	}*/
	@Override
	public int getTotalCores () {
		return totalCores;
	}

	@Override
	public int getTotalResonators () {
		return totalResonators;
	}

	@Override
	public void rebuildTotals () {
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

		synchroniseData();
	}

	/**
	 * Attempts to synchronsie the rebuilt core total back to the client,
	 * where the information is used to prevent additional placement of
	 * resonators client-side in addition to server-side to prevent
	 * ghosting.
	 */
	@Override
	public void synchroniseData () {
		EntityPlayer player = getPlayer();
		if (player != null) {
			IMessage packet = new PacketNetworks.Response(PacketNetworks.SynchroniseType.DATA, buildSynchroniseData());
			NetworkHandler.CHANNEL.sendTo(packet, (EntityPlayerMP) player);
		}
	}

	@Override
	public void synchroniseHiveInfo () {
		EntityPlayer player = getPlayer();
		if (player != null) {
			IMessage packet = new PacketNetworks.Response(SynchroniseType.HIVE_STATUS, buildHiveMembershipData());
			NetworkHandler.CHANNEL.sendTo(packet, (EntityPlayerMP) player);
		}
	}

	public NBTTagCompound buildHiveMembershipData () {
		return NetworkHelper.getHiveMembershipInfo(uuid, getWorld());
	}

	@Override
	public boolean isHiveNetwork () {
		return false;
	}

	@Override
	public boolean isHiveMember () {
		return NetworkHelper.isHiveMember(uuid, getWorld());
	}

	@Override
	@Nullable
	public List<ServerNetwork> getContainedNetworks () {
		return null;
	}

	@Override
	public NBTTagCompound buildHiveManifest (EntityPlayer player) {
		return null;
	}

	@Override
	@Nullable
	public ServerNetwork getOwnerNetwork () {
		return null;
	}

	@Override
	@Nullable
	public HiveNetwork getHiveNetwork () {
		return NetworkHelper.getHiveNetwork(uuid, getWorld());
	}

	@Override
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
			itemEntry.setBoolean(NetworkTags.OUT_OF_RANGE, entry.outOfRange);
			manifest.appendTag(itemEntry);
		}

		NBTTagCompound result = new NBTTagCompound();
		result.setTag(NetworkTags.MANIFEST, manifest);

		return result;
	}

	/**
	 * Rebuilds the manifestItems list.
	 */
	public void rebuildManifest () {
		manifestItems.clear();

		List<ManifestItemEntry> preManifest = new ArrayList<>();
		Set<ManifestTileEntity> done = new HashSet<>();
		Set<BlockPosDimension> positions = new HashSet<>();
		EntityPlayer player = getPlayer();

		int maxDistance = getMaxDistance();

		for (IteRef ref : getManifestTileEntities()) {
			ManifestTileEntity ite = ref.getManifestServerTile();
			if (ite == null) {
				continue;
			}

			if (done.contains(ite)) {
				continue;
			}

			boolean outOfRange = distanceSq(ite.getPos(), player.getPosition()) >= maxDistance;
			int dimId = ite.getWorld().provider.getDimension();

			if (ite.isSingleStackInventory()) {
				ItemStack is = ite.getSingleStack();
				if (!is.isEmpty()) {
					preManifest.add(new ManifestItemEntry(is.copy(), dimId, new ManifestEntry.ItemEntry(ite.getPos(), ite.getChestName(), is.getCount()), outOfRange));
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

							preManifest.add(new ManifestItemEntry(is.copy(), dimId, new ManifestEntry.ItemEntry(mte.getTarget(), mte.getDescriptor(), is.getCount()), outOfRange));
						}
					}
				} else {
					for (ItemStack is : new SlotIterable(ite.getInventory())) {
						if (is.isEmpty()) {
							continue;
						}

						preManifest.add(new ManifestItemEntry(is.copy(), dimId, new ManifestEntry.ItemEntry(ite.getPos(), ite.getChestName(), is.getCount()), outOfRange));
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
	public TileList.TileListIterable getManifestTileEntities () {
		return tiles.filterAssignableClass(ManifestTileEntity.class);
	}

	@Override
	public NBTTagCompound buildSynchroniseData () {
		NBTTagCompound tag = new NBTTagCompound();

		tag.setInteger(NetworkTags.TOTAL_RESONATORS, getTotalResonators());
		tag.setInteger(NetworkTags.TOTAL_CORES, getTotalCores());

		return tag;
	}

	public int distanceSq (BlockPos pos1, BlockPos pos2) {
		int d1 = pos1.getX() - pos2.getX();
		int d2 = pos1.getY() - pos2.getY();
		int d3 = pos1.getZ() - pos2.getZ();
		return Math.abs(d1 * d1 + d2 * d2 + d3 * d3);
	}
}

package com.aranaira.arcanearchives.data.types;

import com.aranaira.arcanearchives.api.immanence.IImmanenceBus;
import com.aranaira.arcanearchives.api.immanence.IImmanenceSubscriber;
import com.aranaira.arcanearchives.data.DataHelper;
import com.aranaira.arcanearchives.immanence.ImmanenceBus;
import com.aranaira.arcanearchives.network.Networking;
import com.aranaira.arcanearchives.network.PacketNetworks;
import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import com.aranaira.arcanearchives.tileentities.MatrixCoreTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantResonatorTileEntity;
import com.aranaira.arcanearchives.tileentities.interfaces.IManifestTileEntity;
import com.aranaira.arcanearchives.types.IteRef;
import com.aranaira.arcanearchives.types.iterators.TileListIterable;
import com.aranaira.arcanearchives.types.lists.ManifestList;
import com.aranaira.arcanearchives.types.lists.TileList;
import com.aranaira.arcanearchives.util.ManifestUtils;
import com.aranaira.arcanearchives.util.ManifestUtils.CollatedEntry;
import com.aranaira.arcanearchives.util.ManifestUtils.ItemEntry;
import com.aranaira.arcanearchives.util.TileUtils;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap.Entry;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.*;

public class ServerNetwork implements IServerNetwork {
	private static ArrayList<ServerNetwork> FAKE_MEMBERS = new ArrayList<>();

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

	// Configuration values
	private int maxDistance = 0;
	private boolean defaultRoutingNoNewItems = false;
	private boolean trovesDispense = true;

	private ImmanenceBus immanenceBus = new ImmanenceBus(this);

	// Initial set-up
	public ServerNetwork (UUID id) {
		uuid = id;
	}

	public boolean getNoNewDefault () {
		return defaultRoutingNoNewItems;
	}

	public void setNoNewDefault (boolean defaultRoutingNoNewItems) {
		this.defaultRoutingNoNewItems = defaultRoutingNoNewItems;
	}

	public int getMaxDistance () {
		return maxDistance * maxDistance;
	}

	public void setMaxDistance (int maxDistance) {
		this.maxDistance = maxDistance;
	}

	public boolean getTrovesDispense () {
		return trovesDispense;
	}

	public void setTrovesDispense (boolean trovesDispense) {
		this.trovesDispense = trovesDispense;
	}

	@Nullable
	public ImmanenceTileEntity getImmanenceTile (UUID tileId) {
		return tiles.getByUUID(tileId);
	}

	@Override
	public TileList getTiles () {
		return tiles;
	}

	@Override
	public boolean anyLoaded () {
		Int2ObjectOpenHashMap<Set<ChunkPos>> map = new Int2ObjectOpenHashMap<>();
		for (DimensionType i : DimensionType.values()) {
			map.put(i.getId(), new HashSet<>());
		}

		for (IteRef ref : tiles) {
			map.get(ref.dimension).add(new ChunkPos(ref.pos));
		}

		for (Entry<Set<ChunkPos>> entry : map.int2ObjectEntrySet()) {
			if (entry.getValue().isEmpty()) {
				continue;
			}
			int dimension = entry.getIntKey();
			Set<ChunkPos> chunks = entry.getValue();

			WorldServer dimWorld = DimensionManager.getWorld(dimension);
			ChunkProviderServer provider = dimWorld.getChunkProvider();
			for (ChunkPos pos : chunks) {
				if (provider.chunkExists(pos.x, pos.z)) {
					return true;
				}
			}
		}

		return false;
	}

	public void tileEntityMoved (UUID tileId, BlockPos newPosition) {
		IteRef ref = getTiles().getReference(tileId);
		if (ref == null) {
			return;
		}

		ref.pos = newPosition;
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
	@Override
	@Nullable
	public EntityPlayer getPlayer () {
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		if (server != null) {
			return server.getPlayerList().getPlayerByUUID(uuid);
		}

		return null;
	}

	@Override
	public Iterable<IteRef> getValidTiles () {
		return TileUtils.filterValid(this.tiles);
	}

	@Override
	public Iterable<IteRef> getImmananceTiles () {
		this.tiles.sortPriority();
		return TileUtils.filterAssignableClass(this.tiles, IImmanenceSubscriber.class);
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

	// TODO: Reconsider this
	@Override
	public void addTile (ImmanenceTileEntity tileEntityInstance) {
		tileEntityInstance.tryGenerateUUID();

		// TODO: WHAT WAS I THINKING???
		if (tiles.containsUUID(tileEntityInstance.uuid)) {
			IteRef ref = tiles.getReference(tileEntityInstance.uuid);
			if (ref != null) {
				ref.networkPriority = tileEntityInstance.getNetworkPriority();
				ref.pos = tileEntityInstance.getPos();
				ref.clazz = tileEntityInstance.getClass();
				ref.dimension = tileEntityInstance.dimension;
				ref.uuid = tileEntityInstance.getUuid();
				return;
			}
		}

		tiles.add(new IteRef(tileEntityInstance));
		tileEntityInstance.hasBeenAddedToNetwork = true;

		if (tileEntityInstance instanceof RadiantResonatorTileEntity || tileEntityInstance instanceof MatrixCoreTileEntity) {
			safeLimitedIDs.add(tileEntityInstance.uuid);
			rebuildTotals();
		}

		tileEntityInstance.joinedNetwork(this);
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
	public void updateTile (ImmanenceTileEntity tileEntityInstance) {
		tiles.updateReference(tileEntityInstance);
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

	@Override
	public ManifestList buildSynchroniseManifest () {
		rebuildManifest();
		return manifestItems;
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
			IMessage packet = new PacketNetworks.DataResponse(buildSynchroniseData());
			Networking.CHANNEL.sendTo(packet, (EntityPlayerMP) player);
		}
	}

	@Override
	public void synchroniseHiveInfo () {
		EntityPlayer player = getPlayer();
		if (player != null) {
			IMessage packet = new PacketNetworks.HiveResponse(buildHiveMembershipData());
			Networking.CHANNEL.sendTo(packet, (EntityPlayerMP) player);
		}
	}

	@Override
	public HiveMembershipInfo buildHiveMembershipData () {
		return DataHelper.getHiveMembershipInfo(uuid);
	}

	@Override
	public boolean isHiveMember () {
		return DataHelper.isHiveMember(uuid);
	}

	@Override
	public List<ServerNetwork> getContainedNetworks () {
		return FAKE_MEMBERS;
	}

	@Override
	public IImmanenceBus getImmanenceBus () {
		return immanenceBus;
	}

	@Override
	public ServerNetwork getOwnerNetwork () {
		return this;
	}

	@Override
	@Nullable
	public HiveNetwork getHiveNetwork () {
		World world = getWorld();
		if (DataHelper.isHiveMember(uuid)) {
			return DataHelper.getHiveNetwork(uuid);
		} else {
			return null;
		}
	}

	/**
	 * Rebuilds the manifestItems list.
	 */
	@Override
	public void rebuildManifest () {
		manifestItems.clear();

		Map<Integer, List<ItemEntry>> preManifest = ManifestUtils.buildItemEntryList(this);
		List<CollatedEntry> manifestList = ManifestUtils.parsePreManifest(preManifest, this);

		manifestItems.addAll(manifestList);
	}

	/**
	 * Fetches only manifest tile entites: radiant chests & troves.
	 */
	@Override
	public TileListIterable getManifestTileEntities () {
		return TileUtils.filterAssignableClass(this.tiles, IManifestTileEntity.class);
	}

	@Override
	public SynchroniseInfo buildSynchroniseData () {
		SynchroniseInfo info = new SynchroniseInfo();
		info.totalResonators = getTotalResonators();
		info.totalCores = getTotalCores();

		return info;
	}

	public int distanceSq (BlockPos pos1, BlockPos pos2) {
		int d1 = pos1.getX() - pos2.getX();
		int d2 = pos1.getY() - pos2.getY();
		int d3 = pos1.getZ() - pos2.getZ();
		return Math.abs(d1 * d1 + d2 * d2 + d3 * d3);
	}

	public int distanceSqNoVertical (BlockPos pos1, BlockPos pos2) {
		int d1 = pos1.getX() - pos2.getX();
		int d2 = pos2.getZ() - pos2.getZ();
		return Math.abs(d1 * d1 + d2 * d2);
	}

	public boolean inRange (BlockPos pos1, BlockPos pos2) {
		int maxDistance = getMaxDistance();
		int distance = distanceSq(pos1, pos2);
		return distance < maxDistance;
	}
}

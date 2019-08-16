package com.aranaira.arcanearchives.data;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.data.HiveSaveData.Hive;
import com.aranaira.arcanearchives.types.ISerializeByteBuf;
import com.google.common.collect.Iterators;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class DataHelper {
	public static UUID INVALID = UUID.fromString("00000000-0000-0000-0000-000000000000");
	// TODO: This needs to be cleared whenever the player enters a new world
	private static Map<UUID, ClientNetwork> CLIENT_MAP = new HashMap<>();

	public static void clearClientCache () {
		CLIENT_MAP.clear();
	}

	/**
	 * Fetches an Arcane Archives server-side network fetched from the World object passed in.
	 * Returns null if either argument is null or the requested network is invalid.
	 *
	 * @param uuid  The player/network UUID
	 * @param world The world object
	 * @return An ServerNetwork instance for the given id, or null if it was not found.
	 */
	@Nullable
	public static ServerNetwork getServerNetwork (UUID uuid, World world) {
		if (!checkUUIDAndWorld(uuid, world)) {
			return null;
		}

		NetworkSaveData saveData = getNetworkData(world);

		return saveData.getNetwork(uuid);
	}

	public static WorldServer getWorld () {
		return FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0);
	}

	public static NetworkSaveData getNetworkData () {
		return getNetworkData(getWorld());
	}

	public static class ServerList implements Iterable<IHiveBase> {
		public List<ServerNetwork> independents;
		public List<HiveNetwork> hives;

		public ServerList (List<ServerNetwork> independents, List<HiveNetwork> hives) {
			this.independents = independents;
			this.hives = hives;
		}

		@Override
		public Iterator<IHiveBase> iterator () {
			return Iterators.concat(independents.iterator(), hives.iterator());
		}
	}

	public static ServerList getNetworks (){
		NetworkSaveData networkData = getNetworkData();
		HiveSaveData hiveData = getHiveData();

		List<ServerNetwork> independents = new ArrayList<>();
		List<HiveNetwork> hives = new ArrayList<>();

		Set<UUID> hiveMembers = new HashSet<>();
		Set<UUID> ownerUuids = hiveData.getHiveOwners();

		Collection<ServerNetwork> baseNetworks = networkData.getAllNetworks();

		for (ServerNetwork network : baseNetworks) {
			UUID nid = network.getUuid();
			if (ownerUuids.contains(nid)) {
				HiveNetwork hive = network.getHiveNetwork();
				hives.add(hive);
				for (ServerNetwork contained: hive.getContainedNetworks()) {
					hiveMembers.add(contained.getUuid());
				}
			} else if (!hiveMembers.contains(nid)) {
				independents.add(network);
			}
		}

		return new ServerList(independents, hives);
	}

	public static NetworkSaveData getNetworkData (World world) {
		NetworkSaveData saveData = (NetworkSaveData) world.getMapStorage().getOrLoadData(NetworkSaveData.class, NetworkSaveData.ID);

		if (saveData == null) {
			saveData = new NetworkSaveData();
			world.getMapStorage().setData(NetworkSaveData.ID, saveData);
		}

		return saveData;
	}

	public static PlayerSaveData getPlayerData (World world, EntityPlayer player) {
		PlayerSaveData saveData = (PlayerSaveData) world.getMapStorage().getOrLoadData(PlayerSaveData.class, PlayerSaveData.ID(player));
		if (saveData == null) {
			saveData = new PlayerSaveData(player);
			world.getMapStorage().setData(saveData.getId(), saveData);
		}

		return saveData;
	}

	public static HiveSaveData getHiveData () {
		World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0);
		return getHiveData(world);
	}

	public static HiveSaveData getHiveData (World world) {
		HiveSaveData saveData = (HiveSaveData) world.getMapStorage().getOrLoadData(HiveSaveData.class, HiveSaveData.ID);
		if (saveData == null) {
			saveData = new HiveSaveData();
			world.getMapStorage().setData(HiveSaveData.ID, saveData);
		}

		return saveData;
	}

	public static class HiveMembershipInfo implements ISerializeByteBuf<HiveMembershipInfo> {
		public boolean isOwner = false;
		public boolean inHive = false;

		@Override
		public HiveMembershipInfo fromBytes (ByteBuf buf) {
			isOwner = buf.readBoolean();
			inHive = buf.readBoolean();
			return this;
		}

		@Override
		public void toBytes (ByteBuf buf) {
			buf.writeBoolean(isOwner);
			buf.writeBoolean(inHive);
		}

		public static HiveMembershipInfo deserialize (ByteBuf buf) {
			HiveMembershipInfo info = new HiveMembershipInfo();
			return info.fromBytes(buf);
		}
	}

	public static HiveMembershipInfo getHiveMembershipInfo (UUID uuid, World world) {
		HiveSaveData saveData = getHiveData(world);
		Hive hive = saveData.getHiveByMember(uuid);

		HiveMembershipInfo info = new HiveMembershipInfo();

		if (hive == null) {
			return info;
		}

		if (hive.owner.equals(uuid)) {
			info.isOwner = true;
			info.inHive = true;
		}

		if (hive.members.contains(uuid)) {
			info.inHive = true;
		}

		return info;
	}

	public static boolean isHiveMember (UUID uuid, World world) {
		HiveSaveData saveData = getHiveData(world);
		return saveData.getHiveByMember(uuid) != null;
	}

	@Nullable
	public static HiveNetwork getHiveNetwork (UUID uuid, World world) {
		HiveSaveData saveData = getHiveData(world);
		return getHiveNetwork(saveData.getHiveByMember(uuid), world);
	}

	@Nullable
	public static HiveNetwork getHiveNetwork (Hive hive, World world) {
		ServerNetwork owner = getServerNetwork(hive.owner, world);
		List<ServerNetwork> members = new ArrayList<>();
		for (UUID member : hive.members) {
			ServerNetwork m = getServerNetwork(member, world);
			assert m != null;
			members.add(m);
		}

		return new HiveNetwork(owner, members);
	}

	public static boolean checkUUIDAndWorld (UUID uuid, World world) {
		if (uuid == null || uuid.equals(INVALID)) {
			ArcaneArchives.logger.warn(() -> "Attempted to fetch an invalid archive: " + uuid);
			ArcaneArchives.logger.warn("Trace:", new InvalidNetworkException("UUID must be valid"));
			// In an ideal situation, we won't need these checks, but it's useful for notifying about bugs with a
			// reduction in the chance of crashing the server/client
			return false;
		}
		if (world == null || world.getMapStorage() == null) {
			ArcaneArchives.logger.error(String.format("Attempted to load a network for %s, but the world or its storage is null!", uuid.toString()));
			ArcaneArchives.logger.warn("Trace:", new InvalidNetworkException("World is null!"));
			return false;
		}
		return true;
	}

	public static ClientNetwork getClientNetwork (UUID uuid) {
		if (uuid == null || uuid.equals(INVALID)) {
			return null;
		}

		if (CLIENT_MAP.containsKey(uuid)) {
			return CLIENT_MAP.get(uuid);
		} else {
			ClientNetwork net = new ClientNetwork(uuid);
			CLIENT_MAP.put(uuid, net);
			return net;
		}
	}

	@SideOnly(Side.CLIENT)
	public static ClientNetwork getClientNetwork () {
		Minecraft mc = Minecraft.getMinecraft();
		UUID id = mc.player.getUniqueID();
		return getClientNetwork(id);
	}

	public static class InvalidNetworkException extends NullPointerException {
		InvalidNetworkException (String s) {
			super(s);
		}
	}
}

/*package com.aranaira.arcanearchives.data;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.data.HiveSaveData.Hive;
import com.aranaira.arcanearchives.data.types.*;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.*;

public class DataHelper {
	public static UUID INVALID = UUID.fromString("00000000-0000-0000-0000-000000000000");
	// TODO: This needs to be cleared whenever the player enters a new world
	private static Map<UUID, ClientNetwork> CLIENT_MAP = new HashMap<>();

	public static void clearClientCache () {
		CLIENT_MAP.clear();
	}

	*//**
 * Fetches an Arcane Archives server-side network fetched from the World object passed in.
 * Returns null if either argument is null or the requested network is invalid.
 *
 * @param uuid The player/network UUID
 * @return An ServerNetwork instance for the given id, or null if it was not found.
 *//*
	@Nullable
	public static ServerNetwork getServerNetwork (UUID uuid) {
		WorldServer world = getWorld();
		if (!checkUUID(uuid) || !checkWorld(world)) {
			return null;
		}

		NetworkSaveData saveData = getNetworkData();

		return saveData.getNetwork(uuid);
	}

	public static WorldServer getWorld () {
		return FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0);
	}

	public static ServerList getNetworks () {
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
				for (ServerNetwork contained : hive.getContainedNetworks()) {
					hiveMembers.add(contained.getUuid());
				}
			} else if (!hiveMembers.contains(nid)) {
				independents.add(network);
			}
		}

		return new ServerList(independents, hives);
	}

	public static NetworkSaveData getNetworkData () {
		WorldServer world = getWorld();
		NetworkSaveData saveData = (NetworkSaveData) Objects.requireNonNull(world.getMapStorage()).getOrLoadData(NetworkSaveData.class, NetworkSaveData.ID);

		if (saveData == null) {
			saveData = new NetworkSaveData();
			world.getMapStorage().setData(NetworkSaveData.ID, saveData);
		}

		return saveData;
	}

	public static PlayerSaveData getPlayerData (EntityPlayer player) {
		WorldServer world = getWorld();
		PlayerSaveData saveData = (PlayerSaveData) Objects.requireNonNull(world.getMapStorage()).getOrLoadData(PlayerSaveData.class, PlayerSaveData.ID(player));
		if (saveData == null) {
			saveData = new PlayerSaveData(player);
			world.getMapStorage().setData(saveData.getId(), saveData);
		}

		return saveData;
	}

	public static HiveSaveData getHiveData () {
		World world = getWorld();
		HiveSaveData saveData = (HiveSaveData) Objects.requireNonNull(world.getMapStorage()).getOrLoadData(HiveSaveData.class, HiveSaveData.ID);
		if (saveData == null) {
			saveData = new HiveSaveData();
			world.getMapStorage().setData(HiveSaveData.ID, saveData);
		}

		return saveData;
	}

	@Nullable
	public static HiveMembershipInfo getHiveMembershipInfo (UUID uuid) {
		if (!checkUUID(uuid)) {
			return null;
		}

		HiveSaveData saveData = getHiveData();
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

	public static boolean isHiveMember (UUID uuid) {
		if (!checkUUID(uuid)) {
			return false;
		}

		HiveSaveData saveData = getHiveData();
		return saveData.getHiveByMember(uuid) != null;
	}

	@Nullable
	public static HiveNetwork getHiveNetwork (UUID uuid) {
		if (!checkUUID(uuid)) {
			return null;
		}
		HiveSaveData saveData = getHiveData();
		Hive hive = saveData.getHiveByMember(uuid);
		if (hive == null) {
			return null;
		} else {
			return getHiveNetwork(hive);
		}
	}

	public static HiveNetwork getHiveNetwork (Hive hive) {
		ServerNetwork owner = getServerNetwork(hive.owner);
		List<ServerNetwork> members = new ArrayList<>();
		for (UUID member : hive.members) {
			ServerNetwork m = getServerNetwork(member);
			if (m == null) {
				ArcaneArchives.logger.error("Attempted to insert a null network for member " + member.toString() + " into Hive for " + hive.owner.toString(), new InvalidNetworkException("Null Hive Member"));
				continue;
			}
			members.add(m);
		}

		return new HiveNetwork(owner, members);
	}

	public static boolean checkUUID (UUID uuid) {
		if (uuid == null) {
			ArcaneArchives.logger.warn("Attempted to fetch a network with a null UUID", new InvalidNetworkException("Null Network UUID"));
			return false;
		} else if (uuid.equals(INVALID)) {
			ArcaneArchives.logger.warn("Attempted to fetch a network with a blank (invalid) UUID", new InvalidNetworkException("Invalid UUID"));
			return false;
		} else {
			return true;
		}
	}

	public static boolean checkWorld (WorldServer world) {
		if (world == null) {
			ArcaneArchives.logger.error("Attempted to load a network, but the world is null!", new InvalidNetworkException("World is null"));
			return false;
		} else if (world.getMapStorage() == null) {
			ArcaneArchives.logger.error("Attempted to load a network, but the world's map storage is null!", new InvalidNetworkException("Map Storage is null"));
			return false;
		} else {
			return true;
		}
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

	public static AccessorSaveData getAcccessorData () {
		World world = getWorld();
		AccessorSaveData saveData = (AccessorSaveData) Objects.requireNonNull(world.getMapStorage()).getOrLoadData(AccessorSaveData.class, AccessorSaveData.ID);
		if (saveData == null) {
			saveData = new AccessorSaveData();
			world.getMapStorage().setData(AccessorSaveData.ID, saveData);
		}

		return saveData;
	}
}*/

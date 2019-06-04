package com.aranaira.arcanearchives.data;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.data.HiveSaveData.Hive;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.*;

public class NetworkHelper {
	public static UUID INVALID = UUID.fromString("00000000-0000-0000-0000-000000000000");
	// TODO: This needs to be cleared whenever the player enters a new world
	private static Map<UUID, ClientNetwork> CLIENT_MAP = new HashMap<>();
	private static Map<UUID, HiveNetwork> HIVE_MAP = new HashMap<>();

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
		if (!checkUUIDAndWorld(uuid, world)) return null;

		NetworkSaveData saveData = (NetworkSaveData) world.getMapStorage().getOrLoadData(NetworkSaveData.class, NetworkSaveData.ID);

		if (saveData == null) {
			saveData = new NetworkSaveData();
			world.getMapStorage().setData(NetworkSaveData.ID, saveData);
		}

		return saveData.getNetwork(uuid);
	}

	public static boolean addToNetwork (UUID owner, UUID newMember, World world) {
		// This requires the handling of the creation of the new network
		// as well as actually saving the data now that it has been modified.
		// Returns true if it successfully added the newMember to the network
		HiveSaveData saveData = (HiveSaveData) world.getMapStorage().getOrLoadData(HiveSaveData.class, HiveSaveData.ID);

		Hive owned = saveData.getHiveByOwner(owner);
		Hive possible = getHiveMembership(newMember, world);
		if (owned.getOwner().equals(newMember) || (possible != null && !(possible.getOwner().equals(owner)))) {
			return false; // They're already a member of another network
		}
		if (possible != null && possible.getOwner().equals(owner)) return true; // They're already a member; nothing to do

		// Now we worry about a new hive
		if (owned == null) {
			// The two founding members, hooray!
			owned = new Hive(owner);
			saveData.addHive(owned);
		}

		owned.addMember(newMember);
		saveData.addMember(owned, newMember);
		saveData.markDirty();
		world.getMapStorage().saveAllData();
		return true;
	}

	public static boolean removeFromNetwork (UUID owner, UUID memberToRemove, World world) {
		HiveSaveData saveData = (HiveSaveData) world.getMapStorage().getOrLoadData(HiveSaveData.class, HiveSaveData.ID);

		Hive owned = getHiveMembership(owner, world);
		Hive member = getHiveMembership(memberToRemove, world);

		if (!owned.equals(member)) return false;

		if (!owned.getMembers().contains(memberToRemove) && !owner.equals(memberToRemove)) return false;

		if (owner.equals(memberToRemove)) {
			saveData.changeOwner(owned);
		} else {
			saveData.removeMember(owned, memberToRemove);
		}

		saveData.markDirty();
		world.getMapStorage().saveAllData();
		return true;
	}

	public static boolean abandonNetwork (EntityPlayer player, World world) {
		Hive hive = getHiveMembership(player.getUniqueID(), world);
		return removeFromNetwork(hive.getOwner(), player.getUniqueID(), world);
	}

	public static boolean ejectPlayer (UUID owner, UUID caster, UUID eject, World world) {
		if (!owner.equals(caster)) return false;
		return removeFromNetwork(owner, eject, world);
	}

	public static NBTTagCompound getHiveMembershipInfo (Hive hive, UUID uuid) {
		NBTTagCompound result = new NBTTagCompound();
		result.setBoolean("is_owner", false);
		result.setBoolean("in_hive", false);

		if (hive == null) {
			return result;
		}

		if (hive.getOwner().equals(uuid)) {
			result.setBoolean("is_owner", true);
			result.setBoolean("in_hive", true);
		}

		if (hive.getMembers().contains(uuid)) {
			result.setBoolean("in_hive", true);
		}

		return result;
	}

	@Nullable
	public static Hive getHiveMembership (UUID uuid, World world) {
		if (!checkUUIDAndWorld(uuid, world)) return null;

		HiveSaveData saveData = (HiveSaveData) world.getMapStorage().getOrLoadData(HiveSaveData.class, HiveSaveData.ID);

		if (saveData == null) {
			saveData = new HiveSaveData();
			world.getMapStorage().setData(HiveSaveData.ID, saveData);
		}

		return saveData.getHiveByMember(uuid);
	}

	@Nullable
	public static HiveNetwork getHiveNetwork (UUID uuid, World world) {
		return getHiveNetwork(uuid, world, getHiveMembership(uuid, world));
	}

	@Nullable
	public static HiveNetwork getHiveNetwork (UUID uuid, World world, Hive hive) {
		// Can be null
		if (hive == null) return null;

		HiveNetwork potential = HIVE_MAP.get(hive.getOwner());
		if (potential == null || !potential.validate(hive)) {
			potential = createFromHive(hive, world);
			HIVE_MAP.put(hive.getOwner(), potential);
		}

		return potential;
	}

	// TODO: Implement
	public static HiveNetwork createFromHive (Hive hive, World world) {
		ServerNetwork owner = getServerNetwork(hive.getOwner(), world);
		List<ServerNetwork> members = new ArrayList<>();
		for (UUID member : hive.getMembers()) {
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

package com.aranaira.arcanearchives.data;

import com.aranaira.arcanearchives.ArcaneArchives;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NetworkHelper {
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
		if (uuid == null || uuid.equals(INVALID)) {
			ArcaneArchives.logger.warn(() -> "Attempted to fetch an invalid archive: " + uuid);
			ArcaneArchives.logger.warn("Trace:", new InvalidNetworkException("UUID must be valid"));
			// In an ideal situation, we won't need these checks, but it's useful for notifying about bugs with a
			// reduction in the chance of crashing the server/client
			return null;
		}
		if (world == null || world.getMapStorage() == null) {
			ArcaneArchives.logger.error(String.format("Attempted to load a network for %s, but the world or its storage is null!", uuid.toString()));
			ArcaneArchives.logger.warn("Trace:", new InvalidNetworkException("World is null!"));
			return null;
		}

		AAWorldSavedData saveData = (AAWorldSavedData) world.getMapStorage().getOrLoadData(AAWorldSavedData.class, AAWorldSavedData.ID);

		if (saveData == null) {
			saveData = new AAWorldSavedData();
			world.getMapStorage().setData(AAWorldSavedData.ID, saveData);
		}

		return saveData.getNetwork(uuid);
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

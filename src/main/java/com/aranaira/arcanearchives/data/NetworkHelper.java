package com.aranaira.arcanearchives.data;

import com.aranaira.arcanearchives.ArcaneArchives;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NetworkHelper
{
	// TODO: This needs to be cleared whenever the player enters a new world
	private static Map<UUID, AAClientNetwork> CLIENT_MAP = new HashMap<>();
	//private static AAWorldSavedData savedData = null;

	public static UUID INVALID = UUID.fromString("00000000-0000-0000-0000-000000000000");

	public static void clearClientCache()
	{
		CLIENT_MAP.clear();
	}

	@Nullable
	public static AAServerNetwork getServerNetwork(UUID uuid, World world)
	{
		if (uuid == null || uuid.equals(INVALID)) {
			ArcaneArchives.logger.warn(() -> "Attempted to fetch an invalid archive: " + uuid);
			ArcaneArchives.logger.info("Trace:", new Throwable());
			return null;
		}
		AAWorldSavedData savedData;
		if(world == null || world.getMapStorage() == null)
		{
			ArcaneArchives.logger.error(String.format("Attempted to load a network for %s, but the world or its storage is null!", uuid.toString()));
			return null;
		}

		AAWorldSavedData saveData = (AAWorldSavedData) world.getMapStorage().getOrLoadData(AAWorldSavedData.class, AAWorldSavedData.ID);

		if(saveData == null)
		{
			saveData = new AAWorldSavedData();
			world.getMapStorage().setData(AAWorldSavedData.ID, saveData);
		}

		savedData = saveData; //TODO: Cleanup redundant variable

		return savedData.getNetwork(uuid);
	}

	public static AAClientNetwork getClientNetwork(UUID uuid)
	{
		if (uuid == null || uuid.equals(INVALID)) return null;

		if(CLIENT_MAP.containsKey(uuid))
		{
			return CLIENT_MAP.get(uuid);
		} else
		{
			AAClientNetwork net = new AAClientNetwork(uuid);
			CLIENT_MAP.put(uuid, net);
			return net;
		}
	}
}

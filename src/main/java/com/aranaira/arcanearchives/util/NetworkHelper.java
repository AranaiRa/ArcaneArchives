package com.aranaira.arcanearchives.util;

import java.util.UUID;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.data.AAWorldSavedData;
import com.aranaira.arcanearchives.data.ArcaneArchivesNetwork;

import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class NetworkHelper 
{
	public static ArcaneArchivesNetwork getArcaneArchivesNetwork(UUID uuid)
	{
		World world = DimensionManager.getWorld(0);
		if (world == null || world.getMapStorage() == null)
		{
			return new AAWorldSavedData().getNetwork(uuid);
		}
		
		AAWorldSavedData saveData = (AAWorldSavedData) world.getMapStorage().getOrLoadData(AAWorldSavedData.class, AAWorldSavedData.ID);
		
		if (saveData == null)
		{
			saveData = new AAWorldSavedData();
			world.getMapStorage().setData(AAWorldSavedData.ID, saveData);
		}
		
		return saveData.getNetwork(uuid);
	}
	
	public static ArcaneArchivesNetwork getArcaneArchivesNetwork(String uuid)
	{
		return getArcaneArchivesNetwork(UUID.fromString(uuid));
	}
}

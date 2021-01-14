package com.aranaira.arcanearchives.api.data;

import net.minecraft.world.World;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.UUID;

public class DataStorage {
  @SuppressWarnings("ConstantConditions")
  public static ArcaneInventoryData getInventory (UUID id) {
    DimensionSavedDataManager manager = ServerLifecycleHooks.getCurrentServer().getWorld(World.OVERWORLD).getSavedData();
    return manager.getOrCreate(() -> new ArcaneInventoryData(id), ArcaneInventoryData.ID(id));
  }
}

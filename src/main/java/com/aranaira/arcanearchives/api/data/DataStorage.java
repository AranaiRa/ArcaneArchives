package com.aranaira.arcanearchives.api.data;

import com.aranaira.arcanearchives.api.inventory.IArcaneInventory;
import net.minecraft.world.World;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.UUID;
import java.util.function.Supplier;

public class DataStorage {
  @SuppressWarnings("ConstantConditions")
  public static <T extends IArcaneInventory<T>> ArcaneInventoryData getInventory (UUID id, Supplier<T> builder) {
    DimensionSavedDataManager manager = ServerLifecycleHooks.getCurrentServer().getWorld(World.OVERWORLD).getSavedData();
    return manager.getOrCreate(() -> new ArcaneInventoryData<>(id, builder), ArcaneInventoryData.ID(id));
  }
}

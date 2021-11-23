package com.aranaira.arcanearchives.api.data;

import com.aranaira.arcanearchives.api.inventory.IArcaneInventory;
import net.minecraft.world.World;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.UUID;
import java.util.function.Function;

@SuppressWarnings("ConstantConditions")
public class DataStorage {
  public static <T extends IArcaneInventory> ArcaneInventoryData<T> getInventory (UUID id, int size, Function<Integer, T> builder) {
    DimensionSavedDataManager manager = ServerLifecycleHooks.getCurrentServer().getLevel(World.OVERWORLD).getDataStorage();
    return manager.computeIfAbsent(() -> new ArcaneInventoryData<>(id, size, builder), ArcaneInventoryData.ID(id));
  }

  public static UUIDNameData.Name getNetworkName (UUID networkId) {
    DimensionSavedDataManager manager = ServerLifecycleHooks.getCurrentServer().getLevel(World.OVERWORLD).getDataStorage();
    UUIDNameData data = manager.computeIfAbsent(() -> new UUIDNameData(UUIDNameData.NETWORK_ID), UUIDNameData.NETWORK_ID);
    return data.getOrGenerate(networkId);
  }

  public static UUIDNameData.Name getTileName (UUID tileId) {
    DimensionSavedDataManager manager = ServerLifecycleHooks.getCurrentServer().getLevel(World.OVERWORLD).getDataStorage();
    UUIDNameData data = manager.computeIfAbsent(() -> new UUIDNameData(UUIDNameData.TILE_ID), UUIDNameData.NETWORK_ID);
    return data.getOrGenerate(tileId);
  }
}

package com.aranaira.arcanearchives.api.data;

import com.aranaira.arcanearchives.api.inventory.IArcaneInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
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

  public static final String SLOT_DATA = "ArcaneArchives-SlotData";
  public static final String OFFSET_DATA = "ArcaneArchives-OffsetData";

  public static int getSelectedSlot (UUID player, UUID tileId) {
    DimensionSavedDataManager manager = ServerLifecycleHooks.getCurrentServer().getLevel(World.OVERWORLD).getDataStorage();
    UUIDIntegerData data = manager.computeIfAbsent(() -> new UUIDIntegerData(SLOT_DATA), SLOT_DATA);
    return data.get(player, tileId);
  }

  public static void setSelectedSlot (UUID player, UUID tileId, int slot) {
    DimensionSavedDataManager manager = ServerLifecycleHooks.getCurrentServer().getLevel(World.OVERWORLD).getDataStorage();
    UUIDIntegerData data = manager.computeIfAbsent(() -> new UUIDIntegerData(SLOT_DATA), SLOT_DATA);
    data.set(player, tileId, slot);
    data.setDirty();
    manager.save();
  }

  public static int getOffset (UUID player, UUID tileId) {
    DimensionSavedDataManager manager = ServerLifecycleHooks.getCurrentServer().getLevel(World.OVERWORLD).getDataStorage();
    UUIDIntegerData data = manager.computeIfAbsent(() -> new UUIDIntegerData(OFFSET_DATA), OFFSET_DATA);
    return data.get(player, tileId);
  }

  public static void setOffset (UUID player, UUID tileId, int slot) {
    DimensionSavedDataManager manager = ServerLifecycleHooks.getCurrentServer().getLevel(World.OVERWORLD).getDataStorage();
    UUIDIntegerData data = manager.computeIfAbsent(() -> new UUIDIntegerData(OFFSET_DATA), OFFSET_DATA);
    data.set(player, tileId, slot);
    data.setDirty();
    manager.save();
  }
}

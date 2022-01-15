package com.aranaira.arcanearchives.api.data;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import noobanidus.libs.noobutil.data.server.StoredInventoryData;
import noobanidus.libs.noobutil.inventory.ILargeInventory;

import java.util.Map;
import java.util.UUID;
import java.util.function.IntFunction;

@SuppressWarnings("ConstantConditions")
public class DataStorage {
  public static <T extends ILargeInventory> StoredInventoryData<T> getInventory (UUID id, int size, IntFunction<T> builder) {
    DimensionSavedDataManager manager = ServerLifecycleHooks.getCurrentServer().getLevel(World.OVERWORLD).getDataStorage();
    return manager.computeIfAbsent(() -> new StoredInventoryData<>(id, size, builder), StoredInventoryData.ID(id));
  }

  public static UUIDNameData.Name getNetworkName (UUID networkId) {
    DimensionSavedDataManager manager = ServerLifecycleHooks.getCurrentServer().getLevel(World.OVERWORLD).getDataStorage();
    UUIDNameData data = manager.computeIfAbsent(() -> new UUIDNameData(UUIDNameData.NETWORK_ID), UUIDNameData.NETWORK_ID);
    return data.getOrGenerate(networkId);
  }

  public static Map<UUID, UUIDNameData.Name> getNetworkNames (UUID incomingId) {
    DimensionSavedDataManager manager = ServerLifecycleHooks.getCurrentServer().getLevel(World.OVERWORLD).getDataStorage();
    UUIDNameData data = manager.computeIfAbsent(() -> new UUIDNameData(UUIDNameData.NETWORK_ID), UUIDNameData.NETWORK_ID);
    data.getOrGenerate(incomingId);
    return data.getNames();
  }

  public static UUIDNameData.Name getTileName (UUID tileId) {
    DimensionSavedDataManager manager = ServerLifecycleHooks.getCurrentServer().getLevel(World.OVERWORLD).getDataStorage();
    UUIDNameData data = manager.computeIfAbsent(() -> new UUIDNameData(UUIDNameData.TILE_ID), UUIDNameData.NETWORK_ID);
    return data.getOrGenerate(tileId);
  }

  public static final String RECIPE_SELECTION = "ArcaneArchives-RecipeSelection";

  public static ResourceLocation getSelectedRecipe (UUID player, UUID tileId) {
    DimensionSavedDataManager manager = ServerLifecycleHooks.getCurrentServer().getLevel(World.OVERWORLD).getDataStorage();
    UUIDPlayerRLData data = manager.computeIfAbsent(() -> new UUIDPlayerRLData(RECIPE_SELECTION), RECIPE_SELECTION);
    return data.get(player, tileId);
  }

  public static void setSelectedRecipe (UUID player, UUID tileId, ResourceLocation rl) {
    DimensionSavedDataManager manager = ServerLifecycleHooks.getCurrentServer().getLevel(World.OVERWORLD).getDataStorage();
    UUIDPlayerRLData data = manager.computeIfAbsent(() -> new UUIDPlayerRLData(RECIPE_SELECTION), RECIPE_SELECTION);
    data.set(player, tileId, rl);
    data.setDirty();
    manager.save();
  }
}

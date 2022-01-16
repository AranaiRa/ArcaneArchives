package com.aranaira.arcanearchives.api.data;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import noobanidus.libs.noobutil.data.server.StoredInventoryData;
import noobanidus.libs.noobutil.inventory.ILargeInventory;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.UUID;
import java.util.function.IntFunction;

@SuppressWarnings("ConstantConditions")
public class DataStorage {
  public static <T extends ILargeInventory> StoredInventoryData<T> getInventory (UUID id, int size, IntFunction<T> builder) {
    DimensionSavedDataManager manager = ServerLifecycleHooks.getCurrentServer().getLevel(World.OVERWORLD).getDataStorage();
    return manager.computeIfAbsent(() -> new StoredInventoryData<>(id, size, builder), StoredInventoryData.ID(id));
  }

  public static UUIDNameData.Name getDomainName(UUID networkId) {
    DimensionSavedDataManager manager = ServerLifecycleHooks.getCurrentServer().getLevel(World.OVERWORLD).getDataStorage();
    UUIDNameData data = manager.computeIfAbsent(() -> new UUIDNameData(UUIDNameData.DOMAIN_ID), UUIDNameData.DOMAIN_ID);
    return data.getOrGenerate(networkId);
  }

  public static Map<UUID, UUIDNameData.Name> getDomainNames(UUID incomingId) {
    DimensionSavedDataManager manager = ServerLifecycleHooks.getCurrentServer().getLevel(World.OVERWORLD).getDataStorage();
    UUIDNameData data = manager.computeIfAbsent(() -> new UUIDNameData(UUIDNameData.DOMAIN_ID), UUIDNameData.DOMAIN_ID);
    data.getOrGenerate(incomingId);
    return data.getNames();
  }

  public static UUIDNameData.Name getEntityName(UUID entityId) {
    DimensionSavedDataManager manager = ServerLifecycleHooks.getCurrentServer().getLevel(World.OVERWORLD).getDataStorage();
    UUIDNameData data = manager.computeIfAbsent(() -> new UUIDNameData(UUIDNameData.ENLISTED_ID), UUIDNameData.DOMAIN_ID);
    return data.getOrGenerate(entityId);
  }

  public static final String RECIPE_SELECTION = "ArcaneArchives-RecipeSelection";

  public static ResourceLocation getSelectedRecipe (UUID player, UUID entityId) {
    DimensionSavedDataManager manager = ServerLifecycleHooks.getCurrentServer().getLevel(World.OVERWORLD).getDataStorage();
    UUIDPlayerRLData data = manager.computeIfAbsent(() -> new UUIDPlayerRLData(RECIPE_SELECTION), RECIPE_SELECTION);
    return data.get(player, entityId);
  }

  public static void setSelectedRecipe (UUID player, UUID entityId, ResourceLocation rl) {
    DimensionSavedDataManager manager = ServerLifecycleHooks.getCurrentServer().getLevel(World.OVERWORLD).getDataStorage();
    UUIDPlayerRLData data = manager.computeIfAbsent(() -> new UUIDPlayerRLData(RECIPE_SELECTION), RECIPE_SELECTION);
    data.set(player, entityId, rl);
    data.setDirty();
    manager.save();
  }
}

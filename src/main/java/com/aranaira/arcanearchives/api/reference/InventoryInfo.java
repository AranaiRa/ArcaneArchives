package com.aranaira.arcanearchives.api.reference;

import com.aranaira.arcanearchives.block.entity.CrystalWorkbenchBlockEntity;
import com.aranaira.arcanearchives.block.entity.DomainIdentifiedBlockEntity;
import com.aranaira.arcanearchives.block.entity.RadiantChestBlockEntity;
import com.aranaira.arcanearchives.inventory.handlers.CrystalWorkbenchInventory;
import com.aranaira.arcanearchives.inventory.handlers.RadiantChestInventory;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import noobanidus.libs.noobutil.recipe.AbstractLargeItemHandler;
import noobanidus.libs.noobutil.recipe.LargeItemHandler;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.IntFunction;

public class InventoryInfo {
  private static final Object2IntMap<Class<?>> sizeMap = new Object2IntOpenHashMap<>();
  private static final Map<Class<?>, IntFunction<? extends AbstractLargeItemHandler>> inventoryMap = new HashMap<>();

  static {
    sizeMap.defaultReturnValue(-1);
    sizeMap.put(CrystalWorkbenchBlockEntity.class, Constants.CrystalWorkbench.InventorySlots);
    inventoryMap.put(CrystalWorkbenchBlockEntity.class, CrystalWorkbenchInventory::new);
    sizeMap.put(RadiantChestBlockEntity.class, Constants.RadiantChest.InventorySlots);
    inventoryMap.put(RadiantChestBlockEntity.class, RadiantChestInventory::new);
  }

  public static int sizeFor (Class<?> clazz) {
    return sizeMap.getInt(clazz);
  }

  @Nullable
  public static IntFunction<? extends AbstractLargeItemHandler> inventoryBuilder (Class<?> clazz) {
    return inventoryMap.get(clazz);
  }
}

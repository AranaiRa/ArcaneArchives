package com.aranaira.arcanearchives.api.inventory;

import com.aranaira.arcanearchives.api.data.DataStorage;
import net.minecraft.item.ItemStack;
import noobanidus.libs.noobutil.data.server.StoredInventoryData;
import noobanidus.libs.noobutil.recipe.AbstractLargeItemHandler;
import noobanidus.libs.noobutil.tracking.ItemTracking;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.function.IntFunction;

public class RemoteInventory {
  protected final UUID inventoryId;
  protected final int size;
  protected final IntFunction<? extends AbstractLargeItemHandler> builder;
  protected AbstractLargeItemHandler inventory = null;
  protected boolean resolved = false;
  protected ItemTracking tracking = null;

  public RemoteInventory(UUID inventoryId, int size, IntFunction<? extends AbstractLargeItemHandler> builder) {
    this.inventoryId = inventoryId;
    this.size = size;
    this.builder = builder;
  }

  @Nullable
  public AbstractLargeItemHandler resolve() {
    if (inventory != null) {
      return inventory;
    }

    if (!resolved) {
      resolved = true;
      StoredInventoryData<? extends AbstractLargeItemHandler> data = DataStorage.getInventory(inventoryId, size, builder);
      inventory = data.getInventory();
      inventory.addListener((o, s) -> RemoteInventory.this.tracking = null);
    }

    return inventory;
  }

/*  public boolean addListener (IInventoryListener listener) {
    AbstractLargeItemHandler inv = resolve();
    if (inv != null) {
      inv.addListener(listener);
    }

    return inv != null;
  }*/

  public boolean hasTracking () {
    return tracking != null;
  }

  public ItemTracking getTracking() {
    AbstractLargeItemHandler inv = resolve();
    if (inv == null) {
      return null;
    }

    if (tracking == null) {
      tracking = new ItemTracking();

      for (int i = 0; i < inv.size(); i++) {
        ItemStack inSlot = inv.getStackInSlot(i);
        if (!inSlot.isEmpty()) {
          tracking.track(inSlot, inventoryId, i);
        }
      }
    }

    return tracking;
  }
}

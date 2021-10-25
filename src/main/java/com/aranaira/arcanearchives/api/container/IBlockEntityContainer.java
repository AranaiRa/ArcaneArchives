package com.aranaira.arcanearchives.api.container;

import com.aranaira.arcanearchives.api.inventory.IArcaneInventory;
import com.aranaira.arcanearchives.api.blockentities.IInventoryBlockEntity;

import javax.annotation.Nullable;

public interface IBlockEntityContainer<V extends IArcaneInventory, T extends IInventoryBlockEntity<V>> {
  @Nullable
  T getBlockEntity();

  @Nullable
  default V getBlockEntityInventory() {
    if (getBlockEntity() == null) {
      return null;
    }
    return getBlockEntity().getBlockEntityInventory();
  }

  @Nullable
  V getEmptyInventory ();
}

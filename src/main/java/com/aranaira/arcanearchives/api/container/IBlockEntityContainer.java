package com.aranaira.arcanearchives.api.container;

import com.aranaira.arcanearchives.api.inventory.IArcaneInventory;
import com.aranaira.arcanearchives.api.blockentities.IInventoryBlockEntity;

import javax.annotation.Nullable;

public interface IBlockEntityContainer<V extends IArcaneInventory, T extends IInventoryBlockEntity<V>> {
  @Nullable
  T getTile();

  @Nullable
  default V getBlockEntityInventory() {
    if (getTile() == null) {
      return null;
    }
    return getTile().getBlockEntityInventory();
  }

  @Nullable
  V getEmptyInventory ();
}

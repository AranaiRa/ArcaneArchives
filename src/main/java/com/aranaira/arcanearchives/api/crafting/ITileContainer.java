package com.aranaira.arcanearchives.api.crafting;

import com.aranaira.arcanearchives.api.inventory.IArcaneInventory;
import com.aranaira.arcanearchives.api.tiles.IInventoryTile;

import javax.annotation.Nullable;

public interface ITileContainer<V extends IArcaneInventory, T extends IInventoryTile<V>> {
  @Nullable
  T getTile();

  @Nullable
  default V getTileInventory() {
    if (getTile() == null) {
      return null;
    }
    return getTile().getTileInventory();
  }

  @Nullable
  default V getEmptyInventory () {
    if (getTile() == null) {
      return null;
    }
    return getTile().getEmptyInventory();
  }
}

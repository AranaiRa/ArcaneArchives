package com.aranaira.arcanearchives.api.crafting;

import com.aranaira.arcanearchives.api.inventory.IArcaneInventory;
import com.aranaira.arcanearchives.api.tiles.IInventoryTile;

public interface ITileContainer<V extends IArcaneInventory<V>, T extends IInventoryTile<V>> {
  T getTile();

  default V getTileInventory() {
    return getTile().getTileInventory();
  }
}

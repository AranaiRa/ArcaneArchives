package com.aranaira.arcanearchives.api.tiles;

import com.aranaira.arcanearchives.api.inventory.IArcaneInventory;

public interface IInventoryTile<T extends IArcaneInventory<T>> extends IArcaneArchivesTile {
  T getTileInventory();
}

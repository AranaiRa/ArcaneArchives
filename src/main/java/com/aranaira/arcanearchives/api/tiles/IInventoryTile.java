package com.aranaira.arcanearchives.api.tiles;

import com.aranaira.arcanearchives.api.inventory.IArcaneInventory;

import javax.annotation.Nullable;

public interface IInventoryTile<T extends IArcaneInventory> extends IArcaneArchivesTile {
  @Nullable
  T getTileInventory();

  T getEmptyInventory();
}

package com.aranaira.arcanearchives.tiles;

import com.aranaira.arcanearchives.api.tiles.IArcaneArchivesTile;
import com.aranaira.arcanearchives.api.tiles.IInventoryTile;
import com.aranaira.arcanearchives.inventory.RadiantChestInventory;

public class RadiantChestTile implements IInventoryTile<RadiantChestInventory> {
  private final RadiantChestInventory inventory = new RadiantChestInventory(54);

  @Override
  public RadiantChestInventory getTileInventory() {
    return inventory;
  }
}

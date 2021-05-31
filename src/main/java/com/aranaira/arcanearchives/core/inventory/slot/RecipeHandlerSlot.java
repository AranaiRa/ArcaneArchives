package com.aranaira.arcanearchives.core.inventory.slot;

import com.aranaira.arcanearchives.core.tiles.CrystalWorkbenchTile;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Slot;

public class RecipeHandlerSlot extends Slot {
  private static IInventory emptyInventory = new Inventory(0);
  private final CrystalWorkbenchTile tile;

  public RecipeHandlerSlot(int index, int xPosition, int yPosition, CrystalWorkbenchTile tile) {
    super(emptyInventory, index, xPosition, yPosition);
    this.tile = tile;
  }
}

package com.aranaira.arcanearchives.core.inventory.slot;

import com.aranaira.arcanearchives.core.blocks.entities.CrystalWorkbenchBlockEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Slot;

public class RecipeHandlerSlot extends Slot {
  private static IInventory emptyInventory = new Inventory(0);
  private final CrystalWorkbenchBlockEntity tile;

  public RecipeHandlerSlot(int index, int xPosition, int yPosition, CrystalWorkbenchBlockEntity tile) {
    super(emptyInventory, index, xPosition, yPosition);
    this.tile = tile;
  }
}

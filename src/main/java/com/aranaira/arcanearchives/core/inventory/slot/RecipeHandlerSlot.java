package com.aranaira.arcanearchives.core.inventory.slot;

import com.aranaira.arcanearchives.core.blocks.entities.CrystalWorkbenchBlockEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Slot;

public class RecipeHandlerSlot extends Slot {
  private static final IInventory recipeInventory = new Inventory(1);
  private final CrystalWorkbenchBlockEntity blockentity;

  public RecipeHandlerSlot(int index, int xPosition, int yPosition, CrystalWorkbenchBlockEntity blockentity) {
    super(recipeInventory, index, xPosition, yPosition);
    this.blockentity = blockentity;
  }
}

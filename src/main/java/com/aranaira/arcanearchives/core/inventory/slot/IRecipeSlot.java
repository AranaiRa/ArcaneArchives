package com.aranaira.arcanearchives.core.inventory.slot;

import com.aranaira.arcanearchives.core.recipes.CrystalWorkbenchRecipe;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.crafting.IRecipe;

import javax.annotation.Nullable;

public interface IRecipeSlot<T extends IRecipe<?>> {
  IInventory emptyInventory = new Inventory(0);

  void setOffset (int offsetTimes);

  int getOffset ();

  int getIndex ();

  static int getRecipeCount () {
    return 7;
  }

  default int getRecipeIndex () {
    return getRecipeCount() * getOffset() + getIndex();
  }

  @Nullable
  T getRecipe ();
}

package com.aranaira.arcanearchives.core.inventory.slot;

import com.aranaira.arcanearchives.core.recipes.CrystalWorkbenchRecipe;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public interface IRecipeSlot<T extends IRecipe<?>> {
  IInventory emptyInventory = new Inventory(0);

  void setOffset (int offsetTimes);

  int getOffset ();

  int getIndex ();

  @Nullable
  default ResourceLocation getRegistryName () {
    T recipe = getRecipe();
    if (recipe == null) {
      return null;
    }

    return recipe.getId();
  }

  static int getRecipeCount () {
    return 7;
  }

  default int getRecipeIndex () {
    return getRecipeCount() * getOffset() + getIndex();
  }

  @Nullable
  T getRecipe ();
}

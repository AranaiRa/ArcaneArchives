package com.aranaira.arcanearchives.api.inventory.slot;

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

  @Nullable
  T getRecipe ();
}

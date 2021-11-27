package com.aranaira.arcanearchives.api.inventory.slot;

import com.aranaira.arcanearchives.api.reference.Constants;
import net.minecraft.item.crafting.IRecipe;

public interface ICrystalWorkbenchRecipeSlot<T extends IRecipe<?>> extends IRecipeSlot<T> {
  static int getRecipeCount () {
    return Constants.CrystalWorkbench.RecipeSlots;
  }

  default int getRecipeIndex () {
    return getRecipeCount() * getOffset() + getIndex();
  }
}

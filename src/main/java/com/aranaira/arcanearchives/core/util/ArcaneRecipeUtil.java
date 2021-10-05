package com.aranaira.arcanearchives.core.util;

import com.aranaira.arcanearchives.api.crafting.ingredients.IngredientInfo;
import com.aranaira.arcanearchives.api.crafting.ingredients.IngredientStack;
import com.aranaira.arcanearchives.core.recipes.CrystalWorkbenchRecipe;
import com.aranaira.arcanearchives.core.recipes.inventory.CrystalWorkbenchCrafting;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArcaneRecipeUtil {
  /**
   * @param recipe The CrystalWorkbenchRecipe.
   * @param crafting The CrystalWorkbenchCrafting instance.
   * @return A list of IngredientInfos containing information about each ingredient and slot.
   */
  public static List<IngredientInfo> getIngredientInfo(CrystalWorkbenchRecipe recipe, CrystalWorkbenchCrafting crafting) {
    List<IngredientStack> ingredients = recipe.getIngredientStacks();
    List<IngredientInfo> result = new ArrayList<>();

    for (int i = 0; i < ingredients.size(); i++) {
      IngredientStack stack = ingredients.get(i);


    }

    return result;
  }
}

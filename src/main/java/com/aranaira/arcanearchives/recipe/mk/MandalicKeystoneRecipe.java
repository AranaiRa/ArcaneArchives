package com.aranaira.arcanearchives.recipe.mk;

import com.aranaira.arcanearchives.api.crafting.IngredientStack;

public class MandalicKeystoneRecipe {
  private IngredientStack input;
  private IngredientStack output;

  public MandalicKeystoneRecipe(IngredientStack input, IngredientStack output) {
    this.input = input;
    this.output = output;
  }

  public IngredientStack getRecipeOutput() {
    return output;
  }

  public IngredientStack getRecipeResult() {
    return output;
  }
}

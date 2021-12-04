package com.aranaira.arcanearchives.api.crafting.ingredients;

public class CollatedInfoPair {
  private final IngredientStack ingredient;
  private final IngredientInfo info;

  public CollatedInfoPair(IngredientStack ingredient, IngredientInfo info) {
    this.ingredient = ingredient;
    this.info = info;
  }

  public IngredientStack getIngredient() {
    return ingredient;
  }

  public IngredientInfo getInfo() {
    return info;
  }
}

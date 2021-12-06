package com.aranaira.arcanearchives.api.crafting.ingredients;

public class CountableIngredientStack extends IngredientStack {
  protected int suppliedAmount = 0;

  public CountableIngredientStack(IngredientStack stack) {
    super(stack.getIngredient(), stack.getCount());
  }

  /**
   * @param amount The amount incoming.
   * @return Left-over or 0 if the entire stack is consumed.
   */
  public int supply (int amount) {
    this.suppliedAmount += amount;

    if (suppliedAmount > getCount()) {
      int remainder = suppliedAmount - getCount();
      suppliedAmount = getCount();
      return remainder;
    }
    else if(suppliedAmount == getCount()) {
      return 0;
    }
    else
    {
      return getCount() - suppliedAmount;
    }
  }

  /**
   * @param amount The amount incoming.
   * @return The amount to subtract from this stack.
   */
  public int subtract (int amount) {
    int remainder = supply(amount);
    return amount - remainder;
  }

  public boolean filled () {
    return (getCount() - suppliedAmount) <= 0;
  }
}

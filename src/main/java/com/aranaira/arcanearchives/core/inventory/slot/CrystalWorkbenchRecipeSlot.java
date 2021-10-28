package com.aranaira.arcanearchives.core.inventory.slot;

import com.aranaira.arcanearchives.core.init.ResolvingRecipes;
import com.aranaira.arcanearchives.core.recipes.CrystalWorkbenchRecipe;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class CrystalWorkbenchRecipeSlot extends Slot implements IRecipeSlot<CrystalWorkbenchRecipe> {
  private final int index;
  private int offset;

  public CrystalWorkbenchRecipeSlot(int index, int xPosition, int yPosition) {
    super(IRecipeSlot.emptyInventory, index, xPosition, yPosition);
    this.index = index;
  }

  @Override
  public void setOffset(int offsetTimes) {
    this.offset = offsetTimes;
  }

  @Override
  public int getOffset() {
    return offset;
  }

  @Override
  public int getIndex() {
    return index;
  }

  @Nullable
  @Override
  public CrystalWorkbenchRecipe getRecipe() {
    int index = getRecipeIndex();
    if (ResolvingRecipes.CRYSTAL_WORKBENCH.hasRecipe(index)) {
      return ResolvingRecipes.CRYSTAL_WORKBENCH.getRecipe(index);
    }

    return null;
  }


  @Override
  public ItemStack getItem() {
    CrystalWorkbenchRecipe recipe = getRecipe();
    if (recipe == null) {
      return ItemStack.EMPTY;
    }

    return recipe.getResultItem();
  }

  @Override
  public boolean hasItem() {
    return ResolvingRecipes.CRYSTAL_WORKBENCH.hasRecipe(getRecipeIndex());
  }

  @Override
  public boolean mayPickup(PlayerEntity p_82869_1_) {
    return false;
  }
}

package com.aranaira.arcanearchives.inventory.slot;

import com.aranaira.arcanearchives.api.inventory.slot.ICrystalWorkbenchRecipeSlot;
import com.aranaira.arcanearchives.api.inventory.slot.IRecipeSlot;
import com.aranaira.arcanearchives.init.ResolvedRecipes;
import com.aranaira.arcanearchives.recipe.CrystalWorkbenchRecipe;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class ClientCrystalWorkbenchRecipeRecipeSlot extends Slot implements ICrystalWorkbenchRecipeSlot<CrystalWorkbenchRecipe> {
  private int offset;
  private final int index;
  private boolean dimmed = false;

  public ClientCrystalWorkbenchRecipeRecipeSlot(int index, int xPosition, int yPosition) {
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

  @Override
  public boolean isDimmed() {
    return dimmed;
  }

  @Override
  public void setDimmed(boolean dimmed) {
    this.dimmed = dimmed;
  }

  @Override
  public Slot getSlot() {
    return this;
  }

  @Nullable
  @Override
  public CrystalWorkbenchRecipe getRecipe() {
    int index = getRecipeIndex();
    if (ResolvedRecipes.CRYSTAL_WORKBENCH.hasRecipe(index)) {
      return ResolvedRecipes.CRYSTAL_WORKBENCH.getRecipe(index);
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
    return ResolvedRecipes.CRYSTAL_WORKBENCH.hasRecipe(getRecipeIndex());
  }

  @Override
  public boolean mayPickup(PlayerEntity p_82869_1_) {
    return false;
  }
}

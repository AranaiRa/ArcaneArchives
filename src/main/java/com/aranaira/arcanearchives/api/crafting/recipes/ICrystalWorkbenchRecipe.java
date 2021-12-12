package com.aranaira.arcanearchives.api.crafting.recipes;

import com.aranaira.arcanearchives.api.blockentities.IIdentifiedBlockEntity;
import com.aranaira.arcanearchives.api.crafting.ingredients.IngredientInfo;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import noobanidus.libs.noobutil.container.IPartitionedPlayerContainer;
import noobanidus.libs.noobutil.crafting.Crafting;
import noobanidus.libs.noobutil.ingredient.IngredientStack;
import noobanidus.libs.noobutil.recipe.ILargeRecipe;
import noobanidus.libs.noobutil.recipe.LargeItemHandler;

import java.util.List;

public interface ICrystalWorkbenchRecipe<H extends LargeItemHandler, C extends Container & IPartitionedPlayerContainer, T extends TileEntity & IIdentifiedBlockEntity, W extends Crafting<H, C, T>> extends ILargeRecipe<H, C, T, W> {
  NonNullList<IngredientStack> getIngredientStacks();

  List<IngredientInfo> getIngredientInfo(W crafting);

  @Override
  default boolean canCraftInDimensions(int width, int height) {
    return true;
  }
}

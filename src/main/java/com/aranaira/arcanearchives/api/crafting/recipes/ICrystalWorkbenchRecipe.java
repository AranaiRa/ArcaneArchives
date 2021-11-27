package com.aranaira.arcanearchives.api.crafting.recipes;

import com.aranaira.arcanearchives.api.blockentities.IIdentifiedBlockEntity;
import com.aranaira.arcanearchives.api.container.IPartitionedPlayerContainer;
import com.aranaira.arcanearchives.api.crafting.ArcaneCrafting;
import com.aranaira.arcanearchives.api.crafting.ingredients.IngredientInfo;
import com.aranaira.arcanearchives.api.crafting.ingredients.IngredientStack;
import com.aranaira.arcanearchives.api.inventory.ArcaneItemHandler;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;

import java.util.List;

public interface ICrystalWorkbenchRecipe<H extends ArcaneItemHandler, C extends Container & IPartitionedPlayerContainer, T extends TileEntity & IIdentifiedBlockEntity, W extends ArcaneCrafting<H, C, T>> extends IArcaneRecipe<H, C, T, W> {
  NonNullList<IngredientStack> getIngredientStacks();

  List<IngredientInfo> getIngredientInfo(W crafting);

  @Override
  default boolean canCraftInDimensions(int width, int height) {
    return true;
  }
}

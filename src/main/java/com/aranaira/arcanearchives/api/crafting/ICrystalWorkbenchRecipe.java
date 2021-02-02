package com.aranaira.arcanearchives.api.crafting;

import com.aranaira.arcanearchives.api.IArcaneArchivesTile;
import com.aranaira.arcanearchives.api.crafting.processors.IIngredientProcessor;
import com.aranaira.arcanearchives.api.inventory.ArcaneItemHandler;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;

import java.util.List;

public interface ICrystalWorkbenchRecipe<C extends Container & IPlayerContainer, T extends TileEntity & IArcaneArchivesTile> extends IRecipe<WorkbenchCrafting<ArcaneItemHandler, C, T>> {
  List<IIngredientProcessor> getProcessors ();

  NonNullList<IngredientStack> getIngredientStacks();

  List<IngredientInfo> getIngredientInfo (C container);

  @Override
  default boolean canFit(int width, int height) {
    return true;
  }
}

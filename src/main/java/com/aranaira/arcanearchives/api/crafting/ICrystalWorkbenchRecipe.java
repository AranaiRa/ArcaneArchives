package com.aranaira.arcanearchives.api.crafting;

import com.aranaira.arcanearchives.api.container.IPlayerContainer;
import com.aranaira.arcanearchives.api.crafting.processors.IIngredientProcessor;
import com.aranaira.arcanearchives.api.inventory.ArcaneItemHandler;
import com.aranaira.arcanearchives.api.tiles.IArcaneArchivesTile;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.crafting.IRecipe;
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

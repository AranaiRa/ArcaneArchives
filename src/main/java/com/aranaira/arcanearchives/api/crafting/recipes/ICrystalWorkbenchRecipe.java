package com.aranaira.arcanearchives.api.crafting.recipes;

import com.aranaira.arcanearchives.api.container.IPlayerContainer;
import com.aranaira.arcanearchives.api.crafting.ingredients.IngredientInfo;
import com.aranaira.arcanearchives.api.crafting.ingredients.IngredientStack;
import com.aranaira.arcanearchives.api.crafting.WorkbenchCrafting;
import com.aranaira.arcanearchives.api.crafting.processors.Processor;
import com.aranaira.arcanearchives.api.inventory.ArcaneItemHandler;
import com.aranaira.arcanearchives.api.tiles.IArcaneArchivesTile;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public interface ICrystalWorkbenchRecipe<H extends ArcaneItemHandler, C extends Container & IPlayerContainer, T extends TileEntity & IArcaneArchivesTile> extends IRecipe<WorkbenchCrafting<H, C, T>> {
  List<Processor<H, C, T>> getProcessors ();

  void addProcessor (Processor<H, C, T> processor);

  List<Processor<H, C, T>> getDefaultProcessors ();

  void removeDefaultProcessor (ResourceLocation rl);

  void removeDefaultProcessor (Processor<H, C, T> processor);

  NonNullList<IngredientStack> getIngredientStacks();

  List<IngredientInfo> getIngredientInfo (C container);

  @Override
  default boolean canFit(int width, int height) {
    return true;
  }
}

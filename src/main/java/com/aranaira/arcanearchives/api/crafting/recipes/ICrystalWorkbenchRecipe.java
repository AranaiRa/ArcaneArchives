package com.aranaira.arcanearchives.api.crafting.recipes;

import com.aranaira.arcanearchives.api.container.IPlayerContainer;
import com.aranaira.arcanearchives.api.crafting.ICrafter;
import com.aranaira.arcanearchives.api.crafting.ingredients.IngredientInfo;
import com.aranaira.arcanearchives.api.crafting.ingredients.IngredientStack;
import com.aranaira.arcanearchives.api.crafting.ArcaneCrafting;
import com.aranaira.arcanearchives.api.crafting.processors.Processor;
import com.aranaira.arcanearchives.api.inventory.ArcaneItemHandler;
import com.aranaira.arcanearchives.api.tiles.IArcaneArchivesTile;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.List;

public interface ICrystalWorkbenchRecipe<H extends ArcaneItemHandler, C extends Container & IPlayerContainer, T extends TileEntity & IArcaneArchivesTile, W extends ArcaneCrafting<H, C, T>> extends IRecipe<W> {
  List<Processor<W>> getProcessors ();

  void addProcessor (Processor<W> processor);

  void skipDefaultProcessor (ResourceLocation rl);

  void skipDefaultProcessor (Processor<W> processor);

  boolean shouldSkipProcessor (Processor<W> processor);

  NonNullList<IngredientStack> getIngredientStacks();

  List<IngredientInfo> getIngredientInfo (C container);

  @Override
  default boolean canFit(int width, int height) {
    return true;
  }
}

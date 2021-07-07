package com.aranaira.arcanearchives.core.recipes;

import com.aranaira.arcanearchives.api.crafting.WorkbenchCrafting;
import com.aranaira.arcanearchives.api.crafting.ingredients.IngredientInfo;
import com.aranaira.arcanearchives.api.crafting.ingredients.IngredientStack;
import com.aranaira.arcanearchives.api.crafting.processors.IngredientProcessor;
import com.aranaira.arcanearchives.api.crafting.recipes.ICrystalWorkbenchRecipe;
import com.aranaira.arcanearchives.core.inventory.container.CrystalWorkbenchContainer;
import com.aranaira.arcanearchives.core.inventory.handlers.CrystalWorkbenchInventory;
import com.aranaira.arcanearchives.core.tiles.CrystalWorkbenchTile;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.List;

public class CrystalWorkbenchRecipe implements ICrystalWorkbenchRecipe<CrystalWorkbenchInventory, CrystalWorkbenchContainer, CrystalWorkbenchTile> {

  @Override
  public List<IngredientProcessor<CrystalWorkbenchInventory, CrystalWorkbenchContainer, CrystalWorkbenchTile>> getProcessors() {
    return null;
  }

  @Override
  public NonNullList<IngredientStack> getIngredientStacks() {
    return null;
  }

  @Override
  public List<IngredientInfo> getIngredientInfo(CrystalWorkbenchContainer container) {
    return null;
  }

  @Override
  public boolean matches(WorkbenchCrafting<CrystalWorkbenchInventory, CrystalWorkbenchContainer, CrystalWorkbenchTile> inv, World worldIn) {
    return false;
  }

  @Override
  public ItemStack getCraftingResult(WorkbenchCrafting<CrystalWorkbenchInventory, CrystalWorkbenchContainer, CrystalWorkbenchTile> inv) {
    return null;
  }

  @Override
  public ItemStack getRecipeOutput() {
    return null;
  }

  @Override
  public ResourceLocation getId() {
    return null;
  }

  @Override
  public IRecipeSerializer<?> getSerializer() {
    return null;
  }

  @Override
  public IRecipeType<?> getType() {
    return null;
  }
}

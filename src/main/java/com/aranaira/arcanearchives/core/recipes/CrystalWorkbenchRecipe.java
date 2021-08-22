package com.aranaira.arcanearchives.core.recipes;

import com.aranaira.arcanearchives.api.crafting.ingredients.IngredientInfo;
import com.aranaira.arcanearchives.api.crafting.ingredients.IngredientStack;
import com.aranaira.arcanearchives.api.crafting.processors.IProcessor;
import com.aranaira.arcanearchives.api.crafting.processors.Processor;
import com.aranaira.arcanearchives.api.crafting.recipes.ICrystalWorkbenchRecipe;
import com.aranaira.arcanearchives.core.init.ModRegistries;
import com.aranaira.arcanearchives.core.inventory.container.CrystalWorkbenchContainer;
import com.aranaira.arcanearchives.core.inventory.handlers.CrystalWorkbenchInventory;
import com.aranaira.arcanearchives.core.recipes.inventory.CrystalWorkbenchCrafting;
import com.aranaira.arcanearchives.core.tiles.CrystalWorkbenchTile;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CrystalWorkbenchRecipe implements ICrystalWorkbenchRecipe<CrystalWorkbenchInventory, CrystalWorkbenchContainer, CrystalWorkbenchTile, CrystalWorkbenchCrafting> {
  private final List<Processor<CrystalWorkbenchCrafting>> processors = new ArrayList<>();
  private final Set<IProcessor<?>> skips = new HashSet<>();
  private final NonNullList<IngredientStack> ingredientStacks = NonNullList.from(IngredientStack.EMPTY);

  @Override
  public List<Processor<CrystalWorkbenchCrafting>> getProcessors() {
    return processors;
  }

  @Override
  public void addProcessor(Processor<CrystalWorkbenchCrafting> processor) {
    processors.add(processor);
  }

  @Override
  public void skipDefaultProcessor(ResourceLocation rl) {
    IProcessor<?> processor = ModRegistries.PROCESSOR_REGISTRY.getValue(rl);
    if (processor == null) {
      throw new NullPointerException("Invalid processor: " + rl);
    }

    skips.add(processor);
  }

  @Override
  public void skipDefaultProcessor(Processor<CrystalWorkbenchCrafting> processor) {
    skips.add(processor);
  }

  @Override
  public boolean shouldSkipProcessor(Processor<CrystalWorkbenchCrafting> processor) {
    return skips.contains(processor);
  }

  @Override
  public NonNullList<IngredientStack> getIngredientStacks() {
    return ingredientStacks;
  }

  @Override
  public List<IngredientInfo> getIngredientInfo(CrystalWorkbenchContainer container) {
    return null;
  }

  @Override
  public boolean matches(CrystalWorkbenchCrafting inv, World worldIn) {
    return false;
  }

  @Override
  public ItemStack getCraftingResult(CrystalWorkbenchCrafting inv) {
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

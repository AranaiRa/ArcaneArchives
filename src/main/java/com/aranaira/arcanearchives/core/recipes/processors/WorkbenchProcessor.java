package com.aranaira.arcanearchives.core.recipes.processors;

import com.aranaira.arcanearchives.api.crafting.WorkbenchCrafting;
import com.aranaira.arcanearchives.api.crafting.processors.IngredientProcessor;
import com.aranaira.arcanearchives.core.inventory.container.CrystalWorkbenchContainer;
import com.aranaira.arcanearchives.core.inventory.handlers.CrystalWorkbenchInventory;
import com.aranaira.arcanearchives.core.tiles.CrystalWorkbenchTile;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class WorkbenchProcessor extends IngredientProcessor<CrystalWorkbenchInventory, CrystalWorkbenchContainer, CrystalWorkbenchTile> {
  public WorkbenchProcessor(ResourceLocation registry, ResourceLocation resourceLocation) {
    super(registry, resourceLocation);
  }

  @Override
  public List<ItemStack> process(Ingredient ingredient, ItemStack incoming, WorkbenchCrafting<CrystalWorkbenchInventory, CrystalWorkbenchContainer, CrystalWorkbenchTile> workbench) {
    return null;
  }
}

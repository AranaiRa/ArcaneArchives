package com.aranaira.arcanearchives.core.recipes.processors;

import com.aranaira.arcanearchives.api.crafting.ICrafter;
import com.aranaira.arcanearchives.api.crafting.processors.IngredientProcessor;
import com.aranaira.arcanearchives.api.crafting.processors.Processor;
import com.aranaira.arcanearchives.core.inventory.container.CrystalWorkbenchContainer;
import com.aranaira.arcanearchives.core.inventory.handlers.CrystalWorkbenchInventory;
import com.aranaira.arcanearchives.core.tiles.CrystalWorkbenchTile;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public abstract class WorkbenchIngredientProcessor extends IngredientProcessor<CrystalWorkbenchInventory, CrystalWorkbenchContainer, CrystalWorkbenchTile> {
  public WorkbenchIngredientProcessor(ResourceLocation registry, ResourceLocation resourceLocation) {
    super(registry, resourceLocation);
  }
}

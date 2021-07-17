package com.aranaira.arcanearchives.core.recipes.processors;

import com.aranaira.arcanearchives.api.crafting.ICrafter;
import com.aranaira.arcanearchives.core.init.Registries;
import com.aranaira.arcanearchives.core.inventory.container.CrystalWorkbenchContainer;
import com.aranaira.arcanearchives.core.inventory.handlers.CrystalWorkbenchInventory;
import com.aranaira.arcanearchives.core.tiles.CrystalWorkbenchTile;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class WorkbenchContainerProcessor extends WorkbenchIngredientProcessor {
  public WorkbenchContainerProcessor(ResourceLocation resourceLocation) {
    super(Registries.Processor.Input.WORKBENCH_ID, resourceLocation);
  }

  @Override
  public ItemStack process(ItemStack input, Ingredient ingredient, ItemStack incoming, List<ItemStack> processed, ICrafter<CrystalWorkbenchInventory, CrystalWorkbenchContainer, CrystalWorkbenchTile> workbench) {
    if (incoming.hasContainerItem()) {
      for (int i = 0; i < incoming.getCount(); i++) {
        processed.add(incoming.getContainerItem());
      }
    }

    return input;
  }
}

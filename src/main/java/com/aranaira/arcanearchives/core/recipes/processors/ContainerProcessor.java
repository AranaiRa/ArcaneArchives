package com.aranaira.arcanearchives.core.recipes.processors;

import com.aranaira.arcanearchives.api.crafting.WorkbenchCrafting;
import com.aranaira.arcanearchives.core.inventory.container.CrystalWorkbenchContainer;
import com.aranaira.arcanearchives.core.inventory.handlers.CrystalWorkbenchInventory;
import com.aranaira.arcanearchives.core.init.Registries;
import com.aranaira.arcanearchives.core.tiles.CrystalWorkbenchTile;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class ContainerProcessor extends WorkbenchProcessor {
  public ContainerProcessor(ResourceLocation resourceLocation) {
    super(Registries.Processor.WORKBENCH_ID, resourceLocation);
  }

  @Override
  public List<ItemStack> process(Ingredient ingredient, ItemStack incoming, WorkbenchCrafting<CrystalWorkbenchInventory, CrystalWorkbenchContainer, CrystalWorkbenchTile> workbench) {
    List<ItemStack> result = new ArrayList<>();
    if (incoming.hasContainerItem()) {
      for (int i = 0; i < incoming.getCount(); i++) {
        result.add(incoming.getContainerItem());
      }
    }

    // TODO: Handle things that are hardcoded instead of using container items

    return result;
  }
}

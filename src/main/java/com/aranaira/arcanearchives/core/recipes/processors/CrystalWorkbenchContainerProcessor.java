package com.aranaira.arcanearchives.core.recipes.processors;

import com.aranaira.arcanearchives.api.crafting.processors.Processor;
import com.aranaira.arcanearchives.core.recipes.inventory.CrystalWorkbenchCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class CrystalWorkbenchContainerProcessor extends CrystalWorkbenchProcessor {
  public CrystalWorkbenchContainerProcessor() {
    super();
  }

  @Override
  public List<ItemStack> processIngredient(ItemStack result, Ingredient ingredient, ItemStack usedItem, CrystalWorkbenchCrafting crafter) {
    List<ItemStack> resultList = new ArrayList<>();
    if (usedItem.hasContainerItem()) {
      for (int i = 0; i < usedItem.getCount(); i++) {
        resultList.add(usedItem.getContainerItem());
      }
    }
    return resultList;
  }
}

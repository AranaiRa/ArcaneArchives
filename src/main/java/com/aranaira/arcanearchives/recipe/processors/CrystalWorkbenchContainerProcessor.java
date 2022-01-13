package com.aranaira.arcanearchives.recipe.processors;

import com.aranaira.arcanearchives.recipe.inventory.CrystalWorkbenchCrafting;
import net.minecraft.item.ItemStack;
import noobanidus.libs.noobutil.ingredient.IngredientStack;

import java.util.ArrayList;
import java.util.List;

public class CrystalWorkbenchContainerProcessor extends CrystalWorkbenchProcessor {
  public CrystalWorkbenchContainerProcessor() {
    super();
  }

  @Override
  public List<ItemStack> processIngredient(ItemStack result, IngredientStack ingredient, ItemStack usedItem, CrystalWorkbenchCrafting crafter) {
    List<ItemStack> resultList = new ArrayList<>();
    if (usedItem.hasContainerItem()) {
      for (int i = 0; i < usedItem.getCount(); i++) {
        resultList.add(usedItem.getContainerItem());
      }
    }
    return resultList;
  }
}

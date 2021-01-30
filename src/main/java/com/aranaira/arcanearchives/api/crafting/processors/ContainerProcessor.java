package com.aranaira.arcanearchives.api.crafting.processors;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class ContainerProcessor implements IIngredientProcessor {
  @Override
  public List<ItemStack> process(Ingredient ingredient, ItemStack incoming) {
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

package com.aranaira.arcanearchives.api.crafting.processors;

import com.aranaira.arcanearchives.api.crafting.ArcaneCrafting;
import com.aranaira.arcanearchives.api.crafting.ICrafter;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface IProcessor<T extends ArcaneCrafting<?, ?, ?>> extends IForgeRegistryEntry<IProcessor<?>> {

  default List<List<ItemStack>> process(ItemStack incomingResult, List<Ingredient> ingredients, List<ItemStack> usedItems, T crafter) {
    if (ingredients.size() != usedItems.size()) {
      throw new IllegalArgumentException("size of `ingredients` doesn't match `usedItems`: " + ingredients.size() + " ingredients vs " + usedItems.size() + " items");
    }

    List<List<ItemStack>> processing = new ArrayList<>();
    processing.add(Collections.singletonList(processOutput(incomingResult, ingredients, usedItems, crafter)));
    for (int i = 0; i < ingredients.size(); i++) {
      processing.add(processIngredient(incomingResult, ingredients.get(i), usedItems.get(i), crafter));
    }

    return processing;
  }

  default ItemStack processOutput(ItemStack result, List<Ingredient> ingredients, List<ItemStack> usedItems, T crafter) {
    return result;
  }

  default List<ItemStack> processIngredient(ItemStack result, Ingredient ingredient, ItemStack usedItem, T crafter) {
    return Collections.singletonList(ItemStack.EMPTY);
  }
}

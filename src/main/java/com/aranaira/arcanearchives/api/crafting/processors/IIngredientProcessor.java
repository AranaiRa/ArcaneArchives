package com.aranaira.arcanearchives.api.crafting.processors;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.List;

public interface IIngredientProcessor {
  List<ItemStack> process (Ingredient ingredient, ItemStack incoming);
}

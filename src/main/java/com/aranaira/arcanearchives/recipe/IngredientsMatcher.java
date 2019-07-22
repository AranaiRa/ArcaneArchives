package com.aranaira.arcanearchives.recipe;

import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.List;

public class IngredientsMatcher {
	private final Object2IntOpenHashMap<Ingredient> counts = new Object2IntOpenHashMap<>();
	private final List<IngredientStack> ingredients;

	public IngredientsMatcher (List<IngredientStack> ingredients) {
		this.ingredients = ingredients;
		rebuildCounts();
	}

	public void rebuildCounts () {
		for (IngredientStack ingredient : ingredients) {
			int count = ingredient.getCount();
			Ingredient ing = ingredient.getIngredient();
			counts.put(ing, count);
		}
	}

	public boolean matches (@Nonnull IItemHandler inv) {
		for (int i = 0; i < inv.getSlots(); ++i) {
			ItemStack itemstack = inv.getStackInSlot(i);
			if (!itemstack.isEmpty()) {
				for (IngredientStack ingredient : ingredients) {
					if (ingredient.apply(itemstack)) {
						account(ingredient.getIngredient(), itemstack);
					}
				}
			}
		}

		return counts.isEmpty();
	}

	public void account (Ingredient ingredient, ItemStack stack) {
		if (counts.containsKey(ingredient)) {
			int curTotal = counts.getInt(ingredient);
			if (curTotal - stack.getCount() <= 0) {
				counts.remove(ingredient, curTotal);
			} else {
				counts.put(ingredient, curTotal - stack.getCount());
			}
		}
	}

	public Int2IntMap getMatchingSlots (@Nonnull IItemHandler inv) {
		rebuildCounts();
		Int2IntMap matchingSlots = new Int2IntOpenHashMap();

		for (int slot = 0; slot < inv.getSlots(); ++slot) {
			ItemStack itemstack = inv.getStackInSlot(slot);
			if (!itemstack.isEmpty()) {
				for (IngredientStack ingredient : ingredients) {
					if (ingredient.apply(itemstack)) {
						int discount = discount(ingredient.getIngredient(), itemstack);
						if (discount != -1) {
							matchingSlots.put(slot, discount);
						}
					}
				}
			}
		}

		return matchingSlots;
	}

	public int discount (Ingredient ingredient, ItemStack stack) {
		int res = -1;
		if (counts.containsKey(ingredient)) {
			int thisAmount = stack.getCount();
			int neededAmount = counts.getInt(ingredient);
			if (thisAmount >= neededAmount) {
				res = neededAmount;
				counts.remove(ingredient, neededAmount);
			} else {
				counts.put(ingredient, neededAmount - thisAmount);
				res = thisAmount;
			}
		}
		return res;
	}
}

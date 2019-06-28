package com.aranaira.arcanearchives.integration.jei.gct;

import com.aranaira.arcanearchives.recipe.gct.GCTRecipe;
import com.aranaira.arcanearchives.util.types.IngredientStack;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GCTWrapper implements IRecipeWrapper {
	public GCTRecipe recipe;

	public GCTWrapper (GCTRecipe recipe) {
		this.recipe = recipe;
	}

	@Override
	public void getIngredients (IIngredients ingredients) {
		if (recipe != null) {
			List<List<ItemStack>> lists = new ArrayList<>();
			for (IngredientStack stack : recipe.getIngredients()) {
				List<ItemStack> stacks = Stream.of(stack.getMatchingStacks()).map(ItemStack::copy).collect(Collectors.toList());
				for (ItemStack s : stacks) {
					s.setCount(stack.getCount());
				}
				lists.add(stacks);
			}
			ingredients.setInputLists(VanillaTypes.ITEM, lists);
			ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
		}
	}
}

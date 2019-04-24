package com.aranaira.arcanearchives.integration.jei.gct;

import com.aranaira.arcanearchives.recipe.gct.GCTRecipe;
import com.aranaira.arcanearchives.util.types.IngredientStack;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;

import java.util.Arrays;

public class GCTWrapper implements IRecipeWrapper
{
	public GCTRecipe recipe;

	public GCTWrapper(GCTRecipe recipe) {
		this.recipe = recipe;
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		if (recipe != null) {
			for (IngredientStack stack : recipe.getIngredients()) {
				ingredients.setInputs(VanillaTypes.ITEM, Arrays.asList(stack.getMatchingStacks()));
			}
			ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
		}
	}
}

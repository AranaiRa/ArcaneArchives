package com.aranaira.arcanearchives.integration.guidebook;

import com.aranaira.arcanearchives.recipe.gct.GCTRecipe;
import com.aranaira.arcanearchives.recipe.gct.GCTRecipeList;
import gigaherz.lirelent.guidebook.guidebook.recipe.RecipeProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GCTRecipeProvider extends RecipeProvider {
	@Nullable
	@Override
	public ProvidedComponents provideRecipeComponents (@Nonnull ItemStack targetOutput, int recipeIndex) {
		return null;
	}

	@Nullable
	@Override
	public ProvidedComponents provideRecipeComponents (@Nonnull ResourceLocation recipeKey) {
		return null;
	}
}

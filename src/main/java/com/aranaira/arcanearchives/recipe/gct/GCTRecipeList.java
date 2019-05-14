package com.aranaira.arcanearchives.recipe.gct;

import com.google.common.collect.ImmutableList;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class GCTRecipeList {
	private static final List<GCTRecipe> RECIPE_LIST = new ArrayList<>();
	private static ImmutableList<GCTRecipe> IMMUTABLE_COPY = null;

	public static List<GCTRecipe> getRecipeList () {
		if (IMMUTABLE_COPY == null) {
			IMMUTABLE_COPY = ImmutableList.copyOf(RECIPE_LIST);
		}

		return IMMUTABLE_COPY;
	}

	public static GCTRecipe makeAndAddRecipe (String name, @Nonnull ItemStack result, Object... recipe) {
		GCTRecipe newRecipe = new GCTRecipe(name, result, recipe);
		addRecipe(newRecipe);
		return newRecipe;
	}

	public static void addRecipe (GCTRecipe recipe) {
		IMMUTABLE_COPY = null;
		RECIPE_LIST.add(recipe);
	}

	public static void removeRecipe (GCTRecipe recipe) {
		if (RECIPE_LIST.contains(recipe)) {
			IMMUTABLE_COPY = null;
			RECIPE_LIST.remove(recipe);
		}
	}

	public static ItemStack getOutputByIndex (int index) {
		if (index < 0 || index >= RECIPE_LIST.size()) {
			return ItemStack.EMPTY;
		}

		return RECIPE_LIST.get(index).getRecipeOutput().copy();
	}

	public static GCTRecipe getRecipeByIndex (int index) {
		if (index < 0 || index >= RECIPE_LIST.size()) {
			return null;
		}

		return RECIPE_LIST.get(index);
	}

	public static int indexOf (GCTRecipe recipe) {
		if (recipe == null || !RECIPE_LIST.contains(recipe)) {
			return -1;
		}

		return RECIPE_LIST.indexOf(recipe);
	}

	public static int getSize () {
		return RECIPE_LIST.size();
	}
}

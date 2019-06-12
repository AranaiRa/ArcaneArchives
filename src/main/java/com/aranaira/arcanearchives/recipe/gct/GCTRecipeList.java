package com.aranaira.arcanearchives.recipe.gct;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.google.common.collect.ImmutableList;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class GCTRecipeList {
	private static final LinkedHashMap<ResourceLocation, GCTRecipe> RECIPE_LIST = new LinkedHashMap<>();
	private static ImmutableList<GCTRecipe> IMMUTABLE_COPY = null;

	public static List<GCTRecipe> getRecipeList () {
		if (IMMUTABLE_COPY == null) {
			IMMUTABLE_COPY = ImmutableList.copyOf(RECIPE_LIST.values());
		}

		return IMMUTABLE_COPY;
	}

	public static GCTRecipe makeAndAddRecipe (String name, @Nonnull ItemStack result, Object... recipe) {
		GCTRecipe newRecipe = new GCTRecipe(name, result, recipe);
		addRecipe(newRecipe);
		return newRecipe;
	}

	public static GCTRecipeWithConditionsCrafter makeAndAddRecipeWithCreatorAndCondition (String name, @Nonnull ItemStack result, Object ... recipe) {
		GCTRecipeWithConditionsCrafter newRecipe = new GCTRecipeWithConditionsCrafter(name, result, recipe);
		addRecipe(newRecipe);
		return newRecipe;
	}

	public static void addRecipe (GCTRecipe recipe) {
		IMMUTABLE_COPY = null;
		RECIPE_LIST.put(recipe.getName(), recipe);
	}

	@Nullable
	public static GCTRecipe getRecipe (ResourceLocation name) {
		return RECIPE_LIST.get(name);
	}

	public static void removeRecipe (GCTRecipe recipe) {
		IMMUTABLE_COPY = null;
		RECIPE_LIST.remove(recipe.getName());
	}

	public static void removeRecipe (ResourceLocation name) {
		IMMUTABLE_COPY = null;
		RECIPE_LIST.remove(name);
	}

	public static ItemStack getOutputByIndex (int index) {
		if (index < 0 || index >= RECIPE_LIST.size()) {
			return ItemStack.EMPTY;
		}

		return getRecipeList().get(index).getRecipeOutput().copy();
	}

	public static GCTRecipe getRecipeByIndex (int index) {
		if (index < 0 || index >= RECIPE_LIST.size()) {
			return null;
		}

		return getRecipeList().get(index);
	}

	public static int indexOf (GCTRecipe recipe) {
		if (recipe == null || !RECIPE_LIST.containsKey(recipe.getName())) {
			return -1;
		}

		return getRecipeList().indexOf(recipe);
	}

	public static int getSize () {
		return RECIPE_LIST.size();
	}
}

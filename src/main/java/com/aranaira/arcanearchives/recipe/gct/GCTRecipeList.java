package com.aranaira.arcanearchives.recipe.gct;

import com.aranaira.arcanearchives.api.IGCTRecipeList;
import com.aranaira.arcanearchives.util.ItemUtilities;
import com.google.common.collect.ImmutableList;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GCTRecipeList implements IGCTRecipeList {
	public static final GCTRecipeList instance = new GCTRecipeList();

	private final LinkedHashMap<ResourceLocation, GCTRecipe> RECIPE_LIST = new LinkedHashMap<>();
	private ImmutableList<GCTRecipe> IMMUTABLE_COPY = null;

	@Override
	public Map<ResourceLocation, GCTRecipe> getRecipes () {
		return RECIPE_LIST;
	}

	@Override
	@Nullable
	public GCTRecipe getRecipeByOutput (ItemStack output) {
		for (GCTRecipe recipe : RECIPE_LIST.values()) {
			if (ItemUtilities.areStacksEqualIgnoreSize(output, recipe.getRecipeOutput())) {
				return recipe;
			}
		}

		return null;
	}

	@Override
	public List<GCTRecipe> getRecipeList () {
		if (IMMUTABLE_COPY == null) {
			IMMUTABLE_COPY = ImmutableList.copyOf(RECIPE_LIST.values());
		}

		return IMMUTABLE_COPY;
	}

	@Override
	public GCTRecipe makeAndAddRecipe (String name, @Nonnull ItemStack result, Object... recipe) {
		GCTRecipe newRecipe = new GCTRecipe(name, result, recipe);
		addRecipe(newRecipe);
		return newRecipe;
	}

	@Override
	public GCTRecipeWithConditionsCrafter makeAndAddRecipeWithCreatorAndCondition (String name, @Nonnull ItemStack result, Object... recipe) {
		GCTRecipeWithConditionsCrafter newRecipe = new GCTRecipeWithConditionsCrafter(name, result, recipe);
		addRecipe(newRecipe);
		return newRecipe;
	}

	@Override
	public void addRecipe (GCTRecipe recipe) {
		IMMUTABLE_COPY = null;
		RECIPE_LIST.put(recipe.getName(), recipe);
	}

	@Override
	@Nullable
	public GCTRecipe getRecipe (ResourceLocation name) {
		return RECIPE_LIST.get(name);
	}

	@Override
	public void removeRecipe (GCTRecipe recipe) {
		IMMUTABLE_COPY = null;
		RECIPE_LIST.remove(recipe.getName());
	}

	@Override
	public void removeRecipe (ResourceLocation name) {
		IMMUTABLE_COPY = null;
		RECIPE_LIST.remove(name);
	}

	@Override
	public ItemStack getOutputByIndex (int index) {
		if (index < 0 || index >= RECIPE_LIST.size()) {
			return ItemStack.EMPTY;
		}

		return getRecipeList().get(index).getRecipeOutput().copy();
	}

	@Override
	public GCTRecipe getRecipeByIndex (int index) {
		if (index < 0 || index >= RECIPE_LIST.size()) {
			return null;
		}

		return getRecipeList().get(index);
	}

	@Override
	public int indexOf (GCTRecipe recipe) {
		if (recipe == null || !RECIPE_LIST.containsKey(recipe.getName())) {
			return -1;
		}

		return getRecipeList().indexOf(recipe);
	}

	@Override
	public int getSize () {
		return RECIPE_LIST.size();
	}
}

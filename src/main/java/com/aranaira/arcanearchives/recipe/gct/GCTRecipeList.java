package com.aranaira.arcanearchives.recipe.gct;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.api.IGCTRecipe;
import com.aranaira.arcanearchives.api.IGCTRecipeList;
import com.aranaira.arcanearchives.util.ItemUtils;
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

	private final LinkedHashMap<ResourceLocation, IGCTRecipe> RECIPE_LIST = new LinkedHashMap<>();
	private ImmutableList<IGCTRecipe> IMMUTABLE_COPY = null;

	@Override
	public Map<ResourceLocation, IGCTRecipe> getRecipes () {
		return RECIPE_LIST;
	}

	@Override
	@Nullable
	public IGCTRecipe getRecipeByOutput (ItemStack output) {
		for (IGCTRecipe recipe : RECIPE_LIST.values()) {
			if (ItemUtils.areStacksEqualIgnoreSize(output, recipe.getRecipeOutput())) {
				return recipe;
			}
		}

		return null;
	}

	@Override
	public List<IGCTRecipe> getRecipeList () {
		if (IMMUTABLE_COPY == null) {
			IMMUTABLE_COPY = ImmutableList.copyOf(RECIPE_LIST.values());
		}

		return IMMUTABLE_COPY;
	}

	@Override
	public IGCTRecipe makeAndAddRecipe (String name, @Nonnull ItemStack result, Object... recipe) {
		GCTRecipe newRecipe = new GCTRecipe(name, result, recipe);
		addRecipe(newRecipe);
		return newRecipe;
	}

	@Override
	public IGCTRecipe makeAndReplaceRecipe (String name, @Nonnull ItemStack result, Object... recipe) throws IndexOutOfBoundsException {
		ResourceLocation nameResolved = new ResourceLocation(ArcaneArchives.MODID, name);
		if (!RECIPE_LIST.containsKey(nameResolved)) {
			throw new IndexOutOfBoundsException("Key '" + name + "' is not contained in the recipe list; use `makeAndAddRecipe` instead or check your spelling.");
		}
		GCTRecipe newRecipe = new GCTRecipe(name, result, recipe);
		addRecipe(newRecipe);
		return newRecipe;
	}

	@Override
	public IGCTRecipe makeAndAddRecipeWithCreatorAndCondition (String name, @Nonnull ItemStack result, Object... recipe) {
		GCTRecipeWithConditionsCrafter newRecipe = new GCTRecipeWithConditionsCrafter(name, result, recipe);
		addRecipe(newRecipe);
		return newRecipe;
	}

	@Override
	public IGCTRecipe makeAndReplaceRecipeWithCreatorAndCondition (String name, @Nonnull ItemStack result, Object... recipe) throws IndexOutOfBoundsException {
		ResourceLocation nameResolved = new ResourceLocation(ArcaneArchives.MODID, name);
		if (!RECIPE_LIST.containsKey(nameResolved)) {
			throw new IndexOutOfBoundsException("Key '" + name + "' is not contained in the recipe list; use `makeAndAddRecipe` instead or check your spelling.");
		}
		GCTRecipeWithConditionsCrafter newRecipe = new GCTRecipeWithConditionsCrafter(name, result, recipe);
		addRecipe(newRecipe);
		return newRecipe;
	}

	@Override
	public void addRecipe (IGCTRecipe recipe) {
		IMMUTABLE_COPY = null;
		RECIPE_LIST.put(recipe.getName(), recipe);
	}

	@Override
	@Nullable
	public IGCTRecipe getRecipe (ResourceLocation name) {
		return RECIPE_LIST.get(name);
	}

	@Override
	public void removeRecipe (IGCTRecipe recipe) {
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
	@Nullable
	public IGCTRecipe getRecipeByIndex (int index) {
		if (index < 0 || index >= RECIPE_LIST.size()) {
			return null;
		}

		return getRecipeList().get(index);
	}

	@Override
	public int indexOf (IGCTRecipe recipe) {
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

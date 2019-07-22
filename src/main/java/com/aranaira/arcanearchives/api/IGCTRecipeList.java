package com.aranaira.arcanearchives.api;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public interface IGCTRecipeList {
	Map<ResourceLocation, IGCTRecipe> getRecipes ();

	@Nullable
	IGCTRecipe getRecipeByOutput (ItemStack output);

	List<IGCTRecipe> getRecipeList ();

	IGCTRecipe makeAndAddRecipe (String name, @Nonnull ItemStack result, Object... recipe);

	IGCTRecipe makeAndAddRecipeWithCreatorAndCondition (String name, @Nonnull ItemStack result, Object... recipe);

	void addRecipe (IGCTRecipe recipe);

	@Nullable
	IGCTRecipe getRecipe (ResourceLocation name);

	void removeRecipe (IGCTRecipe recipe);

	void removeRecipe (ResourceLocation name);

	ItemStack getOutputByIndex (int index);

	IGCTRecipe getRecipeByIndex (int index);

	int indexOf (IGCTRecipe recipe);

	int getSize ();
}

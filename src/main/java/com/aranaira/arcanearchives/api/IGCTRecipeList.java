package com.aranaira.arcanearchives.api;

import com.aranaira.arcanearchives.recipe.gct.GCTRecipe;
import com.aranaira.arcanearchives.recipe.gct.GCTRecipeWithConditionsCrafter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public interface IGCTRecipeList {
	Map<ResourceLocation, GCTRecipe> getRecipes ();

	@Nullable
	GCTRecipe getRecipeByOutput (ItemStack output);

	List<GCTRecipe> getRecipeList ();

	GCTRecipe makeAndAddRecipe (String name, @Nonnull ItemStack result, Object... recipe);

	GCTRecipeWithConditionsCrafter makeAndAddRecipeWithCreatorAndCondition (String name, @Nonnull ItemStack result, Object... recipe);

	void addRecipe (GCTRecipe recipe);

	@Nullable
	GCTRecipe getRecipe (ResourceLocation name);

	void removeRecipe (GCTRecipe recipe);

	void removeRecipe (ResourceLocation name);

	ItemStack getOutputByIndex (int index);

	GCTRecipe getRecipeByIndex (int index);

	int indexOf (GCTRecipe recipe);

	int getSize ();
}

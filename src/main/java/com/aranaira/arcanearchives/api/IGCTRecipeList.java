package com.aranaira.arcanearchives.api;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * The default recipe list used to store Gem Cutter's Table recipes.
 */
public interface IGCTRecipeList {
	/**
	 * @return The current ResourceLocation->IGCTRecipe map of recipes.
	 */
	Map<ResourceLocation, IGCTRecipe> getRecipes ();

	/**
	 * Attempts to find a recipe which creates the output specified.
	 *
	 * @param output The output of the recipe.
	 * @return The IGCTRecipe or null if none was found.
	 */
	@Nullable
	IGCTRecipe getRecipeByOutput (ItemStack output);

	/**
	 * @return Returns an ordered list of recipes according to their
	 * addition in the RecipeLibrary.
	 * <p>
	 * By default this list is an Immutable copy.
	 */
	List<IGCTRecipe> getRecipeList ();

	/**
	 * Creates an IGCTRecipe instance using the default GCTRecipe with
	 * the name as the path of the ResourceLocation. The result is specified
	 * and then the ingredients as varargs.
	 *
	 * @param name   The string name used to refer to this recipe. Used in the
	 *               ResourceLocation mapping.
	 * @param result The itemstack created by this recipe.
	 * @param recipe The ingredients used to create this recipe. By default this
	 *               can consist of any combination of ItemStack (with quantity),
	 *               Item (default quantity of 1), Ingredient instance (default quantity
	 *               of 1), String specifying an ore dictionary value (default quantity
	 *               of 1), Block (default quantity of 1), or IngredientStack.
	 *               Any other type will print a warning message to chat.
	 * @return An instance of IGCTRecipe, a GCTRecipe by default.
	 */
	IGCTRecipe makeAndAddRecipe (String name, @Nonnull ItemStack result, Object... recipe);

	/**
	 * Identical to makeAndAddRecipe except that the default implementation
	 * allows for both the supply of the creator of the item to the item when
	 * it is crafted, and supports and enforces conditions added via IGCTRecipe::addCondition.
	 *
	 * @param name   See makeAndAddRecipe.
	 * @param result See makeAndAddRecipe.
	 * @param recipe See makeAndAddRecipe.
	 * @return An instance of IGCTRecipe which supports Creators and Conditions, which is
	 * by default GCTRecipeWithConditionsCrafter.
	 */
	IGCTRecipe makeAndAddRecipeWithCreatorAndCondition (String name, @Nonnull ItemStack result, Object... recipe);

	/**
	 * Allows the addition of a straight instance of an IGCTRecipe.
	 *
	 * @param recipe The recipe to be added.
	 */
	void addRecipe (IGCTRecipe recipe);

	/**
	 * Attempts to find the specified recipe by ResourceLocation name. Can
	 * be null if not found.
	 *
	 * @param name The ResourceLocation mapping for the recipe being sought.
	 * @return The relevant IGCTRecipe or null if not found.
	 */
	@Nullable
	IGCTRecipe getRecipe (ResourceLocation name);

	/**
	 * Attempts to remove a recipe using the recipe instance.
	 * <p>
	 * No indication of success or failure.
	 *
	 * @param recipe The recipe to be removed.
	 */
	void removeRecipe (IGCTRecipe recipe);

	/**
	 * As per removeRecipe but via the ResourceLocation mapping instead.
	 *
	 * @param name The ResourceLocation of the recipe to be removed.
	 */
	void removeRecipe (ResourceLocation name);

	/**
	 * Attempts first to find the recipe at the specified index
	 * and then return its recipe output.
	 *
	 * @param index The index into the ImmutableList copy.
	 * @return The ItemStack output or ItemStack.EMPTY if not found
	 */
	ItemStack getOutputByIndex (int index);

	/**
	 * Attempts to fetch a recipe via its index in the immutable list.
	 *
	 * @param index The index into the ImmutableList copy.
	 * @return The index or null if not found.
	 */
	@Nullable
	IGCTRecipe getRecipeByIndex (int index);

	/**
	 * @param recipe The recipe being sought.
	 * @return The index of the ImmutableList of recipes or -1 if not found.
	 */
	int indexOf (IGCTRecipe recipe);

	/**
	 * @return The number of registered recipes.
	 */
	int getSize ();
}

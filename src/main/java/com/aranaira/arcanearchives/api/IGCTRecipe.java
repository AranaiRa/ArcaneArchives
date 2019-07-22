package com.aranaira.arcanearchives.api;

import com.aranaira.arcanearchives.recipe.gct.GCTRecipeWithConditionsCrafter.GCTCondition;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

/**
 * This instance is the basis of all GCT Recipes. If you wish to create
 * a custom GCT recipe, you will need to implement this. However, that
 * would also require properly implementing item consumption from the
 * various linked inventories provided.
 *
 * For interfacing with and accessing recipes
 */
public interface IGCTRecipe extends IArcaneArchivesRecipe {
	/**
	 * @return Returns the index within the main GCT Recipe List that this recipe is
	 */
	int getIndex ();


	/**
	 * @return Returns the ResourceLocation associated with the recipe.
	 */
	ResourceLocation getName ();

	/**
	 * This is only called on the server side and the resulting output
	 * replaces the output in what is given to the player.
	 *
	 * @param player The player currently crafting.
	 * @param output The raw, base item.
	 * @return The item to be given to the player instead.
	 */
	ItemStack onCrafted (EntityPlayer player, ItemStack output);

	/**
	 * This is only called on the server.
	 *
	 * Its primary purpose is to modify ingredients when used in the Gem Crafter's Table.
	 * i.e., damaging damageable items or converting items into empty buckets.
	 *
	 * @param world The world.
	 * @param player The player performing the craft.
	 * @param craftingTable The TileEntity of the crafting table.
	 * @param ingredient The ItemStack of the ingredient being consumed.
	 * @return true if the item stack has been modified and returned to the player; otherwise false.
	 */
	boolean handleItemResult (World world, EntityPlayer player, TileEntity craftingTable, ItemStack ingredient);

	@FunctionalInterface
	interface RecipeIngredientHandler {
		boolean test (World world, EntityPlayer player, TileEntity craftingTile, ItemStack ingredient);
	}

	boolean craftable (EntityPlayer player, TileEntity craftingTable);

	@Override
	default boolean craftable (EntityPlayer player, IItemHandler inventory) {
		return false;
	}

	void consumeAndHandleInventory (IGCTRecipe recipe, IItemHandler inventory, EntityPlayer player, @Nullable TileEntity tile, @Nullable Runnable callback, @Nullable RecipeIngredientHandler handler);

	default void consumeAndHandleInventory (IArcaneArchivesRecipe recipe, IItemHandler inventory, EntityPlayer player, @Nullable TileEntity tile, @Nullable Runnable callback, @Nullable RecipeIngredientHandler handler) {
	}

	default IGCTRecipe addCondition (GCTCondition predicate) {
		return this;
	}
}

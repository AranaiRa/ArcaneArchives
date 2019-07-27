package com.aranaira.arcanearchives.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

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

	/**
	 * Returns whether or not the player currently accessing the given tile entity
	 * (Gem Cutter's Table specifically) is able to craft this recipe. Should generally
	 * be left for default.
	 *
	 * @param player The player crafting.
	 * @param craftingTable The tile entity of the GCT.
	 * @return boolean true if the recipe is craftable
	 */
	boolean craftable (EntityPlayer player, TileEntity craftingTable);

	/**
	 * Overrides the IArcaneArchivesRecipe and defaults to false. This is not used
	 * by the GCT.
	 *
	 * @param player
	 * @param inventory
	 * @return
	 */
	@Override
	default boolean craftable (EntityPlayer player, IItemHandler inventory) {
		return false;
	}

	/**
	 * Provides an access point for additional GCTCondition predicates
	 * to be stored in conditional GCT recipes. By default, this does
	 * nothing on a standard recipe.
	 *
	 * @param predicate An instance of GCTCondition
	 * @return the current recipe (for chaining purposes)
	 */
	default IGCTRecipe addCondition (GCTCondition predicate) {
		return this;
	}


	/**
	 * Handles the actual process of removing items from inventories and
	 * either consuming them or modifying and returning them. Callback
	 * is fired every time an item is consumed (in case synchronisation
	 * needs to happen).
	 *
	 * It is *presumed* that matching has already been checked; if matching
	 * is not checked before calling this, the result will be available
	 * but only partial consumption of ingredients will occur.
	 *
	 * Generally this is called from the container output slot onTake function.
	 *
	 * @param recipe The recipe instance being crafted.
	 * @param inventory The combined inventory to be search (in the default
	 *                  implementation this is a CombinedInvWrapper of the main
	 *                  player inventory and the Gem Cutter's Table inventory)
	 * @param player The player performing the craft
	 * @param tile The Gem Cutter's Table tile entity
	 * @param callback The callback to be fired. By default this is the container's
	 *                 detectAndSendChanges function.
	 * @param handler A RecipeIngredientHandler instance. See that file for full documentation.
	 */
	void consumeAndHandleInventory (IGCTRecipe recipe, IItemHandler inventory, EntityPlayer player, TileEntity tile, Runnable callback, RecipeIngredientHandler handler);

	/**
	 * This is used as a placeholder for a boolean-returning function that is called
	 * before an item can be considered craftable, even if the recipe matches the
	 * combined inventory.
	 *
	 * *Note that the default GCTRecipe implementation does not currently support
	 * conditions*.
	 */
	@FunctionalInterface
	interface GCTCondition {
		/**
		 * @param player The player interacting with the GCT.
		 * @param tile The Gem Cutter's Table tile entity.
		 * @return boolean true if the player can craft this recipe
		 */
		boolean test (EntityPlayer player, TileEntity tile);
	}
}

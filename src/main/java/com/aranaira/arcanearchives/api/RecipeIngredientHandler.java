package com.aranaira.arcanearchives.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * A test that indicates if the callback provided to the GCTRecipe's Consume function should
 * be fired.
 */
@FunctionalInterface
public interface RecipeIngredientHandler {
	/**
	 * The default instance/implementation is IGCTRecipe::handleItemResult.
	 *
	 * @param world        The world (this is generally only fired on the remote side)
	 * @param player       The player performing the craft.
	 * @param craftingTile The tile entity of the Gem Cutter's Table.
	 * @param ingredient   The item stack that is to be consumed.
	 * @return boolean true if the stack should not be consumed but returned to the inventory or
	 * to the player's inventory; false if the stack should be consumed.
	 */
	boolean test (World world, EntityPlayer player, TileEntity craftingTile, ItemStack ingredient);
}

package com.aranaira.arcanearchives.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public interface IGCTRecipe extends IArcaneArchivesRecipe {
	int getIndex ();

	ResourceLocation getName ();

	// Only called on the server side, in theory
	ItemStack onCrafted (EntityPlayer player, ItemStack output);

	// Also only called on the server side
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
}

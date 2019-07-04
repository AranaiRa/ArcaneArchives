package com.aranaira.arcanearchives.api;

import com.aranaira.arcanearchives.util.types.IngredientStack;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.List;

public interface IGCTRecipe {
	int getIndex ();

	ResourceLocation getName ();

	boolean matches (@Nonnull IItemHandler inv);

	boolean craftable (EntityPlayer player, TileEntity craftingTable);

	Int2IntMap getMatchingSlots (@Nonnull IItemHandler inv);

	ItemStack getRecipeOutput ();

	List<IngredientStack> getIngredients ();

	// Only called on the server side, in theory
	ItemStack onCrafted (EntityPlayer player, ItemStack output);

	// Also only called on the server side
	boolean handleItemResult (World world, EntityPlayer player, TileEntity craftingTable, ItemStack ingredient);
}

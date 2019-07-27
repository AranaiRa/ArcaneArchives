package com.aranaira.arcanearchives.api;

import com.aranaira.arcanearchives.recipe.IngredientStack;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * This is an internal recipe interface not intended for overriding
 * or public use.
 */
public interface IArcaneArchivesRecipe {
	boolean matches (@Nonnull IItemHandler inv);

	boolean craftable (EntityPlayer player, IItemHandler inventory);

	Int2IntMap getMatchingSlots (@Nonnull IItemHandler inv);

	ItemStack getRecipeOutput ();

	List<IngredientStack> getIngredients ();

}

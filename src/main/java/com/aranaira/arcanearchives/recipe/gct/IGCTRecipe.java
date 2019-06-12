package com.aranaira.arcanearchives.recipe.gct;

import com.aranaira.arcanearchives.tileentities.GemCuttersTableTileEntity;
import com.aranaira.arcanearchives.util.types.IngredientStack;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.swing.plaf.basic.BasicComboBoxUI.ItemHandler;
import java.util.List;

public interface IGCTRecipe {
	int getIndex ();

	ResourceLocation getName ();

	boolean matches (@Nonnull IItemHandler inv);

	boolean craftable (EntityPlayer player, GemCuttersTableTileEntity tile);

	Int2IntMap getMatchingSlots (@Nonnull IItemHandler inv);

	ItemStack getRecipeOutput ();

	List<IngredientStack> getIngredients ();

	// Only called on the server side, in theory
	ItemStack onCrafted (EntityPlayer player, ItemStack output);
}

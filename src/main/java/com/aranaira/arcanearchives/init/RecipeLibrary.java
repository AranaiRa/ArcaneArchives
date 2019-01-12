package com.aranaira.arcanearchives.init;

import java.util.ArrayList;
import java.util.Arrays;

import com.aranaira.arcanearchives.common.GemCuttersTableRecipe;

import net.minecraft.init.Items;
import net.minecraft.item.ItemRedstone;
import net.minecraft.item.ItemStack;

public class RecipeLibrary 
{
	public static GemCuttersTableRecipe COMPONENT_SCINTILLATINGINLAY_RECIPE = new GemCuttersTableRecipe(Arrays.asList(new ItemStack(ItemLibrary.COMPONENT_RADIANTDUST, 6), new ItemStack(Items.REDSTONE, 12), new ItemStack(Items.GOLD_INGOT, 1), new ItemStack(Items.GOLD_NUGGET, 6)), new ItemStack(ItemLibrary.COMPONENT_SCINTILLATINGINLAY));
	
	public static void RegisterGCTRecipes()
	{
		GemCuttersTableRecipe.addRecipe(COMPONENT_SCINTILLATINGINLAY_RECIPE);
	}
}

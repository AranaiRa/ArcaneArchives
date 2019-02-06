package com.aranaira.arcanearchives.init;

import com.aranaira.arcanearchives.registry.crafting.GemCuttersTableRecipe;
import com.aranaira.arcanearchives.registry.crafting.GemCuttersTableRecipeList;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.Collections;

public class RecipeLibrary
{
	public static GemCuttersTableRecipe CUT_RADIANT_QUARTZ_RECIPE = new GemCuttersTableRecipe(Collections.singletonList(new ItemStack(ItemLibrary.RAW_RADIANT_QUARTZ, 2)), new ItemStack(ItemLibrary.CUT_RADIANT_QUARTZ, 1));

	public static GemCuttersTableRecipe COMPONENT_CONTAINMENTFIELD_RECIPE = new GemCuttersTableRecipe(Arrays.asList(new ItemStack(ItemLibrary.COMPONENT_SCINTILLATINGINLAY, 1), new ItemStack(Items.GOLD_INGOT, 2), new ItemStack(ItemLibrary.CUT_RADIANT_QUARTZ, 2)), new ItemStack(ItemLibrary.COMPONENT_CONTAINMENTFIELD));
	public static GemCuttersTableRecipe COMPONENT_MATERIALINTERFACE_RECIPE = new GemCuttersTableRecipe(Arrays.asList(new ItemStack(ItemLibrary.COMPONENT_SCINTILLATINGINLAY, 1), new ItemStack(Items.GOLD_INGOT, 1), new ItemStack(ItemLibrary.CUT_RADIANT_QUARTZ, 1)), new ItemStack(ItemLibrary.COMPONENT_MATERIALINTERFACE));
	public static GemCuttersTableRecipe COMPONENT_MATRIXBRACE_RECIPE = new GemCuttersTableRecipe(Arrays.asList(new ItemStack(ItemLibrary.COMPONENT_SCINTILLATINGINLAY, 1), new ItemStack(Items.GOLD_INGOT, 2)), new ItemStack(ItemLibrary.COMPONENT_MATRIXBRACE));
	public static GemCuttersTableRecipe COMPONENT_RADIANTDUST_RECIPE = new GemCuttersTableRecipe(Collections.singletonList(new ItemStack(ItemLibrary.RAW_RADIANT_QUARTZ, 1)), new ItemStack(ItemLibrary.COMPONENT_RADIANTDUST, 2));
	public static GemCuttersTableRecipe COMPONENT_SCINTILLATINGINLAY_RECIPE = new GemCuttersTableRecipe(Arrays.asList(new ItemStack(ItemLibrary.COMPONENT_RADIANTDUST, 6), new ItemStack(Items.REDSTONE, 12), new ItemStack(Items.GOLD_INGOT, 1), new ItemStack(Items.GOLD_NUGGET, 6)), new ItemStack(ItemLibrary.COMPONENT_SCINTILLATINGINLAY));

	public static GemCuttersTableRecipe MATRIX_CORE_RECIPE = new GemCuttersTableRecipe(Arrays.asList(new ItemStack(ItemLibrary.CUT_RADIANT_QUARTZ, 60), new ItemStack(Blocks.LOG, 12), new ItemStack(ItemLibrary.COMPONENT_SCINTILLATINGINLAY, 12), new ItemStack(BlockLibrary.RADIANT_LANTERN, 4), new ItemStack(Blocks.BOOKSHELF, 8)), new ItemStack(BlockLibrary.MATRIX_CRYSTAL_CORE, 1));
	public static GemCuttersTableRecipe MATRIX_STORAGE_RECIPE = new GemCuttersTableRecipe(Arrays.asList(new ItemStack(ItemLibrary.COMPONENT_MATRIXBRACE, 2), new ItemStack(ItemLibrary.COMPONENT_MATERIALINTERFACE, 1), new ItemStack(ItemLibrary.CUT_RADIANT_QUARTZ, 24)), new ItemStack(BlockLibrary.MATRIX_STORAGE, 1));
	public static GemCuttersTableRecipe MATRIX_REPOSITORY_RECIPE = new GemCuttersTableRecipe(Arrays.asList(new ItemStack(BlockLibrary.MATRIX_STORAGE, 1), new ItemStack(ItemLibrary.COMPONENT_MATERIALINTERFACE, 2)), new ItemStack(BlockLibrary.MATRIX_REPOSITORY, 1));

	public static GemCuttersTableRecipe RADIANT_LANTERN_RECIPE = new GemCuttersTableRecipe(Arrays.asList(new ItemStack(ItemLibrary.RAW_RADIANT_QUARTZ, 1), new ItemStack(Items.GOLD_NUGGET, 1)), new ItemStack(BlockLibrary.RADIANT_LANTERN, 16));

	public static void RegisterGCTRecipes()
	{
		GemCuttersTableRecipeList.addRecipe(COMPONENT_RADIANTDUST_RECIPE);
		GemCuttersTableRecipeList.addRecipe(CUT_RADIANT_QUARTZ_RECIPE);

		GemCuttersTableRecipeList.addRecipe(RADIANT_LANTERN_RECIPE);

		GemCuttersTableRecipeList.addRecipe(COMPONENT_CONTAINMENTFIELD_RECIPE);
		GemCuttersTableRecipeList.addRecipe(COMPONENT_MATERIALINTERFACE_RECIPE);
		GemCuttersTableRecipeList.addRecipe(COMPONENT_MATRIXBRACE_RECIPE);
		GemCuttersTableRecipeList.addRecipe(COMPONENT_SCINTILLATINGINLAY_RECIPE);

		GemCuttersTableRecipeList.addRecipe(MATRIX_CORE_RECIPE);
		GemCuttersTableRecipeList.addRecipe(MATRIX_STORAGE_RECIPE);
		GemCuttersTableRecipeList.addRecipe(MATRIX_REPOSITORY_RECIPE);

	}
}

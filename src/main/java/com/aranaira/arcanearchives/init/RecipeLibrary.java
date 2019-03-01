package com.aranaira.arcanearchives.init;

import com.aranaira.arcanearchives.recipe.gct.GCTRecipe;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class RecipeLibrary
{
	public static GCTRecipe CUT_RADIANT_QUARTZ_RECIPE;
	public static GCTRecipe COMPONENT_CONTAINMENTFIELD_RECIPE;
	public static GCTRecipe COMPONENT_MATERIALINTERFACE_RECIPE;
	public static GCTRecipe COMPONENT_MATRIXBRACE_RECIPE;
	public static GCTRecipe COMPONENT_RADIANTDUST_RECIPE;
	public static GCTRecipe COMPONENT_SCINTILLATINGINLAY_RECIPE;
	public static GCTRecipe MATRIX_CORE_RECIPE;
	public static GCTRecipe MATRIX_STORAGE_RECIPE;
	public static GCTRecipe MATRIX_REPOSITORY_RECIPE;
	public static GCTRecipe RADIANT_LANTERN_RECIPE;

	public static void buildRecipes()
	{
		RADIANT_LANTERN_RECIPE = GCTRecipe.buildAndAdd("radiant_lantern", new ItemStack(BlockRegistry.RADIANT_LANTERN, 4), ItemRegistry.RAW_RADIANT_QUARTZ, ItemRegistry.RAW_RADIANT_QUARTZ, Items.GOLD_NUGGET);

		MATRIX_REPOSITORY_RECIPE = GCTRecipe.buildAndAdd("matrix_repository", new ItemStack(BlockRegistry.MATRIX_REPOSITORY, 1), BlockRegistry.MATRIX_STORAGE, ItemRegistry.COMPONENT_MATERIALINTERFACE, ItemRegistry.COMPONENT_MATERIALINTERFACE);

		COMPONENT_SCINTILLATINGINLAY_RECIPE = GCTRecipe.buildAndAdd("scintillatinginlay", new ItemStack(ItemRegistry.COMPONENT_SCINTILLATINGINLAY), ItemRegistry.COMPONENT_RADIANTDUST, ItemRegistry.COMPONENT_RADIANTDUST, ItemRegistry.COMPONENT_RADIANTDUST, ItemRegistry.COMPONENT_RADIANTDUST, ItemRegistry.COMPONENT_RADIANTDUST, ItemRegistry.COMPONENT_RADIANTDUST, Items.REDSTONE, Items.REDSTONE, Items.REDSTONE, Items.REDSTONE, Items.REDSTONE, Items.REDSTONE, Items.REDSTONE, Items.REDSTONE, Items.REDSTONE, Items.REDSTONE, Items.REDSTONE, Items.REDSTONE, Items.GOLD_INGOT, Items.GOLD_NUGGET, Items.GOLD_NUGGET, Items.GOLD_NUGGET, Items.GOLD_NUGGET, Items.GOLD_NUGGET, Items.GOLD_NUGGET); // TODO: Simplify

		COMPONENT_RADIANTDUST_RECIPE = GCTRecipe.buildAndAdd("radiantdust", new ItemStack(ItemRegistry.COMPONENT_RADIANTDUST, 2), ItemRegistry.RAW_RADIANT_QUARTZ);

		COMPONENT_MATRIXBRACE_RECIPE = GCTRecipe.buildAndAdd("component_matrixbrace", new ItemStack(ItemRegistry.COMPONENT_MATRIXBRACE), ItemRegistry.COMPONENT_SCINTILLATINGINLAY, Items.GOLD_INGOT, Items.GOLD_INGOT);

		COMPONENT_MATERIALINTERFACE_RECIPE = GCTRecipe.buildAndAdd("materialinterface", new ItemStack(ItemRegistry.COMPONENT_MATERIALINTERFACE), ItemRegistry.COMPONENT_SCINTILLATINGINLAY, Items.GOLD_INGOT, ItemRegistry.CUT_RADIANT_QUARTZ);

		COMPONENT_CONTAINMENTFIELD_RECIPE = GCTRecipe.buildAndAdd("containmentfield", new ItemStack(ItemRegistry.COMPONENT_CONTAINMENTFIELD), ItemRegistry.COMPONENT_SCINTILLATINGINLAY, Items.GOLD_INGOT, Items.GOLD_INGOT, ItemRegistry.CUT_RADIANT_QUARTZ, ItemRegistry.CUT_RADIANT_QUARTZ);

		CUT_RADIANT_QUARTZ_RECIPE = GCTRecipe.buildAndAdd("cut_radiant_quartz", new ItemStack(ItemRegistry.CUT_RADIANT_QUARTZ, 1), ItemRegistry.RAW_RADIANT_QUARTZ, ItemRegistry.RAW_RADIANT_QUARTZ);

		List<Object> ingredients = new ArrayList<>();
		for(int i = 0; i < 60; i++) ingredients.add(ItemRegistry.CUT_RADIANT_QUARTZ);
		for(int i = 0; i < 12; i++) ingredients.add("logWood");
		for(int i = 0; i < 12; i++) ingredients.add(ItemRegistry.COMPONENT_SCINTILLATINGINLAY);
		for(int i = 0; i < 4; i++) ingredients.add(BlockRegistry.RADIANT_LANTERN);
		for(int i = 0; i < 8; i++) ingredients.add(Blocks.BOOKSHELF); // TODO: Extend with oredict support
		MATRIX_CORE_RECIPE = GCTRecipe.buildAndAdd("matrix_core", new ItemStack(BlockRegistry.MATRIX_CRYSTAL_CORE, 1), ingredients.toArray(new Object[0]));

		ingredients.clear();
		for(int i = 0; i < 2; i++) ingredients.add(ItemRegistry.COMPONENT_MATRIXBRACE);
		ingredients.add(ItemRegistry.COMPONENT_MATERIALINTERFACE);
		for(int i = 0; i < 24; i++) ingredients.add(ItemRegistry.CUT_RADIANT_QUARTZ);
		MATRIX_STORAGE_RECIPE = GCTRecipe.buildAndAdd("matrix_storage", new ItemStack(BlockRegistry.MATRIX_STORAGE, 1), ingredients.toArray(new Object[0]));
	}
}

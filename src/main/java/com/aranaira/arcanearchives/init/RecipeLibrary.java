package com.aranaira.arcanearchives.init;

import com.aranaira.arcanearchives.recipe.gct.GCTRecipe;
import com.aranaira.arcanearchives.recipe.gct.GCTRecipeList;
import com.aranaira.arcanearchives.util.types.IngredientStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreIngredient;

@SuppressWarnings("unused")
public class RecipeLibrary
{
	public static GCTRecipe CUT_RADIANT_QUARTZ_RECIPE;
	public static GCTRecipe MANIFEST_RECIPE;
	public static GCTRecipe COMPONENT_CONTAINMENTFIELD_RECIPE;
	public static GCTRecipe COMPONENT_MATERIALINTERFACE_RECIPE;
	public static GCTRecipe COMPONENT_MATRIXBRACE_RECIPE;
	public static GCTRecipe COMPONENT_RADIANTDUST_RECIPE;
	public static GCTRecipe COMPONENT_SCINTILLATINGINLAY_RECIPE;
	public static GCTRecipe MATRIX_CORE_RECIPE;
	public static GCTRecipe MATRIX_STORAGE_RECIPE;
	public static GCTRecipe MATRIX_REPOSITORY_RECIPE;
	public static GCTRecipe RADIANT_LANTERN_RECIPE;
	public static GCTRecipe RADIANT_TROVE_RECIPE;

	public static void buildRecipes()
	{
		COMPONENT_RADIANTDUST_RECIPE = GCTRecipeList.makeAndAddRecipe("radiantdust", new ItemStack(ItemRegistry.COMPONENT_RADIANTDUST, 2), ItemRegistry.RAW_RADIANT_QUARTZ);

		CUT_RADIANT_QUARTZ_RECIPE = GCTRecipeList.makeAndAddRecipe("cut_radiant_quartz", new ItemStack(ItemRegistry.CUT_RADIANT_QUARTZ, 1), new ItemStack(ItemRegistry.RAW_RADIANT_QUARTZ, 2));

		MANIFEST_RECIPE = GCTRecipeList.makeAndAddRecipe("manifest", new ItemStack(ItemRegistry.MANIFEST, 1), new IngredientStack("paper", 1), new IngredientStack("dyeBlack", 1), new ItemStack(ItemRegistry.COMPONENT_RADIANTDUST, 2));

		RADIANT_LANTERN_RECIPE = GCTRecipeList.makeAndAddRecipe("radiant_lantern", new ItemStack(BlockRegistry.RADIANT_LANTERN, 4), new ItemStack(ItemRegistry.RAW_RADIANT_QUARTZ, 2), new IngredientStack("nuggetGold", 1));

		RADIANT_TROVE_RECIPE = GCTRecipeList.makeAndAddRecipe("radiant_trove", new ItemStack(BlockRegistry.RADIANT_TROVE, 4), new ItemStack(ItemRegistry.CUT_RADIANT_QUARTZ, 4), new ItemStack(ItemRegistry.COMPONENT_MATRIXBRACE), new ItemStack(ItemRegistry.COMPONENT_MATERIALINTERFACE), new ItemStack(BlockRegistry.RADIANT_CHEST));

		/*MATRIX_REPOSITORY_RECIPE = GCTRecipeList.makeAndAddRecipe("matrix_repository", new ItemStack(BlockRegistry.MATRIX_REPOSITORY, 1), BlockRegistry.MATRIX_STORAGE, ItemRegistry.COMPONENT_MATERIALINTERFACE, ItemRegistry.COMPONENT_MATERIALINTERFACE);*/

		COMPONENT_SCINTILLATINGINLAY_RECIPE = GCTRecipeList.makeAndAddRecipe("scintillatinginlay", new ItemStack(ItemRegistry.COMPONENT_SCINTILLATINGINLAY, 1), new ItemStack(ItemRegistry.COMPONENT_RADIANTDUST, 6), new IngredientStack("dustRedstone", 12), new IngredientStack("ingotGold"), new IngredientStack("nuggetGold", 6));

		COMPONENT_MATRIXBRACE_RECIPE = GCTRecipeList.makeAndAddRecipe("component_matrixbrace", new ItemStack(ItemRegistry.COMPONENT_MATRIXBRACE, 1), ItemRegistry.COMPONENT_SCINTILLATINGINLAY, new IngredientStack("ingotGold", 2));

		COMPONENT_MATERIALINTERFACE_RECIPE = GCTRecipeList.makeAndAddRecipe("materialinterface", new ItemStack(ItemRegistry.COMPONENT_MATERIALINTERFACE, 1), ItemRegistry.COMPONENT_SCINTILLATINGINLAY, new IngredientStack("ingotGold"), ItemRegistry.CUT_RADIANT_QUARTZ);

		COMPONENT_CONTAINMENTFIELD_RECIPE = GCTRecipeList.makeAndAddRecipe("containmentfield", new ItemStack(ItemRegistry.COMPONENT_CONTAINMENTFIELD, 1), ItemRegistry.COMPONENT_SCINTILLATINGINLAY, new IngredientStack("ingotGold", 2), new ItemStack(ItemRegistry.CUT_RADIANT_QUARTZ, 2));

		/*MATRIX_CORE_RECIPE = GCTRecipeList.makeAndAddRecipe("matrix_core", new ItemStack(BlockRegistry.MATRIX_CRYSTAL_CORE, 1), new ItemStack(ItemRegistry.CUT_RADIANT_QUARTZ, 60), new IngredientStack("logWood", 12), new ItemStack(ItemRegistry.COMPONENT_SCINTILLATINGINLAY, 12), new IngredientStack(BlockRegistry.RADIANT_LANTERN, 4), new IngredientStack("bookshelf", 1));*/

		/*MATRIX_STORAGE_RECIPE = GCTRecipeList.makeAndAddRecipe("matrix_storage", new ItemStack(BlockRegistry.MATRIX_STORAGE, 1), new ItemStack(ItemRegistry.COMPONENT_MATRIXBRACE, 2), ItemRegistry.COMPONENT_MATERIALINTERFACE, new ItemStack(ItemRegistry.CUT_RADIANT_QUARTZ, 24));*/
	}
}

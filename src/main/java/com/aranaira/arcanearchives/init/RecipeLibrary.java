package com.aranaira.arcanearchives.init;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.config.ConfigHandler;
import com.aranaira.arcanearchives.config.ConfigHandler.ArsenalConfig;
import com.aranaira.arcanearchives.data.*;
import com.aranaira.arcanearchives.data.HiveSaveData.Hive;
import com.aranaira.arcanearchives.integration.astralsorcery.Liquefaction;
import com.aranaira.arcanearchives.recipe.gct.GCTRecipe;
import com.aranaira.arcanearchives.recipe.gct.GCTRecipeList;
import com.aranaira.arcanearchives.tileentities.GemCuttersTableTileEntity;
import com.aranaira.arcanearchives.util.types.IngredientStack;
import net.minecraft.init.Blocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = ArcaneArchives.MODID)
public class RecipeLibrary {
	public static GCTRecipe SHAPED_RADIANT_QUARTZ_RECIPE;
	public static GCTRecipe MANIFEST_RECIPE;
	public static GCTRecipe CONTAINMENT_FIELD_RECIPE;
	public static GCTRecipe MATERIAL_INTERFACE_RECIPE;
	public static GCTRecipe MATRIX_BRACE_RECIPE;
	public static GCTRecipe RADIANT_DUST_RECIPE;
	public static GCTRecipe SCINTILLATING_INLAY_RECIPE;
	public static GCTRecipe DEVOURING_CHARM_RECIPE;
	/*public static GCTRecipe MATRIX_CORE_RECIPE;
	public static GCTRecipe MATRIX_STORAGE_RECIPE;
	public static GCTRecipe MATRIX_REPOSITORY_RECIPE;*/
	public static GCTRecipe GEM_SOCKET_RECIPE;
	public static GCTRecipe RADIANT_LANTERN_RECIPE;
	public static GCTRecipe RADIANT_TROVE_RECIPE;
	public static GCTRecipe RADIANT_TANK_RECIPE;
	public static GCTRecipe MONITORING_CRYSTAL_RECIPE;
	public static GCTRecipe SCEPTER_MANIPULATION_RECIPE;
	public static GCTRecipe RADIANT_AMPHORA_RECIPE;
	public static GCTRecipe LETTER_OF_INVITATION_RECIPE;
	public static GCTRecipe LETTER_OF_RESIGNATION_RECIPE;
	public static GCTRecipe WRIT_OF_EXPULSION_RECIPE;

	public static GCTRecipe MURDERGLEAM_RECIPE;
	public static GCTRecipe CLEANSEGLEAM_RECIPE;
	public static GCTRecipe SALVEGLEAM_RECIPE;
	public static GCTRecipe MUNCHSTONE_RECIPE;
	public static GCTRecipe MINDSPINDLE_RECIPE;
	public static GCTRecipe RIVERTEAR_RECIPE;
	public static GCTRecipe MOUNTAINTEAR_RECIPE;
	public static GCTRecipe PARCHTEAR_RECIPE;
	public static GCTRecipe PHOENIXWAY_RECIPE;
	public static GCTRecipe STORMWAY_RECIPE;

	public static void buildRecipes () {
		RADIANT_DUST_RECIPE = GCTRecipeList.makeAndAddRecipe("radiant_dust", new ItemStack(ItemRegistry.COMPONENT_RADIANTDUST, 2), ItemRegistry.RAW_RADIANT_QUARTZ);

		SHAPED_RADIANT_QUARTZ_RECIPE = GCTRecipeList.makeAndAddRecipe("shaped_radiant_quartz", new ItemStack(ItemRegistry.SHAPED_RADIANT_QUARTZ, 1), new ItemStack(ItemRegistry.RAW_RADIANT_QUARTZ, 2));

		MANIFEST_RECIPE = GCTRecipeList.makeAndAddRecipe("manifest", new ItemStack(ItemRegistry.MANIFEST, 1), new IngredientStack("paper", 1), new IngredientStack("dyeBlack", 1), new ItemStack(ItemRegistry.COMPONENT_RADIANTDUST, 2));

		////////////////////

		LETTER_OF_INVITATION_RECIPE = GCTRecipeList.makeAndAddRecipeWithCreatorAndCondition("letter_invitation", new ItemStack(ItemRegistry.LETTER_OF_INVITATION, 1), new IngredientStack("paper", 3), new ItemStack(ItemRegistry.COMPONENT_RADIANTDUST, 1), new IngredientStack("dyeLightBlue", 1)).addCondition((EntityPlayer player, GemCuttersTableTileEntity tile) -> {
			if (!player.world.isRemote) {
				HiveSaveData saveData = NetworkHelper.getHiveData(player.world);
				Hive hive = saveData.getHiveByMember(player.getUniqueID());
				return (hive == null || hive.owner.equals(player.getUniqueID()));
			} else {
				ClientNetwork network = NetworkHelper.getClientNetwork();
				return network.ownsHive() || !network.inHive();
			}
		});

		LETTER_OF_RESIGNATION_RECIPE = GCTRecipeList.makeAndAddRecipeWithCreatorAndCondition("letter_resignation", new ItemStack(ItemRegistry.LETTER_OF_RESIGNATION, 1), new IngredientStack("paper", 3), new ItemStack(ItemRegistry.COMPONENT_RADIANTDUST, 1), new IngredientStack("dyePink", 1)).addCondition((EntityPlayer player, GemCuttersTableTileEntity tile) -> {
			if (!player.world.isRemote) {
				HiveSaveData saveData = NetworkHelper.getHiveData(player.world);
				Hive hive = saveData.getHiveByMember(player.getUniqueID());
				return hive != null;
			} else {
				ClientNetwork network = NetworkHelper.getClientNetwork();
				return network.inHive();
			}
		});

		WRIT_OF_EXPULSION_RECIPE = GCTRecipeList.makeAndAddRecipeWithCreatorAndCondition("writ_explusion", new ItemStack(ItemRegistry.WRIT_OF_EXPULSION, 1), new IngredientStack("paper", 3), new ItemStack(ItemRegistry.COMPONENT_RADIANTDUST, 1), new IngredientStack("dyeRed", 1)).addCondition((EntityPlayer player, GemCuttersTableTileEntity tile) -> {
			if (!player.world.isRemote) {
				HiveSaveData saveData = NetworkHelper.getHiveData(player.world);
				Hive hive = saveData.getHiveByMember(player.getUniqueID());
				return hive != null && hive.owner.equals(player.getUniqueID());
			} else {
				ClientNetwork network = NetworkHelper.getClientNetwork();
				return network.inHive() && network.ownsHive();
			}
		});

		//////

		SCEPTER_MANIPULATION_RECIPE = GCTRecipeList.makeAndAddRecipe("scepter_manipulation", new ItemStack(ItemRegistry.SCEPTER_MANIPULATION), new ItemStack(ItemRegistry.SCEPTER_REVELATION, 1), new IngredientStack(ItemRegistry.COMPONENT_SCINTILLATINGINLAY, 1));

		RADIANT_LANTERN_RECIPE = GCTRecipeList.makeAndAddRecipe("radiant_lantern", new ItemStack(BlockRegistry.RADIANT_LANTERN, 4), new ItemStack(ItemRegistry.RAW_RADIANT_QUARTZ, 2), new IngredientStack("nuggetGold", 1));

		RADIANT_TROVE_RECIPE = GCTRecipeList.makeAndAddRecipe("radiant_trove", new ItemStack(BlockRegistry.RADIANT_TROVE, 4), new ItemStack(ItemRegistry.SHAPED_RADIANT_QUARTZ, 2), new ItemStack(ItemRegistry.COMPONENT_MATERIALINTERFACE), new ItemStack(BlockRegistry.RADIANT_CHEST));

		RADIANT_TANK_RECIPE = GCTRecipeList.makeAndAddRecipe("radiant_tank", new ItemStack(BlockRegistry.RADIANT_TANK, 4), new ItemStack(ItemRegistry.SHAPED_RADIANT_QUARTZ, 2), new ItemStack(ItemRegistry.COMPONENT_CONTAINMENTFIELD), new IngredientStack("ingotGold", 1));

		MONITORING_CRYSTAL_RECIPE = GCTRecipeList.makeAndAddRecipe("monitoring_crystal", new ItemStack(BlockRegistry.MONITORING_CRYSTAL), new ItemStack(ItemRegistry.SHAPED_RADIANT_QUARTZ, 1), new IngredientStack("nuggetGold", 2), new IngredientStack("stickWood", 4));

		/*MATRIX_REPOSITORY_RECIPE = GCTRecipeList.makeAndAddRecipe("matrix_repository", new ItemStack(BlockRegistry.MATRIX_REPOSITORY, 1), BlockRegistry.MATRIX_STORAGE, ItemRegistry.COMPONENT_MATERIALINTERFACE, ItemRegistry.COMPONENT_MATERIALINTERFACE);*/

		SCINTILLATING_INLAY_RECIPE = GCTRecipeList.makeAndAddRecipe("scintillat_inginlay", new ItemStack(ItemRegistry.COMPONENT_SCINTILLATINGINLAY, 1), new ItemStack(ItemRegistry.COMPONENT_RADIANTDUST, 6), new IngredientStack("dustRedstone", 12), new IngredientStack("ingotGold"), new IngredientStack("nuggetGold", 6));

		MATRIX_BRACE_RECIPE = GCTRecipeList.makeAndAddRecipe("matrix_brace", new ItemStack(ItemRegistry.COMPONENT_MATRIXBRACE, 1), ItemRegistry.COMPONENT_SCINTILLATINGINLAY, new IngredientStack("ingotGold", 2));

		MATERIAL_INTERFACE_RECIPE = GCTRecipeList.makeAndAddRecipe("material_interface", new ItemStack(ItemRegistry.COMPONENT_MATERIALINTERFACE, 1), ItemRegistry.COMPONENT_SCINTILLATINGINLAY, new IngredientStack("ingotGold"), ItemRegistry.SHAPED_RADIANT_QUARTZ);

		CONTAINMENT_FIELD_RECIPE = GCTRecipeList.makeAndAddRecipe("containment_field", new ItemStack(ItemRegistry.COMPONENT_CONTAINMENTFIELD, 1), ItemRegistry.COMPONENT_SCINTILLATINGINLAY, new IngredientStack("ingotGold", 2), new ItemStack(ItemRegistry.SHAPED_RADIANT_QUARTZ, 2));

		DEVOURING_CHARM_RECIPE = GCTRecipeList.makeAndAddRecipe("devouring_charm", new ItemStack(ItemRegistry.DEVOURING_CHARM, 4), new IngredientStack("ingotGold", 1), new IngredientStack(Blocks.OBSIDIAN, 2), new IngredientStack(Items.FLINT_AND_STEEL, 1));

		RADIANT_AMPHORA_RECIPE = GCTRecipeList.makeAndAddRecipe("radiant_amphora", new ItemStack(ItemRegistry.RADIANT_AMPHORA), new ItemStack(ItemRegistry.COMPONENT_RADIANTDUST, 4), new ItemStack(Items.CLAY_BALL, 4), new ItemStack(Items.GOLD_NUGGET, 4));

		if (ConfigHandler.ArsenalConfig.EnableArsenal) {
			GEM_SOCKET_RECIPE = GCTRecipeList.makeAndAddRecipe("gemsocket", new ItemStack(ItemRegistry.BAUBLE_GEMSOCKET), new ItemStack(ItemRegistry.COMPONENT_SCINTILLATINGINLAY, 1), new ItemStack(Items.GOLD_NUGGET, 4), new ItemStack(Items.BOWL, 1), new ItemStack(Items.LEATHER, 1));

			MURDERGLEAM_RECIPE = GCTRecipeList.makeAndAddRecipe("murdergleam", new ItemStack(ItemRegistry.MURDERGLEAM), new ItemStack(ItemRegistry.SHAPED_RADIANT_QUARTZ, 1), new IngredientStack("dyeYellow", 4), new IngredientStack(Items.DIAMOND_SWORD), new IngredientStack(Ingredient.fromItem(Items.BLAZE_POWDER), 2));

			CLEANSEGLEAM_RECIPE = GCTRecipeList.makeAndAddRecipe("cleansestone", new ItemStack(ItemRegistry.CLEANSEGLEAM), new ItemStack(ItemRegistry.SHAPED_RADIANT_QUARTZ, 1), new IngredientStack("dyeBlue", 4), new IngredientStack(Items.POISONOUS_POTATO), new IngredientStack(Ingredient.fromItem(Items.MILK_BUCKET), 1), new IngredientStack(Ingredient.fromItem(Items.MILK_BUCKET), 1));

			SALVEGLEAM_RECIPE = GCTRecipeList.makeAndAddRecipe("salvegleam", new ItemStack(ItemRegistry.SALVEGLEAM), new ItemStack(ItemRegistry.SHAPED_RADIANT_QUARTZ, 1), new IngredientStack("dyePink", 4), new IngredientStack(Items.GOLDEN_APPLE), new IngredientStack(Items.SPECKLED_MELON), new IngredientStack(Items.GOLDEN_CARROT));

			MUNCHSTONE_RECIPE = GCTRecipeList.makeAndAddRecipe("munchstone", new ItemStack(ItemRegistry.MUNCHSTONE), new ItemStack(ItemRegistry.SHAPED_RADIANT_QUARTZ, 1), new IngredientStack("dyeBlack", 4), new IngredientStack(Items.CAKE), new IngredientStack(Ingredient.fromItem(Items.SPECKLED_MELON), 2));

			MINDSPINDLE_RECIPE = GCTRecipeList.makeAndAddRecipe("mindspindle", new ItemStack(ItemRegistry.MINDSPINDLE), new ItemStack(ItemRegistry.SHAPED_RADIANT_QUARTZ, 1), new IngredientStack("dyeGreen", 4), new IngredientStack(Items.ENCHANTED_BOOK), new IngredientStack(Ingredient.fromItem(Items.EMERALD), 2));

			RIVERTEAR_RECIPE = GCTRecipeList.makeAndAddRecipe("rivertear", new ItemStack(ItemRegistry.RIVERTEAR), new ItemStack(ItemRegistry.SHAPED_RADIANT_QUARTZ, 1), new IngredientStack(Ingredient.fromItem(Items.SUGAR), 4), new IngredientStack(Ingredient.fromItem(Items.WATER_BUCKET)), new IngredientStack(Ingredient.fromItem(Items.WATER_BUCKET)));

			MOUNTAINTEAR_RECIPE = GCTRecipeList.makeAndAddRecipe("mountaintear", new ItemStack(ItemRegistry.MOUNTAINTEAR), new ItemStack(ItemRegistry.SHAPED_RADIANT_QUARTZ, 1), new IngredientStack("dyeOrange", 4), new IngredientStack(Blocks.MAGMA, 4), new IngredientStack(Ingredient.fromItem(Items.LAVA_BUCKET)), new IngredientStack(Ingredient.fromItem(Items.LAVA_BUCKET)));

			PARCHTEAR_RECIPE = GCTRecipeList.makeAndAddRecipe("parchtear", new ItemStack(ItemRegistry.PARCHTEAR), new ItemStack(ItemRegistry.SHAPED_RADIANT_QUARTZ, 1), new IngredientStack("dyeBlack", 4), new IngredientStack(Blocks.HARDENED_CLAY, 4), new IngredientStack(Blocks.DEADBUSH, 4));

			PHOENIXWAY_RECIPE = GCTRecipeList.makeAndAddRecipe("phoenixway", new ItemStack(ItemRegistry.PHOENIXWAY), new ItemStack(ItemRegistry.SHAPED_RADIANT_QUARTZ, 1), new IngredientStack(Ingredient.fromItem(Items.BLAZE_POWDER), 4), new IngredientStack(Ingredient.fromItem(Items.COAL), 4), new IngredientStack(Ingredient.fromItem(Items.FLINT_AND_STEEL)));

			STORMWAY_RECIPE = GCTRecipeList.makeAndAddRecipe("stormway", new ItemStack(ItemRegistry.STORMWAY), new ItemStack(ItemRegistry.SHAPED_RADIANT_QUARTZ, 1), new IngredientStack("dyeYellow", 4), new IngredientStack(Ingredient.fromItem(Items.REDSTONE), 16), new IngredientStack(Blocks.IRON_BARS), 8);
		}

		/*MATRIX_CORE_RECIPE = GCTRecipeList.makeAndAddRecipe("matrix_core", new ItemStack(BlockRegistry.MATRIX_CRYSTAL_CORE, 1), new ItemStack(ItemRegistry.SHAPED_RADIANT_QUARTZ, 60), new IngredientStack("logWood", 12), new ItemStack(ItemRegistry.COMPONENT_SCINTILLATINGINLAY, 12), new IngredientStack(BlockRegistry.RADIANT_LANTERN, 4), new IngredientStack("bookshelf", 1));*/

		/*MATRIX_STORAGE_RECIPE = GCTRecipeList.makeAndAddRecipe("matrix_storage", new ItemStack(BlockRegistry.MATRIX_STORAGE, 1), new ItemStack(ItemRegistry.COMPONENT_MATRIXBRACE, 2), ItemRegistry.COMPONENT_MATERIALINTERFACE, new ItemStack(ItemRegistry.SHAPED_RADIANT_QUARTZ, 24));*/
	}

	@SubscribeEvent
	public static void onRegisterRecipes (Register<IRecipe> event) {
		/*if (Loader.isModLoaded("astralsorcery")) {
			Liquefaction.init();
		}*/
	}
}

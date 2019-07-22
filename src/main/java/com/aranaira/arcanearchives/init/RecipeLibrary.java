package com.aranaira.arcanearchives.init;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.api.GCTRecipeEvent;
import com.aranaira.arcanearchives.api.IGCTRecipe;
import com.aranaira.arcanearchives.config.ConfigHandler;
import com.aranaira.arcanearchives.data.ClientNetwork;
import com.aranaira.arcanearchives.data.HiveSaveData;
import com.aranaira.arcanearchives.data.HiveSaveData.Hive;
import com.aranaira.arcanearchives.data.DataHelper;
import com.aranaira.arcanearchives.integration.astralsorcery.Liquefaction;
import com.aranaira.arcanearchives.recipe.gct.GCTRecipe;
import com.aranaira.arcanearchives.recipe.gct.GCTRecipeList;
import com.aranaira.arcanearchives.recipe.IngredientStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreIngredient;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = ArcaneArchives.MODID)
public class RecipeLibrary {
	public static IGCTRecipe SHAPED_RADIANT_QUARTZ_RECIPE;
	public static IGCTRecipe MANIFEST_RECIPE;
	public static IGCTRecipe CONTAINMENT_FIELD_RECIPE;
	public static IGCTRecipe MATERIAL_INTERFACE_RECIPE;
	public static IGCTRecipe MATRIX_BRACE_RECIPE;
	public static IGCTRecipe RADIANT_DUST_RECIPE;
	public static IGCTRecipe SCINTILLATING_INLAY_RECIPE;
	public static IGCTRecipe DEVOURING_CHARM_RECIPE;
	/*public static IGCTRecipe MATRIX_CORE_RECIPE;
	public static IGCTRecipe MATRIX_STORAGE_RECIPE;
	public static IGCTRecipe MATRIX_REPOSITORY_RECIPE;*/
	public static IGCTRecipe GEM_SOCKET_RECIPE;
	public static IGCTRecipe RADIANT_LANTERN_RECIPE;
	public static IGCTRecipe RADIANT_TROVE_RECIPE;
	public static IGCTRecipe RADIANT_TANK_RECIPE;
	public static IGCTRecipe MONITORING_CRYSTAL_RECIPE;
	public static IGCTRecipe SCEPTER_MANIPULATION_RECIPE;
	public static IGCTRecipe RADIANT_AMPHORA_RECIPE;
	public static IGCTRecipe LETTER_OF_INVITATION_RECIPE;
	public static IGCTRecipe LETTER_OF_RESIGNATION_RECIPE;
	public static IGCTRecipe WRIT_OF_EXPULSION_RECIPE;
	public static IGCTRecipe BRAZIER_RECIPE;

	public static IGCTRecipe SLAUGHTERGLEAM_RECIPE;
	public static IGCTRecipe MURDERGLEAM_RECIPE;
	public static IGCTRecipe CLEANSEGLEAM_RECIPE;
	public static IGCTRecipe AGEGLEAM_RECIPE;
	public static IGCTRecipe SWITCHGLEAM_RECIPE;
	public static IGCTRecipe SALVEGLEAM_RECIPE;
	public static IGCTRecipe MUNCHSTONE_RECIPE;
	public static IGCTRecipe ORDERSTONE_RECIPE;
	public static IGCTRecipe MINDSPINDLE_RECIPE;
	public static IGCTRecipe ELIXIRSPINDLE_RECIPE;
	public static IGCTRecipe RIVERTEAR_RECIPE;
	public static IGCTRecipe MOUNTAINTEAR_RECIPE;
	public static IGCTRecipe PARCHTEAR_RECIPE;
	public static IGCTRecipe PHOENIXWAY_RECIPE;
	public static IGCTRecipe STORMWAY_RECIPE;

	public static void buildRecipes () {
		RADIANT_DUST_RECIPE = GCTRecipeList.instance.makeAndAddRecipe("radiant_dust", new ItemStack(ItemRegistry.COMPONENT_RADIANTDUST, 2), ItemRegistry.RAW_RADIANT_QUARTZ);

		SHAPED_RADIANT_QUARTZ_RECIPE = GCTRecipeList.instance.makeAndAddRecipe("shaped_quartz", new ItemStack(ItemRegistry.SHAPED_RADIANT_QUARTZ, 1), new ItemStack(ItemRegistry.RAW_RADIANT_QUARTZ, 2));

		MANIFEST_RECIPE = GCTRecipeList.instance.makeAndAddRecipe("manifest", new ItemStack(ItemRegistry.MANIFEST, 1), new IngredientStack("paper", 1), new IngredientStack("dyeBlack", 1), new ItemStack(ItemRegistry.COMPONENT_RADIANTDUST, 2));

		////////////////////

		LETTER_OF_INVITATION_RECIPE = GCTRecipeList.instance.makeAndAddRecipeWithCreatorAndCondition("letter_invitation", new ItemStack(ItemRegistry.LETTER_OF_INVITATION, 1), new IngredientStack("paper", 3), new ItemStack(ItemRegistry.COMPONENT_RADIANTDUST, 1), new IngredientStack("dyeLightBlue", 1)).addCondition((EntityPlayer player, TileEntity tile) -> {
			if (!player.world.isRemote) {
				HiveSaveData saveData = DataHelper.getHiveData(player.world);
				Hive hive = saveData.getHiveByMember(player.getUniqueID());
				return (hive == null || hive.owner.equals(player.getUniqueID()));
			} else {
				ClientNetwork network = DataHelper.getClientNetwork();
				return network.ownsHive() || !network.inHive();
			}
		});

		LETTER_OF_RESIGNATION_RECIPE = GCTRecipeList.instance.makeAndAddRecipeWithCreatorAndCondition("letter_resignation", new ItemStack(ItemRegistry.LETTER_OF_RESIGNATION, 1), new IngredientStack("paper", 3), new ItemStack(ItemRegistry.COMPONENT_RADIANTDUST, 1), new IngredientStack("dyePink", 1)).addCondition((EntityPlayer player, TileEntity tile) -> {
			if (!player.world.isRemote) {
				HiveSaveData saveData = DataHelper.getHiveData(player.world);
				Hive hive = saveData.getHiveByMember(player.getUniqueID());
				return hive != null;
			} else {
				ClientNetwork network = DataHelper.getClientNetwork();
				return network.inHive();
			}
		});

		WRIT_OF_EXPULSION_RECIPE = GCTRecipeList.instance.makeAndAddRecipeWithCreatorAndCondition("writ_expulsion", new ItemStack(ItemRegistry.WRIT_OF_EXPULSION, 1), new IngredientStack("paper", 3), new ItemStack(ItemRegistry.COMPONENT_RADIANTDUST, 1), new IngredientStack("dyeRed", 1)).addCondition((EntityPlayer player, TileEntity tile) -> {
			if (!player.world.isRemote) {
				HiveSaveData saveData = DataHelper.getHiveData(player.world);
				Hive hive = saveData.getHiveByMember(player.getUniqueID());
				return hive != null && hive.owner.equals(player.getUniqueID());
			} else {
				ClientNetwork network = DataHelper.getClientNetwork();
				return network.inHive() && network.ownsHive();
			}
		});

		//////

		SCEPTER_MANIPULATION_RECIPE = GCTRecipeList.instance.makeAndAddRecipe("scepter_manipulation", new ItemStack(ItemRegistry.SCEPTER_MANIPULATION), new ItemStack(ItemRegistry.SCEPTER_REVELATION, 1), new IngredientStack(ItemRegistry.COMPONENT_SCINTILLATINGINLAY, 1));

		RADIANT_LANTERN_RECIPE = GCTRecipeList.instance.makeAndAddRecipe("radiant_lantern", new ItemStack(BlockRegistry.RADIANT_LANTERN, 4), new ItemStack(ItemRegistry.RAW_RADIANT_QUARTZ, 2), new IngredientStack("nuggetGold", 1));

		RADIANT_TROVE_RECIPE = GCTRecipeList.instance.makeAndAddRecipe("radiant_trove", new ItemStack(BlockRegistry.RADIANT_TROVE, 4), new ItemStack(ItemRegistry.SHAPED_RADIANT_QUARTZ, 2), new ItemStack(ItemRegistry.COMPONENT_MATERIALINTERFACE), new ItemStack(BlockRegistry.RADIANT_CHEST));

		RADIANT_TANK_RECIPE = GCTRecipeList.instance.makeAndAddRecipe("radiant_tank", new ItemStack(BlockRegistry.RADIANT_TANK, 4), new ItemStack(ItemRegistry.SHAPED_RADIANT_QUARTZ, 2), new ItemStack(ItemRegistry.COMPONENT_CONTAINMENTFIELD), new IngredientStack("ingotGold", 1));

		MONITORING_CRYSTAL_RECIPE = GCTRecipeList.instance.makeAndAddRecipe("monitoring_crystal", new ItemStack(BlockRegistry.MONITORING_CRYSTAL), new ItemStack(ItemRegistry.SHAPED_RADIANT_QUARTZ, 1), new IngredientStack("nuggetGold", 2), new IngredientStack("stickWood", 4));

		SCINTILLATING_INLAY_RECIPE = GCTRecipeList.instance.makeAndAddRecipe("scintillating_inlay", new ItemStack(ItemRegistry.COMPONENT_SCINTILLATINGINLAY, 1), new ItemStack(ItemRegistry.COMPONENT_RADIANTDUST, 6), new IngredientStack("dustRedstone", 12), new IngredientStack("ingotGold"), new IngredientStack("nuggetGold", 6));

		BRAZIER_RECIPE = GCTRecipeList.instance.makeAndAddRecipe("brazier_of_hoarding", new ItemStack(BlockRegistry.BRAZIER_OF_HOARDING, 1), new ItemStack(ItemRegistry.COMPONENT_RADIANTDUST, 4), new IngredientStack(Items.COAL, 8), new IngredientStack("ingotGold", 2), new IngredientStack("logWood", 3));

		/*MATRIX_REPOSITORY_RECIPE = GCTRecipeList.instance.makeAndAddRecipe("matrix_repository", new ItemStack(BlockRegistry.MATRIX_REPOSITORY, 1), BlockRegistry.MATRIX_STORAGE, ItemRegistry.COMPONENT_MATERIALINTERFACE, ItemRegistry.COMPONENT_MATERIALINTERFACE);*/

		MATRIX_BRACE_RECIPE = GCTRecipeList.instance.makeAndAddRecipe("matrix_brace", new ItemStack(ItemRegistry.COMPONENT_MATRIXBRACE, 1), ItemRegistry.COMPONENT_SCINTILLATINGINLAY, new IngredientStack("ingotGold", 2));

		MATERIAL_INTERFACE_RECIPE = GCTRecipeList.instance.makeAndAddRecipe("material_interface", new ItemStack(ItemRegistry.COMPONENT_MATERIALINTERFACE, 1), ItemRegistry.COMPONENT_SCINTILLATINGINLAY, new IngredientStack("ingotGold"), ItemRegistry.SHAPED_RADIANT_QUARTZ);

		CONTAINMENT_FIELD_RECIPE = GCTRecipeList.instance.makeAndAddRecipe("containment_field", new ItemStack(ItemRegistry.COMPONENT_CONTAINMENTFIELD, 1), ItemRegistry.COMPONENT_SCINTILLATINGINLAY, new IngredientStack("ingotGold", 2), new ItemStack(ItemRegistry.SHAPED_RADIANT_QUARTZ, 2));

		DEVOURING_CHARM_RECIPE = GCTRecipeList.instance.makeAndAddRecipe("devouring_charm", new ItemStack(ItemRegistry.DEVOURING_CHARM, 4), new IngredientStack("ingotGold", 1), new IngredientStack(Blocks.OBSIDIAN, 2), new IngredientStack(Items.FLINT_AND_STEEL, 1));

		RADIANT_AMPHORA_RECIPE = GCTRecipeList.instance.makeAndAddRecipe("radiant_amphora", new ItemStack(ItemRegistry.RADIANT_AMPHORA), new ItemStack(ItemRegistry.COMPONENT_RADIANTDUST, 4), new ItemStack(Items.CLAY_BALL, 4), new ItemStack(Items.GOLD_NUGGET, 4));

		if (ConfigHandler.ArsenalConfig.EnableArsenal) {
			GEM_SOCKET_RECIPE = GCTRecipeList.instance.makeAndAddRecipe("gemsocket", new ItemStack(ItemRegistry.BAUBLE_GEMSOCKET), new ItemStack(ItemRegistry.COMPONENT_SCINTILLATINGINLAY, 1), new ItemStack(Items.GOLD_NUGGET, 4), new ItemStack(Items.BOWL, 1), new ItemStack(Items.LEATHER, 1));

			MURDERGLEAM_RECIPE = GCTRecipeList.instance.makeAndAddRecipe("murdergleam", new ItemStack(ItemRegistry.MURDERGLEAM), new ItemStack(ItemRegistry.SHAPED_RADIANT_QUARTZ, 1), new IngredientStack("dyeYellow", 4), new IngredientStack(Items.DIAMOND_SWORD), new IngredientStack(Ingredient.fromItem(Items.BLAZE_POWDER), 2));

			SLAUGHTERGLEAM_RECIPE = GCTRecipeList.instance.makeAndAddRecipe("slaughtergleam", new ItemStack(ItemRegistry.SLAUGHTERGLEAM), new ItemStack(ItemRegistry.SHAPED_RADIANT_QUARTZ, 1), new IngredientStack("dyeRed", 4), new IngredientStack(Items.DIAMOND), new IngredientStack(Items.GOLD_INGOT, 2), new IngredientStack(Blocks.LAPIS_BLOCK, 2));

			AGEGLEAM_RECIPE = GCTRecipeList.instance.makeAndAddRecipe("agegleam", new ItemStack(ItemRegistry.AGEGLEAM), new ItemStack(ItemRegistry.SHAPED_RADIANT_QUARTZ, 1), new IngredientStack("dyeGreen", 4), new IngredientStack(Items.WHEAT, 9), new IngredientStack(Items.CARROT, 9), new IngredientStack(Items.WHEAT_SEEDS, 9));

			CLEANSEGLEAM_RECIPE = GCTRecipeList.instance.makeAndAddRecipe("cleansegleam", new ItemStack(ItemRegistry.CLEANSEGLEAM), new ItemStack(ItemRegistry.SHAPED_RADIANT_QUARTZ, 1), new IngredientStack("dyeBlue", 4), new IngredientStack(Items.POISONOUS_POTATO), new IngredientStack(Ingredient.fromItem(Items.MILK_BUCKET), 1));

			SWITCHGLEAM_RECIPE = GCTRecipeList.instance.makeAndAddRecipe("switchgleam", new ItemStack(ItemRegistry.SWITCHGLEAM), new ItemStack(ItemRegistry.SHAPED_RADIANT_QUARTZ, 1), new IngredientStack("dyePurple", 4), new IngredientStack(Blocks.TRIPWIRE_HOOK), new IngredientStack(Items.ENDER_PEARL));

			SALVEGLEAM_RECIPE = GCTRecipeList.instance.makeAndAddRecipe("salvegleam", new ItemStack(ItemRegistry.SALVEGLEAM), new ItemStack(ItemRegistry.SHAPED_RADIANT_QUARTZ, 1), new IngredientStack("dyePink", 4), new IngredientStack(Items.GOLDEN_APPLE), new IngredientStack(Items.SPECKLED_MELON), new IngredientStack(Items.GOLDEN_CARROT));

			MUNCHSTONE_RECIPE = GCTRecipeList.instance.makeAndAddRecipe("munchstone", new ItemStack(ItemRegistry.MUNCHSTONE), new ItemStack(ItemRegistry.SHAPED_RADIANT_QUARTZ, 1), new IngredientStack("dyeBlack", 4), new IngredientStack(Items.CAKE), new IngredientStack(Ingredient.fromItem(Items.SPECKLED_MELON), 2));

			ORDERSTONE_RECIPE = GCTRecipeList.instance.makeAndAddRecipe("orderstone", new ItemStack(ItemRegistry.ORDERSTONE), new ItemStack(ItemRegistry.SHAPED_RADIANT_QUARTZ, 1), new IngredientStack("dyePink", 4), new IngredientStack(Blocks.ANVIL), new IngredientStack(Ingredient.fromItem(Items.SIGN), 30));

			MINDSPINDLE_RECIPE = GCTRecipeList.instance.makeAndAddRecipe("mindspindle", new ItemStack(ItemRegistry.MINDSPINDLE), new ItemStack(ItemRegistry.SHAPED_RADIANT_QUARTZ, 1), new IngredientStack("dyeGreen", 4), new IngredientStack(Items.ENCHANTED_BOOK), new IngredientStack(Ingredient.fromItem(Items.EMERALD), 2));

			ELIXIRSPINDLE_RECIPE = GCTRecipeList.instance.makeAndAddRecipe("elixirspindle", new ItemStack(ItemRegistry.ELIXIRSPINDLE), new ItemStack(ItemRegistry.SHAPED_RADIANT_QUARTZ, 1), new IngredientStack("dyePurple", 4), new IngredientStack(Items.BREWING_STAND), new IngredientStack(Items.EMERALD, 2), new IngredientStack(Items.REDSTONE, 4), new IngredientStack(Items.GLOWSTONE_DUST, 4));

			RIVERTEAR_RECIPE = GCTRecipeList.instance.makeAndAddRecipe("rivertear", new ItemStack(ItemRegistry.RIVERTEAR), new ItemStack(ItemRegistry.SHAPED_RADIANT_QUARTZ, 1), new IngredientStack(Ingredient.fromItem(Items.SUGAR), 4), new IngredientStack(Ingredient.fromItem(Items.WATER_BUCKET)), new IngredientStack(new OreIngredient("dyeBlue"), 4));

			MOUNTAINTEAR_RECIPE = GCTRecipeList.instance.makeAndAddRecipe("mountaintear", new ItemStack(ItemRegistry.MOUNTAINTEAR), new ItemStack(ItemRegistry.SHAPED_RADIANT_QUARTZ, 1), new IngredientStack("dyeOrange", 4), new IngredientStack(Blocks.MAGMA, 4), new IngredientStack(Ingredient.fromItem(Items.LAVA_BUCKET)));

			PARCHTEAR_RECIPE = GCTRecipeList.instance.makeAndAddRecipe("parchtear", new ItemStack(ItemRegistry.PARCHTEAR), new ItemStack(ItemRegistry.SHAPED_RADIANT_QUARTZ, 1), new IngredientStack("dyeBlack", 4), new IngredientStack(Blocks.HARDENED_CLAY, 4), new IngredientStack(Blocks.DEADBUSH, 4));

			PHOENIXWAY_RECIPE = GCTRecipeList.instance.makeAndAddRecipe("phoenixway", new ItemStack(ItemRegistry.PHOENIXWAY), new ItemStack(ItemRegistry.SHAPED_RADIANT_QUARTZ, 1), new IngredientStack(Ingredient.fromItem(Items.BLAZE_POWDER), 4), new IngredientStack(Ingredient.fromItem(Items.COAL), 4), new IngredientStack(Ingredient.fromItem(Items.FLINT_AND_STEEL)));

			STORMWAY_RECIPE = GCTRecipeList.instance.makeAndAddRecipe("stormway", new ItemStack(ItemRegistry.STORMWAY), new ItemStack(ItemRegistry.SHAPED_RADIANT_QUARTZ, 1), new IngredientStack("dyeYellow", 4), new IngredientStack(Ingredient.fromItem(Items.REDSTONE), 16), new IngredientStack(Blocks.IRON_BARS, 8));
		}

		/*MATRIX_CORE_RECIPE = GCTRecipeList.instance.makeAndAddRecipe("matrix_core", new ItemStack(BlockRegistry.MATRIX_CRYSTAL_CORE, 1), new ItemStack(ItemRegistry.SHAPED_RADIANT_QUARTZ, 60), new IngredientStack("logWood", 12), new ItemStack(ItemRegistry.COMPONENT_SCINTILLATINGINLAY, 12), new IngredientStack(BlockRegistry.RADIANT_LANTERN, 4), new IngredientStack("bookshelf", 1));*/

		/*MATRIX_STORAGE_RECIPE = GCTRecipeList.instance.makeAndAddRecipe("matrix_storage", new ItemStack(BlockRegistry.MATRIX_STORAGE, 1), new ItemStack(ItemRegistry.COMPONENT_MATRIXBRACE, 2), ItemRegistry.COMPONENT_MATERIALINTERFACE, new ItemStack(ItemRegistry.SHAPED_RADIANT_QUARTZ, 24));*/

		GCTRecipeEvent event = new GCTRecipeEvent(GCTRecipeList.instance);
		MinecraftForge.EVENT_BUS.post(event);
	}

	@SubscribeEvent
	public static void integrationEventAS (Register<IRecipe> event) {
		if (Loader.isModLoaded("astralsorcery")) {
			Liquefaction.init();
		}
	}
}

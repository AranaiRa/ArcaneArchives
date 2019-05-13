package com.aranaira.arcanearchives.init;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.blocks.*;
import com.aranaira.arcanearchives.blocks.templates.BlockTemplate;
import com.aranaira.arcanearchives.items.itemblocks.MonitoringCrystalItem;
import com.aranaira.arcanearchives.items.itemblocks.RadiantTankItem;
import com.aranaira.arcanearchives.items.templates.ItemBlockTemplate;
import com.aranaira.arcanearchives.tileentities.*;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Arrays;

@SuppressWarnings("WeakerAccess")
@Mod.EventBusSubscriber(modid = ArcaneArchives.MODID)
public class BlockRegistry
{

	//Matrices
	/*public static final MatrixCrystalCore MATRIX_CRYSTAL_CORE = new MatrixCrystalCore();
	public static final MatrixRepository MATRIX_REPOSITORY = new MatrixRepository();
	public static final MatrixReservoir MATRIX_RESERVOIR = new MatrixReservoir();
	public static final MatrixStorage MATRIX_STORAGE = new MatrixStorage();
	public static final MatrixDistillate MATRIX_DISTILLATE = new MatrixDistillate(); //TODO: Check if Thaumcraft is loaded*/

	//Blocks
	public static final StorageRawQuartz STORAGE_RAW_QUARTZ = new StorageRawQuartz();
	public static final StorageCutQuartz STORAGE_CUT_QUARTZ = new StorageCutQuartz();
	public static final RadiantChest RADIANT_CHEST = new RadiantChest();
	public static final RadiantTrove RADIANT_TROVE = new RadiantTrove();
	public static final RadiantCraftingTable RADIANT_CRAFTING_TABLE = new RadiantCraftingTable();
	public static final RadiantLantern RADIANT_LANTERN = new RadiantLantern();
	public static final RadiantResonator RADIANT_RESONATOR = new RadiantResonator();
	public static final RawQuartz RAW_QUARTZ = new RawQuartz();
	//public static final DominionCrystal DOMINION_CRYSTAL = new DominionCrystal();
	public static final GemCuttersTable GEMCUTTERS_TABLE = new GemCuttersTable();
	public static final MonitoringCrystal MONITORING_CRYSTAL = new MonitoringCrystal();
	public static final AccessorBlock ACCESSOR = new AccessorBlock();
	public static final RadiantTank RADIANT_TANK = new RadiantTank();
	public static final Brazier BRAZIER_OF_HOARDING = new Brazier();

	// Tiles
	public static final RadiantResonatorTileEntity RADIANT_RESONATOR_TILE_ENTITY = new RadiantResonatorTileEntity();
	/*public static final MatrixCoreTileEntity MATRIX_CORE_TILE_ENTITY = new MatrixCoreTileEntity();
	public static final MatrixRepositoryTileEntity MATRIX_REPOSITORY_TILE_ENTITY = new MatrixRepositoryTileEntity();*/
	public static final AccessorTileEntity ACCESSOR_TILE_ENTITY = new AccessorTileEntity();
	public static final RadiantChestTileEntity RADIANT_CHEST_TILE_ENTITY = new RadiantChestTileEntity();
	public static final RadiantTroveTileEntity RADIANT_TROVE_TILE_ENTITY = new RadiantTroveTileEntity();
	public static final GemCuttersTableTileEntity GEMCUTTERS_TABLE_TILE_ENTITY = new GemCuttersTableTileEntity();
	public static final RadiantCraftingTableTileEntity RADIANT_CRAFTING_TABLE_TILE_ENTITY = new RadiantCraftingTableTileEntity();
	//public static final MatrixStorageTileEntity MATRIX_STORAGE_TILE_ENTITY = new MatrixStorageTileEntity();
	public static final MonitoringCrystalTileEntity MONITORING_CRYSTAL_TILE_ENTITY = new MonitoringCrystalTileEntity();
	public static final RadiantTankTileEntity RADIANT_TANK_TILE_ENTITY = new RadiantTankTileEntity();
	public static final BrazierTileEntity BRAZIER_OF_HOARDING_TILE_ENTITY = new BrazierTileEntity();

	@SubscribeEvent
	public static void onBlockRegister(RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();

		//MATRIX_CRYSTAL_CORE.setItemBlock(new ItemBlockTemplate(MATRIX_CRYSTAL_CORE));
		//MATRIX_REPOSITORY.setItemBlock(new ItemBlockTemplate(MATRIX_REPOSITORY));
		//MATRIX_RESERVOIR.setItemBlock(new ItemBlockTemplate(MATRIX_RESERVOIR));
		//MATRIX_STORAGE.setItemBlock(new ItemBlockTemplate(MATRIX_STORAGE));
		//MATRIX_DISTILLATE.setItemBlock(new ItemBlockTemplate(MATRIX_DISTILLATE));
		STORAGE_RAW_QUARTZ.setItemBlock(new ItemBlock(STORAGE_RAW_QUARTZ));
		STORAGE_CUT_QUARTZ.setItemBlock(new ItemBlock(STORAGE_CUT_QUARTZ) {
			@Override
			public EnumRarity getRarity(ItemStack stack) {
				return EnumRarity.UNCOMMON;
			}
		});
		RADIANT_CHEST.setItemBlock(new ItemBlockTemplate(RADIANT_CHEST));
		RADIANT_CRAFTING_TABLE.setItemBlock(new ItemBlock(RADIANT_CRAFTING_TABLE) {
			@Override
			public EnumRarity getRarity(ItemStack stack) {
				return EnumRarity.RARE;
			}
		});
		RADIANT_LANTERN.setItemBlock(new ItemBlock(RADIANT_LANTERN) {
			@Override
			public EnumRarity getRarity(ItemStack stack) {
				return EnumRarity.UNCOMMON;
			}
		});
		RADIANT_RESONATOR.setItemBlock(new ItemBlockTemplate(RADIANT_RESONATOR));
		RAW_QUARTZ.setItemBlock(new ItemBlock(RAW_QUARTZ));
		//DOMINION_CRYSTAL.setItemBlock(new ItemBlockTemplate(DOMINION_CRYSTAL));
		GEMCUTTERS_TABLE.setItemBlock(new ItemBlockTemplate(GEMCUTTERS_TABLE));
		BRAZIER_OF_HOARDING.setItemBlock(new ItemBlockTemplate(BRAZIER_OF_HOARDING));
		RADIANT_TROVE.setItemBlock(new ItemBlockTemplate(RADIANT_TROVE));
		MONITORING_CRYSTAL.setItemBlock(new MonitoringCrystalItem(MONITORING_CRYSTAL));
		// TODO: Fix this
		RADIANT_TANK.setItemBlock(new RadiantTankItem(RADIANT_TANK));

		registry.registerAll(/*MATRIX_CRYSTAL_CORE, MATRIX_REPOSITORY, MATRIX_RESERVOIR, MATRIX_STORAGE, MATRIX_DISTILLATE*/STORAGE_RAW_QUARTZ, STORAGE_CUT_QUARTZ, RADIANT_CHEST, RADIANT_CRAFTING_TABLE, RADIANT_LANTERN, RADIANT_RESONATOR, RAW_QUARTZ/*DOMINION_CRYSTAL*/, GEMCUTTERS_TABLE, ACCESSOR, RADIANT_TROVE, MONITORING_CRYSTAL, RADIANT_TANK, BRAZIER_OF_HOARDING);
	}

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		// ACCESSOR doesn't get registered.

		Arrays.asList(/*MATRIX_CRYSTAL_CORE, MATRIX_REPOSITORY, MATRIX_RESERVOIR, MATRIX_STORAGE, MATRIX_DISTILLATE*/STORAGE_RAW_QUARTZ, STORAGE_CUT_QUARTZ, RADIANT_CHEST, RADIANT_CRAFTING_TABLE, RADIANT_LANTERN, RADIANT_RESONATOR, RAW_QUARTZ/*DOMINION_CRYSTAL*/, GEMCUTTERS_TABLE, ACCESSOR, RADIANT_TROVE, MONITORING_CRYSTAL, RADIANT_TANK, BRAZIER_OF_HOARDING).forEach(BlockTemplate::registerModels);

		Arrays.asList(STORAGE_RAW_QUARTZ, STORAGE_CUT_QUARTZ).forEach((block) -> {
			ItemBlock itemBlock = block.getItemBlock();
			if(itemBlock != null) {
				ModelLoader.setCustomModelResourceLocation(itemBlock, 0, new ModelResourceLocation(block.getRegistryName(), "inventory"));
			}
		});
	}

	public static void registerTileEntities() {
		Arrays.asList(RADIANT_RESONATOR_TILE_ENTITY, /*MATRIX_CORE_TILE_ENTITY, MATRIX_REPOSITORY_TILE_ENTITY, */ ACCESSOR_TILE_ENTITY, RADIANT_CHEST_TILE_ENTITY, GEMCUTTERS_TABLE_TILE_ENTITY, RADIANT_CRAFTING_TABLE_TILE_ENTITY/*, MATRIX_STORAGE_TILE_ENTITY*/, RADIANT_TROVE_TILE_ENTITY, MONITORING_CRYSTAL_TILE_ENTITY, RADIANT_TANK_TILE_ENTITY, BRAZIER_OF_HOARDING_TILE_ENTITY).forEach((tile) -> {
			GameRegistry.registerTileEntity(tile.getClass(), new ResourceLocation(ArcaneArchives.MODID, tile.getName()));
			ArcaneArchives.logger.info(String.format("Registered tile entity: %s", tile.getName()));
		});
	}
}

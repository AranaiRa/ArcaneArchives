package com.aranaira.arcanearchives.init;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.blocks.templates.BlockTemplate;
import com.aranaira.arcanearchives.items.*;
import com.aranaira.arcanearchives.items.templates.ItemTemplate;
import com.aranaira.arcanearchives.util.IHasModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Arrays;
import java.util.stream.Stream;

@Mod.EventBusSubscriber(modid = ArcaneArchives.MODID)
public class ItemRegistry
{
	public static final RawQuartzItem RAW_RADIANT_QUARTZ = new RawQuartzItem();
	public static final CutQuartzItem CUT_RADIANT_QUARTZ = new CutQuartzItem();
	public static final GeomancyPendulumItem GEOMANCY_PENDULUM = new GeomancyPendulumItem();
	public static final GeomanticMapItem GEOMANTIC_MAP = new GeomanticMapItem();
	public static final ManifestItem MANIFEST = new ManifestItem();
	public static final RadiantBucketItem RADIANT_BUCKET = new RadiantBucketItem();
	public static final ScepterAbductionItem SCEPTER_ABDUCTION = new ScepterAbductionItem();
	public static final ScepterManipulationItem SCEPTER_MANIPULATION = new ScepterManipulationItem();
	public static final ScepterTranslocationItem SCEPTER_TRANSLOCATION = new ScepterTranslocationItem();
	public static final TomeOfArcanaItem TOME_OF_ARCANA = new TomeOfArcanaItem();
	public static final TomeOfRequisitionItem TOME_OF_REQUISITION = new TomeOfRequisitionItem();

	//CRAFTING COMPONENTS
	public static final ComponentContainmentFieldItem COMPONENT_CONTAINMENTFIELD = new ComponentContainmentFieldItem();
	public static final ComponentMatrixBraceItem COMPONENT_MATRIXBRACE = new ComponentMatrixBraceItem();
	public static final ComponentMaterialInterfaceItem COMPONENT_MATERIALINTERFACE = new ComponentMaterialInterfaceItem();
	public static final ComponentRadiantDustItem COMPONENT_RADIANTDUST = new ComponentRadiantDustItem();
	public static final ComponentScintillatingInlayItem COMPONENT_SCINTILLATINGINLAY = new ComponentScintillatingInlayItem();

	//SPIRIT SPHERES
	public static final SpiritOrbItem SPIRIT_ORB = new SpiritOrbItem();

	@SubscribeEvent
	public static void onItemRegister(RegistryEvent.Register<Item> event)
	{
		IForgeRegistry<Item> registry = event.getRegistry();

		Arrays.asList(RAW_RADIANT_QUARTZ, CUT_RADIANT_QUARTZ, GEOMANCY_PENDULUM, GEOMANTIC_MAP, MANIFEST, RADIANT_BUCKET, SCEPTER_ABDUCTION, SCEPTER_MANIPULATION, SCEPTER_TRANSLOCATION, TOME_OF_ARCANA, TOME_OF_REQUISITION, COMPONENT_CONTAINMENTFIELD, COMPONENT_MATRIXBRACE, COMPONENT_MATERIALINTERFACE, COMPONENT_RADIANTDUST, COMPONENT_SCINTILLATINGINLAY, SPIRIT_ORB).forEach(registry::register);

		Stream.of(BlockRegistry.MATRIX_CRYSTAL_CORE, BlockRegistry.MATRIX_REPOSITORY, BlockRegistry.MATRIX_RESERVOIR, BlockRegistry.MATRIX_STORAGE, BlockRegistry.MATRIX_DISTILLATE, BlockRegistry.STORAGE_RAW_QUARTZ, BlockRegistry.STORAGE_CUT_QUARTZ, BlockRegistry.RADIANT_CHEST, BlockRegistry.RADIANT_CRAFTING_TABLE, BlockRegistry.RADIANT_LANTERN, BlockRegistry.RADIANT_RESONATOR, BlockRegistry.RAW_QUARTZ, BlockRegistry.DOMINION_CRYSTAL, BlockRegistry.GEMCUTTERS_TABLE).map(BlockTemplate::getItemBlock).forEach((block) -> {
			registry.register(block);
			ModelLoader.setCustomModelResourceLocation(block, 0, new ModelResourceLocation(block.getRegistryName(), "inventory"));
		});
	}

	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent event)
	{
		Arrays.asList(RAW_RADIANT_QUARTZ, CUT_RADIANT_QUARTZ, GEOMANCY_PENDULUM, GEOMANTIC_MAP, MANIFEST, RADIANT_BUCKET, SCEPTER_ABDUCTION, SCEPTER_MANIPULATION, SCEPTER_TRANSLOCATION, TOME_OF_ARCANA, TOME_OF_REQUISITION, COMPONENT_CONTAINMENTFIELD, COMPONENT_MATRIXBRACE, COMPONENT_MATERIALINTERFACE, COMPONENT_RADIANTDUST, COMPONENT_SCINTILLATINGINLAY).forEach(ItemTemplate::registerModels);

		SPIRIT_ORB.registerModels();
	}

	@SubscribeEvent
	public static void missingMappings (RegistryEvent.MissingMappings<Item> event) {
		for (RegistryEvent.MissingMappings.Mapping<Item> mapping : event.getMappings()) {
			ResourceLocation missingResource = mapping.key;
			if (missingResource.getNamespace().equals(ArcaneArchives.MODID) && missingResource.getPath().equals("accessorblock")) {
				mapping.remap(Items.AIR);
			}
		}
	}
}

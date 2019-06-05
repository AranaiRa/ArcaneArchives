package com.aranaira.arcanearchives.init;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.blocks.templates.BlockTemplate;
import com.aranaira.arcanearchives.items.*;
import com.aranaira.arcanearchives.items.gems.pampel.*;
import com.aranaira.arcanearchives.items.gems.pendeloque.*;
import com.aranaira.arcanearchives.items.templates.ItemTemplate;
import com.aranaira.arcanearchives.items.unused.TomeOfRequisitionItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Arrays;
import java.util.stream.Stream;

@Mod.EventBusSubscriber(modid = ArcaneArchives.MODID)
public class ItemRegistry {
	public static final RawQuartzItem RAW_RADIANT_QUARTZ = new RawQuartzItem();
	public static final CutQuartzItem CUT_RADIANT_QUARTZ = new CutQuartzItem();
	//public static final GeomancyPendulumItem GEOMANCY_PENDULUM = new GeomancyPendulumItem();
	//public static final GeomanticMapItem GEOMANTIC_MAP = new GeomanticMapItem();
	public static final ManifestItem MANIFEST = new ManifestItem();
	public static final RadiantAmphoraItem RADIANT_AMPHORA = new RadiantAmphoraItem();
	//public static final ScepterAbductionItem SCEPTER_ABDUCTION = new ScepterAbductionItem();
	//public static final ScepterTranslocationItem SCEPTER_TRANSLOCATION = new ScepterTranslocationItem();
	public static final ScepterRevelationItem SCEPTER_REVELATION = new ScepterRevelationItem();
	public static final ScepterManipulationItem SCEPTER_MANIPULATION = new ScepterManipulationItem();
	public static final TomeOfArcanaItem TOME_OF_ARCANA = new TomeOfArcanaItem();
	public static final TomeOfRequisitionItem TOME_OF_REQUISITION = new TomeOfRequisitionItem();
	public static final LetterOfInvitationItem LETTER_OF_INVITATION = new LetterOfInvitationItem();
	public static final LetterOfResignationItem LETTER_OF_RESIGNATION = new LetterOfResignationItem();
	public static final WritOfExpulsionItem WRIT_OF_EXPULSION = new WritOfExpulsionItem();

	//CRAFTING COMPONENTS
	public static final ComponentContainmentFieldItem COMPONENT_CONTAINMENTFIELD = new ComponentContainmentFieldItem();
	public static final ComponentMatrixBraceItem COMPONENT_MATRIXBRACE = new ComponentMatrixBraceItem();
	public static final ComponentMaterialInterfaceItem COMPONENT_MATERIALINTERFACE = new ComponentMaterialInterfaceItem();
	public static final ComponentRadiantDustItem COMPONENT_RADIANTDUST = new ComponentRadiantDustItem();
	public static final ComponentScintillatingInlayItem COMPONENT_SCINTILLATINGINLAY = new ComponentScintillatingInlayItem();

	//ARCANE GEMS
	public static final FabrialItem FABRIAL = new FabrialItem();
	public static final MindspindleItem MINDSPINDLE = new MindspindleItem();
	public static final MountaintearItem MOUNTAINTEAR = new MountaintearItem();
	public static final RivertearItem RIVERTEAR = new RivertearItem();

	//BAUBLES
	public static final BaubleGemSocket BAUBLE_GEMSOCKET = new BaubleGemSocket();

	//SPIRIT SPHERES
	//public static final SpiritOrbItem SPIRIT_ORB = new SpiritOrbItem();

	@SubscribeEvent
	public static void onItemRegister (RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();


		Arrays.asList(RAW_RADIANT_QUARTZ, CUT_RADIANT_QUARTZ, /*GEOMANCY_PENDULUM, GEOMANTIC_MAP,*/ MANIFEST, RADIANT_AMPHORA, /*SCEPTER_ABDUCTION, SCEPTER_TRANSLOCATION, */TOME_OF_ARCANA, LETTER_OF_INVITATION, LETTER_OF_RESIGNATION, WRIT_OF_EXPULSION,/* TOME_OF_REQUISITION,*/ COMPONENT_CONTAINMENTFIELD, COMPONENT_MATRIXBRACE, COMPONENT_MATERIALINTERFACE, COMPONENT_RADIANTDUST, COMPONENT_SCINTILLATINGINLAY/*, SPIRIT_ORB*/, SCEPTER_REVELATION, SCEPTER_MANIPULATION, BAUBLE_GEMSOCKET, /*FABRIAL, HOMINGSTONE, MUNCHSTONE, GLYPHSPINDLE,*/MINDSPINDLE, MOUNTAINTEAR, RIVERTEAR/*, PYREWAY, EARTHWAY, NIGHTWAY*/).forEach(registry::register);
		Stream.of(/*BlockRegistry.MATRIX_CRYSTAL_CORE, BlockRegistry.MATRIX_REPOSITORY, BlockRegistry.MATRIX_RESERVOIR, BlockRegistry.MATRIX_STORAGE, BlockRegistry.MATRIX_DISTILLATE, */BlockRegistry.QUARTZ_SLIVER, BlockRegistry.STORAGE_RAW_QUARTZ, BlockRegistry.STORAGE_CUT_QUARTZ, BlockRegistry.RADIANT_CHEST, BlockRegistry.RADIANT_CRAFTING_TABLE, BlockRegistry.RADIANT_LANTERN, BlockRegistry.RADIANT_RESONATOR, BlockRegistry.RAW_QUARTZ/*, BlockRegistry.DOMINION_CRYSTAL*/, BlockRegistry.LECTERN_MANIFEST, BlockRegistry.GEMCUTTERS_TABLE, BlockRegistry.RADIANT_TROVE, BlockRegistry.MONITORING_CRYSTAL, BlockRegistry.RADIANT_TANK, BlockRegistry.BRAZIER_OF_HOARDING).map(BlockTemplate::getItemBlock).forEach(registry::register);
	}

	@SubscribeEvent
	public static void onModelRegister (ModelRegistryEvent event) {
		Arrays.asList(RAW_RADIANT_QUARTZ, CUT_RADIANT_QUARTZ/*, GEOMANCY_PENDULUM, GEOMANTIC_MAP*/, MANIFEST, RADIANT_AMPHORA, /*SCEPTER_ABDUCTION, SCEPTER_MANIPULATION, SCEPTER_TRANSLOCATION*/ LETTER_OF_INVITATION, LETTER_OF_RESIGNATION, WRIT_OF_EXPULSION, /*TOME_OF_REQUISITION, */COMPONENT_CONTAINMENTFIELD, COMPONENT_MATRIXBRACE, COMPONENT_MATERIALINTERFACE, COMPONENT_RADIANTDUST, COMPONENT_SCINTILLATINGINLAY/*, SPIRIT_ORB*/, SCEPTER_REVELATION, SCEPTER_MANIPULATION, BAUBLE_GEMSOCKET, /*FABRIAL, HOMINGSTONE, MUNCHSTONE, GLYPHSPINDLE,*/MINDSPINDLE, MOUNTAINTEAR, RIVERTEAR/*, PYREWAY, EARTHWAY, NIGHTWAY*/).forEach(ItemTemplate::registerModels);

		Stream.of(/*BlockRegistry.MATRIX_CRYSTAL_CORE, BlockRegistry.MATRIX_REPOSITORY, BlockRegistry.MATRIX_RESERVOIR, BlockRegistry.MATRIX_STORAGE, BlockRegistry.MATRIX_DISTILLATE, */BlockRegistry.STORAGE_RAW_QUARTZ, BlockRegistry.STORAGE_CUT_QUARTZ, BlockRegistry.RADIANT_CHEST, BlockRegistry.RADIANT_CRAFTING_TABLE, BlockRegistry.RADIANT_LANTERN, BlockRegistry.RADIANT_RESONATOR, BlockRegistry.RAW_QUARTZ, /*BlockRegistry.DOMINION_CRYSTAL, */BlockRegistry.LECTERN_MANIFEST, BlockRegistry.GEMCUTTERS_TABLE, BlockRegistry.RADIANT_TROVE, BlockRegistry.MONITORING_CRYSTAL, BlockRegistry.BRAZIER_OF_HOARDING).map(BlockTemplate::getItemBlock).forEach((block) -> ModelLoader.setCustomModelResourceLocation(block, 0, new ModelResourceLocation(block.getRegistryName(), "inventory")));

		// has to be a bit special because it's a ItemGuidebook
		TOME_OF_ARCANA.registerModels();

		/*SPIRIT_ORB.registerModels();*/
	}

	@SubscribeEvent
	public static void missingMappings (RegistryEvent.MissingMappings<Item> event) {
	}
}

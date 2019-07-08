package com.aranaira.arcanearchives.init;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.blocks.templates.BlockTemplate;
import com.aranaira.arcanearchives.items.*;
import com.aranaira.arcanearchives.items.gems.GemRechargePowder;
import com.aranaira.arcanearchives.items.gems.GemRechargePowderRainbow;
import com.aranaira.arcanearchives.items.gems.asscher.*;
import com.aranaira.arcanearchives.items.gems.oval.MunchstoneItem;
import com.aranaira.arcanearchives.items.gems.oval.OrderstoneItem;
import com.aranaira.arcanearchives.items.gems.oval.TransferstoneItem;
import com.aranaira.arcanearchives.items.gems.pampel.Elixirspindle;
import com.aranaira.arcanearchives.items.gems.pampel.MindspindleItem;
import com.aranaira.arcanearchives.items.gems.pendeloque.MountaintearItem;
import com.aranaira.arcanearchives.items.gems.pendeloque.ParchtearItem;
import com.aranaira.arcanearchives.items.gems.pendeloque.RivertearItem;
import com.aranaira.arcanearchives.items.gems.trillion.PhoenixwayItem;
import com.aranaira.arcanearchives.items.gems.trillion.StormwayItem;
import com.aranaira.arcanearchives.items.templates.ItemTemplate;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Mod.EventBusSubscriber(modid = ArcaneArchives.MODID)
public class ItemRegistry {
	public static final RawQuartzItem RAW_RADIANT_QUARTZ = new RawQuartzItem();
	public static final ShapedQuartzItem SHAPED_RADIANT_QUARTZ = new ShapedQuartzItem();
	public static final ManifestItem MANIFEST = new ManifestItem();
	public static final RadiantAmphoraItem RADIANT_AMPHORA = new RadiantAmphoraItem();
	public static final ScepterRevelationItem SCEPTER_REVELATION = new ScepterRevelationItem();
	public static final ScepterManipulationItem SCEPTER_MANIPULATION = new ScepterManipulationItem();
	public static final TomeOfArcanaItem TOME_OF_ARCANA = new TomeOfArcanaItem();
	public static final LetterOfInvitationItem LETTER_OF_INVITATION = new LetterOfInvitationItem();
	public static final LetterOfResignationItem LETTER_OF_RESIGNATION = new LetterOfResignationItem();
	public static final WritOfExpulsionItem WRIT_OF_EXPULSION = new WritOfExpulsionItem();

	//public static final GeomancyPendulumItem GEOMANCY_PENDULUM = new GeomancyPendulumItem();
	//public static final GeomanticMapItem GEOMANTIC_MAP = new GeomanticMapItem();
	//public static final ScepterAbductionItem SCEPTER_ABDUCTION = new ScepterAbductionItem();
	//public static final ScepterTranslocationItem SCEPTER_TRANSLOCATION = new ScepterTranslocationItem();
	//public static final TomeOfRequisitionItem TOME_OF_REQUISITION = new TomeOfRequisitionItem();

	//CRAFTING COMPONENTS
	public static final ContainmentFieldItem COMPONENT_CONTAINMENTFIELD = new ContainmentFieldItem();
	public static final MatrixBraceItem COMPONENT_MATRIXBRACE = new MatrixBraceItem();
	public static final MaterialInterfaceItem COMPONENT_MATERIALINTERFACE = new MaterialInterfaceItem();
	public static final RadiantDustItem COMPONENT_RADIANTDUST = new RadiantDustItem();
	public static final ScintillatingInlayItem COMPONENT_SCINTILLATINGINLAY = new ScintillatingInlayItem();

	//DEVICE UPGRADES
	public static final DevouringCharmItem DEVOURING_CHARM = new DevouringCharmItem();

	//ARCANE GEMS
	public static final FabrialItem FABRIAL = new FabrialItem();

	public static final Slaughtergleam SLAUGHTERGLEAM = new Slaughtergleam();
	public static final MurdergleamItem MURDERGLEAM = new MurdergleamItem();
	public static final AgegleamItem AGEGLEAM = new AgegleamItem();
	public static final CleansegleamItem CLEANSEGLEAM = new CleansegleamItem();
	public static final SwitchgleamItem SWITCHGLEAM = new SwitchgleamItem();
	public static final SalvegleamItem SALVEGLEAM = new SalvegleamItem();

	public static final MunchstoneItem MUNCHSTONE = new MunchstoneItem();
	public static final TransferstoneItem TRANSFERSTONE = new TransferstoneItem();
	public static final OrderstoneItem ORDERSTONE = new OrderstoneItem();

	public static final MindspindleItem MINDSPINDLE = new MindspindleItem();
	public static final Elixirspindle ELIXIRSPINDLE = new Elixirspindle();

	public static final MountaintearItem MOUNTAINTEAR = new MountaintearItem();
	public static final RivertearItem RIVERTEAR = new RivertearItem();
	public static final ParchtearItem PARCHTEAR = new ParchtearItem();

	public static final PhoenixwayItem PHOENIXWAY = new PhoenixwayItem();
	public static final StormwayItem STORMWAY = new StormwayItem();

	public static final GemRechargePowder CHROMATIC_POWDER = new GemRechargePowder();
	public static final GemRechargePowderRainbow RAINBOW_CHROMATIC_POWDER = new GemRechargePowderRainbow();

	public static List<Item> ARSENAL_ITEMS = Arrays.asList(/*FABRIAL, */SLAUGHTERGLEAM, MURDERGLEAM, CLEANSEGLEAM, SWITCHGLEAM, SALVEGLEAM, MUNCHSTONE, /*TRANSFERSTONE,*/ ORDERSTONE, MINDSPINDLE, ELIXIRSPINDLE, MOUNTAINTEAR, RIVERTEAR, PARCHTEAR, PHOENIXWAY, STORMWAY, CHROMATIC_POWDER, RAINBOW_CHROMATIC_POWDER);
	//BAUBLES
	public static final GemSocket BAUBLE_GEMSOCKET = new GemSocket();

	//SPIRIT SPHERES
	//public static final SpiritOrbItem SPIRIT_ORB = new SpiritOrbItem();

	@SubscribeEvent
	public static void onItemRegister (RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();

		Arrays.asList(RAW_RADIANT_QUARTZ, SHAPED_RADIANT_QUARTZ, /*GEOMANCY_PENDULUM, GEOMANTIC_MAP,*/ MANIFEST, /*RADIANT_AMPHORA, SCEPTER_ABDUCTION, SCEPTER_TRANSLOCATION, */TOME_OF_ARCANA, LETTER_OF_INVITATION, LETTER_OF_RESIGNATION, WRIT_OF_EXPULSION,/* TOME_OF_REQUISITION,*/ RADIANT_AMPHORA, COMPONENT_CONTAINMENTFIELD, COMPONENT_MATRIXBRACE, COMPONENT_MATERIALINTERFACE, COMPONENT_RADIANTDUST, COMPONENT_SCINTILLATINGINLAY, DEVOURING_CHARM/*, SPIRIT_ORB*/, SCEPTER_REVELATION, SCEPTER_MANIPULATION, /*DEVOURING_CHARM,*/ BAUBLE_GEMSOCKET, /*FABRIAL*/ SLAUGHTERGLEAM, MURDERGLEAM, AGEGLEAM, CLEANSEGLEAM, SWITCHGLEAM, SALVEGLEAM, MUNCHSTONE, /*TRANSFERSTONE,*/ ORDERSTONE, MINDSPINDLE, ELIXIRSPINDLE, MOUNTAINTEAR, RIVERTEAR, PARCHTEAR, PHOENIXWAY, STORMWAY, CHROMATIC_POWDER, RAINBOW_CHROMATIC_POWDER).forEach(registry::register);
		Stream.of(/*BlockRegistry.MATRIX_CRYSTAL_CORE, BlockRegistry.MATRIX_REPOSITORY, BlockRegistry.MATRIX_RESERVOIR, BlockRegistry.MATRIX_STORAGE, BlockRegistry.MATRIX_DISTILLATE, */BlockRegistry.QUARTZ_SLIVER, BlockRegistry.STORAGE_RAW_QUARTZ, BlockRegistry.STORAGE_SHAPED_QUARTZ, BlockRegistry.RADIANT_CHEST, BlockRegistry.RADIANT_CRAFTING_TABLE, BlockRegistry.RADIANT_LANTERN, BlockRegistry.RADIANT_RESONATOR, BlockRegistry.RAW_QUARTZ/*, BlockRegistry.DOMINION_CRYSTAL*/, BlockRegistry.LECTERN_MANIFEST, BlockRegistry.GEMCUTTERS_TABLE, BlockRegistry.RADIANT_TROVE, BlockRegistry.MONITORING_CRYSTAL, BlockRegistry.RADIANT_TANK, BlockRegistry.BRAZIER_OF_HOARDING).map(BlockTemplate::getItemBlock).forEach(registry::register);

		// For handling bucket-related events
		MinecraftForge.EVENT_BUS.register(RADIANT_AMPHORA);
	}

	@SubscribeEvent
	public static void onModelRegister (ModelRegistryEvent event) {
		Arrays.asList(RAW_RADIANT_QUARTZ, SHAPED_RADIANT_QUARTZ/*, GEOMANCY_PENDULUM, GEOMANTIC_MAP*/, MANIFEST, /*RADIANT_AMPHORA, SCEPTER_ABDUCTION, SCEPTER_MANIPULATION, SCEPTER_TRANSLOCATION*/ LETTER_OF_INVITATION, LETTER_OF_RESIGNATION, WRIT_OF_EXPULSION, /*TOME_OF_REQUISITION, */RADIANT_AMPHORA, COMPONENT_CONTAINMENTFIELD, COMPONENT_MATRIXBRACE, COMPONENT_MATERIALINTERFACE, COMPONENT_RADIANTDUST, COMPONENT_SCINTILLATINGINLAY, DEVOURING_CHARM/*, SPIRIT_ORB*/, SCEPTER_REVELATION, SCEPTER_MANIPULATION, /*DEVOURING_CHARM,*/ BAUBLE_GEMSOCKET, /*FABRIAL*/ SLAUGHTERGLEAM, MURDERGLEAM, AGEGLEAM, CLEANSEGLEAM, SWITCHGLEAM, SALVEGLEAM, MUNCHSTONE, /*TRANSFERSTONE,*/ ORDERSTONE, MINDSPINDLE, ELIXIRSPINDLE, MOUNTAINTEAR, RIVERTEAR, PARCHTEAR, PHOENIXWAY, STORMWAY, CHROMATIC_POWDER, RAINBOW_CHROMATIC_POWDER).forEach(ItemTemplate::registerModels);

		Stream.of(/*BlockRegistry.MATRIX_CRYSTAL_CORE, BlockRegistry.MATRIX_REPOSITORY, BlockRegistry.MATRIX_RESERVOIR, BlockRegistry.MATRIX_STORAGE, BlockRegistry.MATRIX_DISTILLATE, */BlockRegistry.STORAGE_RAW_QUARTZ, BlockRegistry.STORAGE_SHAPED_QUARTZ, BlockRegistry.RADIANT_CHEST, BlockRegistry.RADIANT_CRAFTING_TABLE, BlockRegistry.RADIANT_LANTERN, BlockRegistry.RADIANT_RESONATOR, BlockRegistry.RAW_QUARTZ, /*BlockRegistry.DOMINION_CRYSTAL, */BlockRegistry.LECTERN_MANIFEST, BlockRegistry.GEMCUTTERS_TABLE, BlockRegistry.RADIANT_TROVE, BlockRegistry.MONITORING_CRYSTAL, BlockRegistry.BRAZIER_OF_HOARDING).map(BlockTemplate::getItemBlock).forEach((block) -> ModelLoader.setCustomModelResourceLocation(block, 0, new ModelResourceLocation(block.getRegistryName(), "inventory")));

		// has to be a bit special because it's a ItemGuidebook
		TOME_OF_ARCANA.registerModels();

		/*SPIRIT_ORB.registerModels();*/
	}

	@SubscribeEvent
	public static void missingMappings (RegistryEvent.MissingMappings<Item> event) {
	}
}

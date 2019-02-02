package com.aranaira.arcanearchives.init;

import com.aranaira.arcanearchives.items.*;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemLibrary
{

	public static final List<Item> ITEMS = new ArrayList<Item>();

	public static final Item RAW_RADIANT_QUARTZ = new RawQuartzItem();
	public static final Item CUT_RADIANT_QUARTZ = new CutQuartzItem();
	public static final Item GEOMANCY_PENDULUM = new GeomancyPendulumItem();
	public static final Item GEOMANTIC_MAP = new GeomanticMapItem();
	public static final Item MANIFEST = new ManifestItem();
	public static final Item RADIANT_BUCKET = new RadiantBucketItem();
	public static final Item SCEPTER_ABDUCTION = new ScepterAbductionItem();
	public static final Item SCEPTER_MANIPULATION = new ScepterManipulationItem();
	public static final Item SCEPTER_TRANSLOCATION = new ScepterTranslocationItem();
	public static final Item TOME_OF_ARCANA = new TomeOfArcanaItem();
	public static final Item TOME_OF_REQUISITION = new TomeOfRequisitionItem();

	//CRAFTING COMPONENTS
	public static final Item COMPONENT_CONTAINMENTFIELD = new ComponentContainmentFieldItem();
	public static final Item COMPONENT_MATRIXBRACE = new ComponentMatrixBraceItem();
	public static final Item COMPONENT_MATERIALINTERFACE = new ComponentMaterialInterfaceItem();
	public static final Item COMPONENT_RADIANTDUST = new ComponentRadiantDustItem();
	public static final Item COMPONENT_SCINTILLATINGINLAY = new ComponentScintillatingInlayItem();
	
	//SPIRIT SPHERES
	public static final Item SPIRIT_ORB = new SpiritOrbItem();
}

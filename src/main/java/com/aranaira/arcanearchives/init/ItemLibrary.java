package com.aranaira.arcanearchives.init;

import java.util.ArrayList;
import java.util.List;

import com.aranaira.arcanearchives.items.*;

import net.minecraft.item.Item;

public class ItemLibrary {

	public static final List<Item> ITEMS = new ArrayList<Item>();
	
	public static final Item RAW_RADIANT_QUARTZ = new RawQuartzItem();
	public static final Item CUT_RADIANT_QUARTZ = new CutQuartzItem();
	public static final Item MANIFEST = new ManifestItem();
	public static final Item TOME_OF_ARCANA = new TomeOfArcanaItem();
	public static final Item TOME_OF_REQUISITION = new TomeOfRequisitionItem();
	
	//ENCHAINMENT
	public static final Item ENCHAINMENT_LATTICE = new EnchainmentLatticeItem();
	public static final Item ENCHAINMENT_SLIVER = new EnchainmentSliverItem();
	public static final Item ENCHAINMENT_FRAGMENT = new EnchainmentFragmentItem();
	public static final Item ENCHAINMENT_SHARD = new EnchainmentShardItem();
	public static final Item ENCHAINMENT_GEM = new EnchainmentGemItem();
	public static final Item ENCHAINMENT_PRISM = new EnchainmentPrismItem();
	
	//CRAFTING COMPONENTS
	public static final Item COMPONENT_CONTAINMENTFIELD = new ComponentContainmentFieldItem();
	public static final Item COMPONENT_MATRIXBRACE = new ComponentMatrixBraceItem();
	public static final Item COMPONENT_MATERIALINTERFACE = new ComponentMaterialInterfaceItem();
	public static final Item COMPONENT_RADIANTDUST = new ComponentRadiantDustItem();
	public static final Item COMPONENT_SCINTILLATINGINLAY = new ComponentScintillatingInlayItem();
}

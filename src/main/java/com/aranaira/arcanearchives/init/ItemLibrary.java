package com.aranaira.arcanearchives.init;

import java.util.ArrayList;
import java.util.List;

import com.aranaira.arcanearchives.items.*;

import net.minecraft.item.Item;

public class ItemLibrary {

	public static final List<Item> ITEMS = new ArrayList<Item>();
	
	public static final Item RAW_RADIANT_QUARTZ = new RawQuartzItem();
	public static final Item CUT_RADIANT_QUARTZ = new CutQuartzItem();
	public static final Item TOME_OF_REQUISITION = new TomeOfRequisitionItem();
}

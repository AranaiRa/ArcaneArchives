package com.aranaira.arcanearchives.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class RawQuartzItem extends Item 
{
	public static final String name = "RawQuartzItem";
	public RawQuartzItem() {
		setRegistryName("item_rawquartz");
		setUnlocalizedName("Raw Quartz");
		setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
	}
}

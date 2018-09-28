package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.ArcaneArchives;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.EnumFacing;

public class RawQuartz extends Block {

	//public static final PropertyDirection FACING = PropertyDirection.create("facing");
	public static final String name = "RawQuartz";
	public RawQuartz() {
		super(Material.ROCK);
		setRegistryName("RawQuartz");
		setLightLevel(16/16f);
		//setDefaultState(this.blockState.getBaseState().withProperty(FACING,  EnumFacing.NORTH));
		setUnlocalizedName(ArcaneArchives.MODID + ":" + name);
		setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		// TODO Auto-generated constructor stub
	}
	
}

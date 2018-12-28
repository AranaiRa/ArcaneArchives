package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.init.ItemLibrary;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class StorageRawQuartz extends BlockTemplate {

	public static final String name = "storage_raw_quartz";
	public StorageRawQuartz() {
		super(name, Material.ROCK);
		setLightLevel(16/16f);
		setHardness(2.1f);
		setHarvestLevel("pickaxe", 0);
	}
}

package com.aranaira.arcanearchives.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class RadiantCraftingTable extends BlockTemplate{

	public static final String NAME = "radiant_crafting_table"; 
	
	public RadiantCraftingTable() {
		super(NAME, Material.GLASS);
		setLightLevel(16/16f);
		setHardness(2.1f);
	}

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }
	
	@Override
	public boolean hasOBJModel()
	{
		return true;
	}
}

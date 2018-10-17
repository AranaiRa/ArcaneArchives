package com.aranaira.arcanearchives.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class RadiantChest extends BlockTemplate{

	public static final String NAME = "radiant_chest"; 
	
	public RadiantChest() {
		super(NAME, Material.GLASS);
		setLightLevel(16/16f);
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

package com.aranaira.arcanearchives.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class RadiantLantern extends BlockTemplate{

	public static final String NAME = "radiant_lantern"; 
	
	public RadiantLantern() {
		super(NAME, Material.GLASS);
		setLightLevel(16/16f);
		setHardness(0.3f);
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

    @Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
    	return new AxisAlignedBB(0.35, 0.0, 0.35, 0.65, 1.0, 0.65);
	}
}

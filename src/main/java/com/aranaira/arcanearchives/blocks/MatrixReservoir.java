package com.aranaira.arcanearchives.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;

public class MatrixReservoir extends BlockTemplate {

	public static final String name = "matrix_reservoir";
	
	public MatrixReservoir()
	{
		super(name, Material.GLASS);
		setLightLevel(16/16f);
	}
	
	@Override
	public boolean hasOBJModel()
	{
		return true;
	}

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

}

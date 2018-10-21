package com.aranaira.arcanearchives.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;

public class MatrixStorage extends BlockTemplate {

	public static final String name = "matrix_storage";
	
	public MatrixStorage()
	{
		super(name, Material.GLASS);
		setLightLevel(16/16f);
		this.Height = 3;
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

package com.aranaira.arcanearchives.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;

public class MatrixDistillate extends BlockTemplate {

	public static final String name = "matrix_distillate";
	
	public MatrixDistillate()
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

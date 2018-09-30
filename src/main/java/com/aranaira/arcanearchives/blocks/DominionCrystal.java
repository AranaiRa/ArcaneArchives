package com.aranaira.arcanearchives.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class DominionCrystal extends BlockTemplate {

	public static final String name = "dominion_crystal";
	
	public DominionCrystal()
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

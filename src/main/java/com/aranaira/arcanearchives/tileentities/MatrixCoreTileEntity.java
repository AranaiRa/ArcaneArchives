package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.ArcaneArchives;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MatrixCoreTileEntity extends ImmanenceTileEntity 
{
	public MatrixCoreTileEntity()
	{
		super("matrix_core_tile_entity");
		ImmanenceGeneration = 200;
	}
	
	@Override
	public void update() {
		
		super.update();
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
	{
	    return (oldState.getBlock() != newSate.getBlock());
	}
}

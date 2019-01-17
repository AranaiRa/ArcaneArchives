package com.aranaira.arcanearchives.util;

import com.aranaira.arcanearchives.blocks.BlockTemplate;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Placeable 
{
	public static Iterable<BlockPos> GetPositions (BlockPos pos, int Width, int Height)
	{
		BlockPos posA = pos.add(-(Width/2), Height, (Width/2));
		BlockPos posB = pos.add(Width/2, 0, -(Width/2));

		return BlockPos.getAllInBox(posB, posA); // unsure of the order of these
	}

	public static boolean CanPlaceSize(World worldIn, BlockPos pos, int Width, int Height)
	{
		// These need to be generated properly with facing
		for (BlockPos pos2 : GetPositions(pos, Width, Height))
		{
			IBlockState state = worldIn.getBlockState(pos2);
			Block block = state.getBlock();

			if (!block.isAir(state, worldIn, pos2) && !block.isReplaceable(worldIn, pos2))
			{
				return false;
			}
		}

		return true;
	}

	public static void ReplaceBlocks (World worldIn, BlockPos pos, int Width, int Height)
	{
		for (BlockPos pos2 : GetPositions(pos, Width, Height))
		{
			worldIn.setBlockToAir(pos2);
		}
	}
}

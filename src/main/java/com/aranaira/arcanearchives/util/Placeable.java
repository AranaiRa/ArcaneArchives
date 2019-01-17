package com.aranaira.arcanearchives.util;

import com.aranaira.arcanearchives.blocks.BlockTemplate;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Placeable 
{
	public static boolean CanPlaceSize(World worldIn, BlockPos pos, int Width, int Height)
	{
		return isAreaClear(worldIn, pos, Width, Height);
	}
	
	//These are redundant, but names are self-explanatory, so they are left for the time being.
	public static boolean isAreaClear(World worldIn, BlockPos pos, int Width, int Height)
	{
		BlockPos posA = pos.up().add(-(Width/2), Height, (Width/2));
		BlockPos posB = pos.up().add(Width/2, 0, -(Width/2));
		Iterable<BlockPos> positions = BlockPos.getAllInBox(posB, posA); // unsure of the order of these

		for (BlockPos pos2 : positions)
		{
			if (!worldIn.isAirBlock(pos2))
			{
				return false;
			}
		}

		return true;
	}
}

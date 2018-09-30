package com.aranaira.arcanearchives.util;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Placeable 
{
	public static boolean CanPlaceLimit(int limit, Block block)
	{
		//TODO : Add way to get other blocks of that type on the network.
		//TODO : Print max amount if they are over that amount.
		return true;
	}
	
	public static boolean CanPlaceSize(World worldIn, BlockPos pos, int Width, int Height)
	{
		return isAreaClear(worldIn, pos, Width, Height);
	}
	
	//These are redundant, but names are self-explanatory, so they are left for the time being.
	public static boolean isAreaClear(World worldIn, BlockPos pos, int Width, int Height)
	{
		for (int x = -(Width/2); x < Width/2; x++)
			for (int z = -(Width/2); z < Width/2; z++)
				for (int y = 0; y < Height; y++)
					if (!isAirAtOffset(worldIn, pos, x, y, z))
						return false;
		return true;
	}
	
	private static boolean isAirAtOffset(World worldIn, BlockPos pos, int offsetX, int offsetY, int offsetZ)
	{
		return (Block.getIdFromBlock(worldIn.getBlockState(pos.add(offsetX, offsetY, offsetZ)).getBlock()) == 0);
	}
}

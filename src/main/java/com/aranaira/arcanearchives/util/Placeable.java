package com.aranaira.arcanearchives.util;

import com.aranaira.arcanearchives.blocks.BlockTemplate;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Placeable 
{
	public static Iterable<BlockPos> GetPositions (BlockPos pos, Size size, EnumFacing facing)
	{

		BlockPos posA = pos.add(-(size.width/2), size.height, (size.width/2));
		BlockPos posB = pos.add(size.width/2, 0, -(size.width/2));

		return BlockPos.getAllInBox(posB, posA); // unsure of the order of these
	}

	public static boolean CanPlaceSize(World worldIn, BlockPos pos, Size size, EnumFacing facing)
	{
		// These need to be generated properly with facing
		for (BlockPos pos2 : GetPositions(pos, size, facing))
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

	public static void ReplaceBlocks (World worldIn, BlockPos pos, Size size, EnumFacing facing)
	{
		for (BlockPos pos2 : GetPositions(pos, size, facing))
		{
			worldIn.setBlockToAir(pos2);
		}
	}

	@SuppressWarnings("WeakerAccess")
	public static class Size {
		public int width = 1;
		public int height = 1;
		public int length = 1;

		public Size (int width, int height, int length) {
			this.width = width;
			this.height = height;
			this.length = length;
		}

		public Size (int[] size) {
			if (size.length == 3) {
				this.width = size[0];
				this.height = size[1];
				this.length = size[2];
			}
		}

		public int[] getArray () {
			return new int[]{this.width, this.height, this.length};
		}

		public boolean hasAccessors ()
		{
			return this.width != 1 || this.height != 1 || this.length != 1;
		}

	}
}

package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.blocks.BlockDirectionalTemplate;
import com.aranaira.arcanearchives.blocks.BlockTemplate;
import com.aranaira.arcanearchives.util.Size;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public class AATileEntity extends TileEntity
{
	public String name;
	public Size size;
	private boolean breaking = false;

	public Size getSize()
	{
		return this.size;
	}

	public void setSize(Size newSize)
	{
		this.size = newSize;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public EnumFacing getFacing()
	{
		IBlockState state = world.getBlockState(getPos());
		if(state.getBlock() instanceof BlockTemplate)
		{
			return ((BlockTemplate) state.getBlock()).getFacing(world, pos);
		}

		return EnumFacing.WEST;
	}

	public void breakBlock()
	{
		breakBlock(null, true);
	}

	public void breakBlock(@Nullable IBlockState state, boolean harvest)
	{
		if(breaking) return;

		breaking = true;

		Block block = (state == null) ? world.getBlockState(getPos()).getBlock() : state.getBlock();
		EnumFacing facing = null;

		if(block instanceof BlockDirectionalTemplate && state != null)
		{
			facing = state.getValue(BlockDirectionalTemplate.FACING);
		}
		if(block instanceof BlockTemplate)
		{
			for(BlockPos point : ((BlockTemplate) block).calculateAccessors(world, getPos(), facing))
			{
				world.removeTileEntity(point);
				world.setBlockState(point, Blocks.AIR.getDefaultState());
			}

			// Is this enough to properly break the center?
			if(harvest)
			{
				world.destroyBlock(getPos(), true);
			}
		}
	}

	public boolean isActive () {
		return true;
	}
}

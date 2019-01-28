package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.blocks.BlockTemplate;
import com.aranaira.arcanearchives.util.Size;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class AATileEntity extends TileEntity
{
	public String name;
	public Size size;

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
		if(state.getBlock() instanceof BlockTemplate) {
			return ((BlockTemplate)state.getBlock()).getFacing(world, pos);
		}

		return EnumFacing.WEST;
	}
}

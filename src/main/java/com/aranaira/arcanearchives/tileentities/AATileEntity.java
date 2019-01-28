package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.blocks.BlockDirectionalTemplate;
import com.aranaira.arcanearchives.util.Placeable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import java.util.List;

public class AATileEntity extends TileEntity
{
	public String name;
	public Placeable.Size size;

	public Placeable.Size getSize()
	{
		return this.size;
	}

	public void setSize(Placeable.Size newSize)
	{
		this.size = newSize;
	}

	public boolean hasAccessors()
	{
		return this.size.hasAccessors();
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public EnumFacing getFacing(World world)
	{
		IBlockState state = world.getBlockState(getPos());
		if(state.getBlock() instanceof BlockDirectionalTemplate)
		{
			return state.getValue(BlockDirectionalTemplate.FACING);
		} else
		{
			return null;
		}
	}
}

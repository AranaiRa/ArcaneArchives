package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.init.BlockLibrary;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class AccessorTileEntity extends TileEntity
{
	public BlockPos ParentPos;

	public AccessorTileEntity()
	{
		super();
		BlockLibrary.TILE_ENTITIES.put("accessor", this);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		if(ParentPos != null) compound.setLong("pos", ParentPos.toLong());
		return super.writeToNBT(compound);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		if(compound.hasKey("pos")) ParentPos = BlockPos.fromLong(compound.getLong("pos"));
		super.readFromNBT(compound);
	}
}

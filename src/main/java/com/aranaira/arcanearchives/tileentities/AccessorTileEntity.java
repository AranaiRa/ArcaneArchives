package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.util.Size;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

@MethodsReturnNonnullByDefault
public class AccessorTileEntity extends AATileEntity
{
	public BlockPos ParentPos;

	public AccessorTileEntity()
	{
		super();
		setName("accessor");
		setSize(new Size(1, 1, 1));
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

	public BlockPos getParentPos()
	{
		return ParentPos;
	}

	@Nullable
	public AATileEntity getParent () {
		TileEntity te = world.getTileEntity(getParentPos());
		if (te instanceof AATileEntity) return (AATileEntity) te;
		return null;
	}
}

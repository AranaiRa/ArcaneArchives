package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.util.Placeable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class AATileEntity extends TileEntity {
    public Placeable.Size size;
    public EnumFacing facing;

    public void setSize (Placeable.Size newSize)
	{
		this.size = newSize;
	}

	public Placeable.Size getSize ()
	{
		return this.size;
	}

	public boolean hasAccessors ()
	{
		return this.size.hasAccessors();
	}

	public EnumFacing getFacing ()
	{
		return this.facing;
	}

	public void setFacing (EnumFacing facing)
	{
		this.facing = facing;
	}
}

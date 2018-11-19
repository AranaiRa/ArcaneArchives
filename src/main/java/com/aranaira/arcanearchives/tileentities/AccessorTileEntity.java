package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.init.BlockLibrary;

import net.minecraft.tileentity.TileEntity;

public class AccessorTileEntity extends TileEntity 
{
	public AccessorTileEntity()
	{
		super();
		BlockLibrary.TILE_ENTITIES.put("accessor", this);
	}
}

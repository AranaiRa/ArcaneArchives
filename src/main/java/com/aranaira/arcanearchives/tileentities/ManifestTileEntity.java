package com.aranaira.arcanearchives.tileentities;

import net.minecraft.item.ItemStack;

public abstract class ManifestTileEntity extends ImmanenceTileEntity
{
	public ManifestTileEntity(String name)
	{
		super(name);
	}

	public boolean isSingleStackInventory () {
		return false;
	}

	public ItemStack getSingleStack () {
		return ItemStack.EMPTY;
	}
}

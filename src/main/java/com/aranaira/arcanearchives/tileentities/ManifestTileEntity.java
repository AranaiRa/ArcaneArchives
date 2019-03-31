package com.aranaira.arcanearchives.tileentities;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

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

	public abstract String getDescriptor ();
	public abstract String getChestName ();
	public abstract IItemHandler getInventory ();
}

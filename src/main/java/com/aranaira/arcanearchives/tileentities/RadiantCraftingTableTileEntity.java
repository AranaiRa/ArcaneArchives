package com.aranaira.arcanearchives.tileentities;

import net.minecraftforge.items.ItemStackHandler;

public class RadiantCraftingTableTileEntity extends ImmanenceTileEntity
{
	private ItemStackHandler persistentMatrix = new ItemStackHandler(9);

	public RadiantCraftingTableTileEntity()
	{
		super("radiantcraftingtable");
	}

	public ItemStackHandler getInventory()
	{
		return persistentMatrix;
	}
}

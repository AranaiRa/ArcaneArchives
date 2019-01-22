package com.aranaira.arcanearchives.common;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.tileentities.RadiantCraftingTableTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;

public class ContainerRadiantCraftingTable extends Container
{
	RadiantCraftingTableTileEntity mTileEntity;

	public ContainerRadiantCraftingTable(RadiantCraftingTableTileEntity RCTTE, IInventory playerInventory)
	{
		//ArcaneArchivesNetwork aanetwork = NetworkHelper.getArcaneArchivesNetwork(playerIn.getUniqueID());
		mTileEntity = RCTTE;

		ArcaneArchives.logger.info("^^^^NULL CHECKS");
		ArcaneArchives.logger.info("inv null? " + playerInventory.equals(null));
		ArcaneArchives.logger.info("te null? " + RCTTE.equals(null));
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return true;
	}
}

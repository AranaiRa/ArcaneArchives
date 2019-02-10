package com.aranaira.arcanearchives.inventory;

import com.aranaira.arcanearchives.tileentities.RadiantCraftingTableTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;

import javax.annotation.Nonnull;

public class ContainerRadiantCraftingTable extends Container
{
	RadiantCraftingTableTileEntity mTileEntity;

	public ContainerRadiantCraftingTable(RadiantCraftingTableTileEntity RCTTE, IInventory playerInventory)
	{
		//AAServerNetwork aanetwork = NetworkHelper.getServerNetwork(playerIn.getUniqueID());
		mTileEntity = RCTTE;

		// ArcaneArchives.logger.info("^^^^NULL CHECKS");
		// ArcaneArchives.logger.info("inv null? " + playerInventory.equals(null));
		// ArcaneArchives.logger.info("te null? " + RCTTE.equals(null));
	}

	@Override
	public boolean canInteractWith(@Nonnull EntityPlayer playerIn)
	{
		return true;
	}
}

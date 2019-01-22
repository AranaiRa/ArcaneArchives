package com.aranaira.arcanearchives.common;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.tileentities.MatrixStorageTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;

public class ContainerMatrixStorage extends Container
{
	MatrixStorageTileEntity mTileEntity;

	public ContainerMatrixStorage(MatrixStorageTileEntity MSTE, IInventory playerInventory)
	{
		//ArcaneArchivesNetwork aanetwork = NetworkHelper.getArcaneArchivesNetwork(playerIn.getUniqueID());
		mTileEntity = MSTE;

		ArcaneArchives.logger.info("^^^^NULL CHECKS");
		ArcaneArchives.logger.info("inv null? " + playerInventory.equals(null));
		ArcaneArchives.logger.info("te null? " + MSTE.equals(null));
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return true;
	}
}

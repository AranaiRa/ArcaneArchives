package com.aranaira.arcanearchives.common;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.tileentities.MatrixRepositoryTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;

public class ContainerMatrixRepository extends Container
{
	MatrixRepositoryTileEntity mTileEntity;

	public ContainerMatrixRepository(MatrixRepositoryTileEntity MRTE, IInventory playerInventory)
	{
		//ArcaneArchivesNetwork aanetwork = NetworkHelper.getArcaneArchivesNetwork(playerIn.getUniqueID());
		mTileEntity = MRTE;

		ArcaneArchives.logger.info("^^^^NULL CHECKS");
		ArcaneArchives.logger.info("inv null? " + playerInventory.equals(null));
		ArcaneArchives.logger.info("te null? " + MRTE.equals(null));
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return true;
	}
}

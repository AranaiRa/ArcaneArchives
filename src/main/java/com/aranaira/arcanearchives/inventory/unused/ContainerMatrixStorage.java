package com.aranaira.arcanearchives.inventory.unused;

import com.aranaira.arcanearchives.tileentities.unused.MatrixStorageTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;

import javax.annotation.Nonnull;

public class ContainerMatrixStorage extends Container {
	MatrixStorageTileEntity mTileEntity;

	public ContainerMatrixStorage (MatrixStorageTileEntity MSTE, IInventory playerInventory) {
		//ServerNetwork aanetwork = NetworkHelper.getServerNetwork(playerIn.getUniqueID());
		mTileEntity = MSTE;

		// Againn, nonnull by default
		/*ArcaneArchives.logger.info("^^^^NULL CHECKS");
		ArcaneArchives.logger.info("inv null? " + playerInventory.equals(null));
		ArcaneArchives.logger.info("te null? " + MSTE.equals(null));*/
	}

	@Override
	public boolean canInteractWith (@Nonnull EntityPlayer playerIn) {
		return true;
	}
}

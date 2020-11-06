package com.aranaira.arcanearchives.inventory;

import com.aranaira.arcanearchives.tileentities.BrazierTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;

public class ContainerBrazier extends Container {

	protected BrazierTileEntity tile;

	public ContainerBrazier (BrazierTileEntity te, PlayerEntity player) {
		this.tile = te;
	}

	public BrazierTileEntity getTile () {
		return this.tile;
	}

	@Override
	public void onContainerClosed (PlayerEntity playerIn) {
		super.onContainerClosed(playerIn);
	}

	@Override
	public boolean canInteractWith (PlayerEntity playerIn) {
		return true;
	}
}

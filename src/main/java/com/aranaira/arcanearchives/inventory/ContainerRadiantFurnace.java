package com.aranaira.arcanearchives.inventory;

import com.aranaira.arcanearchives.blocks.RadiantFurnace;
import com.aranaira.arcanearchives.tileentities.BrazierTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantFurnaceTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

public class ContainerRadiantFurnace extends Container {

	protected RadiantFurnaceTileEntity tile;

	public ContainerRadiantFurnace(RadiantFurnaceTileEntity te, EntityPlayer player) {
		this.tile = te;
	}

	public RadiantFurnaceTileEntity getTile () {
		return this.tile;
	}

	@Override
	public void onContainerClosed (EntityPlayer playerIn) {
		super.onContainerClosed(playerIn);
	}

	@Override
	public boolean canInteractWith (EntityPlayer playerIn) {
		return true;
	}
}

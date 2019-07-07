package com.aranaira.arcanearchives.inventory;

import com.aranaira.arcanearchives.tileentities.BrazierTileEntity;
import com.google.common.collect.Sets;
import invtweaks.api.container.ChestContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import java.util.Set;

public class ContainerBrazier extends Container {

	protected BrazierTileEntity tile;

	public ContainerBrazier(BrazierTileEntity te, EntityPlayer player) {
		this.tile = te;
	}

	public BrazierTileEntity getTile () {
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

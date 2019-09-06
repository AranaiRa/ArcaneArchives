package com.aranaira.arcanearchives.inventory;

import com.aranaira.arcanearchives.blocks.RadiantFurnace;
import com.aranaira.arcanearchives.tileentities.BrazierTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantFurnaceTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class ContainerRadiantFurnace extends Container {

	protected RadiantFurnaceTileEntity tile;
	private final EntityPlayer player;

	public ContainerRadiantFurnace(RadiantFurnaceTileEntity te, EntityPlayer player) {
		this.tile = te;
		this.player = player;

		addPlayerSlots(player.inventory);
		//addFurnaceSlots();
	}

	private void addFurnaceSlots() {
		this.addSlotToContainer(new Slot(player.inventory, 0, 0, 0));
		this.addSlotToContainer(new Slot(player.inventory, 1, 20, 0));
		this.addSlotToContainer(new Slot(player.inventory, 2, 40, 0));
		this.addSlotToContainer(new Slot(player.inventory, 3, 60, 0));
	}

	private void addPlayerSlots(IInventory inv) {
		int index = 4;

		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 9; col++) {
				this.addSlotToContainer(new Slot(inv, index, 23 + col * 18, 115 + row * 18));
				index++;
			}
		}

		index = 0;

		for (int col = 0; col < 9; col++) {
			this.addSlotToContainer(new Slot(inv, index, 23 + col * 18, 173));
			index++;
		}
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

package com.aranaira.arcanearchives.inventory.slots;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;

public class SlotImmutable extends Slot {
	public SlotImmutable (IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}

	@Override
	public boolean canTakeStack (PlayerEntity playerIn) {
		return false;
	}
}

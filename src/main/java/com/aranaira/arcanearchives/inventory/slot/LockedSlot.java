package com.aranaira.arcanearchives.inventory.slot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;

public class LockedSlot extends Slot {
  public LockedSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
    super(inventoryIn, index, xPosition, yPosition);
  }

  public boolean mayPickup(PlayerEntity playerIn) {
    return false;
  }
}

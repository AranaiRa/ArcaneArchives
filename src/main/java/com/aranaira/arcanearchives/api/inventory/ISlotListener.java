package com.aranaira.arcanearchives.api.inventory;

import net.minecraft.inventory.IInventory;

@FunctionalInterface
public interface ISlotListener {
  void slotChanged (IInventory inventory, int slot);
}

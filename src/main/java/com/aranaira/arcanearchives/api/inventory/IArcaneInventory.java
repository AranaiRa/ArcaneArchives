package com.aranaira.arcanearchives.api.inventory;

import net.minecraftforge.items.IItemHandlerModifiable;

public interface IArcaneInventory extends IItemHandlerModifiable {
  default int getSlots() {
    return this.size();
  }

  int size();

  long getCountInSlot(int slot);

  boolean dynamicallySized();
}

package com.aranaira.arcanearchives.api.inventory;

import net.minecraftforge.items.IItemHandlerModifiable;

@FunctionalInterface
public interface IInventoryListener<T extends IItemHandlerModifiable> {
  void inventoryChanged (T inventory, int slot);
}

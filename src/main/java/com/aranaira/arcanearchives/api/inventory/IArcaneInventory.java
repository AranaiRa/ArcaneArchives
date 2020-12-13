package com.aranaira.arcanearchives.api.inventory;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

public interface IArcaneInventory extends IItemHandlerModifiable {
  default int getSlots () {
    return this.size();
  }

  int size ();

  boolean dynamicallySized ();
/*
  ItemStack extractItemInternal (int slot, int amount, boolean simulate);

  default ItemStack extractItem (int slot, int amount, boolean simulate) {
    if (simulate) {
      ItemStack stack = getStackInSlot(slot);
      if (stack.isEmpty()) {
        return ItemStack.EMPTY;
      }
    } else {
      return extractItemInternal(slot, amount, simulate);
    }
  }*/
}

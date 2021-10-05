package com.aranaira.arcanearchives.api.inventory.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

/* Taken from Dank Storage by tfarecnim
Licensed under a CC0 license but used with permission
https://github.com/Tfarcenim/Dank-Storage/blob/1.16.x/src/main/java/tfar/dankstorage/inventory/CappedSlot.java
 */
public class CappedSlot extends Slot {
  public CappedSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
    super(inventoryIn, index, xPosition, yPosition);
  }

  @Override
  public int getMaxStackSize(ItemStack stack) {
    return Math.min(getMaxStackSize(), stack.getMaxStackSize());
  }
}

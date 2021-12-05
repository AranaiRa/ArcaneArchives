package com.aranaira.arcanearchives.api.inventory.slot;

import com.aranaira.arcanearchives.api.inventory.ISlotListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/* Taken from Dank Storage by tfarecnim
Licensed under a CC0 license but used with permission
https://github.com/Tfarcenim/Dank-Storage/blob/1.16.x/src/main/java/tfar/dankstorage/inventory/CappedSlot.java
 */
public class CappedSlot extends Slot {
  private final List<ISlotListener> listeners = new ArrayList<>();

  public CappedSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
    super(inventoryIn, index, xPosition, yPosition);
  }

  @Override
  public int getMaxStackSize(ItemStack stack) {
    return Math.min(getMaxStackSize(), stack.getMaxStackSize());
  }

  public void addListener(ISlotListener listener) {
    this.listeners.add(listener);
  }

  @Override
  public ItemStack remove(int amount) {
    ItemStack result = super.remove(amount);
    for (ISlotListener listener : listeners) {
      listener.slotChanged(this.container, this.index);
    }
    return result;
  }

  @Override
  public void set(ItemStack stack) {
    super.set(stack);
    for (ISlotListener listener : listeners) {
      listener.slotChanged(this.container, this.index);
    }
  }
}

package com.aranaira.arcanearchives.types;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SlotIterable implements Iterable<SlotIterable.SlotIterator.SlotStack> {
  private IItemHandler inventory;
  private SlotIterator iter;

  public SlotIterable(IItemHandler inventory) {
    this.inventory = inventory;
  }

  @Override
  public Iterator<SlotIterator.SlotStack> iterator() {
    return new SlotIterator();
  }

  public class SlotIterator implements Iterator<SlotIterator.SlotStack> {
    int cursor;
    int size = inventory.getSlots();

    SlotIterator() {
    }

    @Override
    public boolean hasNext() {
      return cursor != size;
    }

    @Override
    public SlotStack next() {
      int i = cursor;
      if (i >= inventory.getSlots()) {
        throw new NoSuchElementException();
      }
      cursor = i + 1;
      return new SlotStack(inventory.getStackInSlot(cursor), cursor);
    }

    public class SlotStack {
      private ItemStack stack;
      private int slotIndex;

      public SlotStack(ItemStack stack, int slotIndex) {
        this.stack = stack;
        this.slotIndex = slotIndex;
      }

      public ItemStack getStack() {
        return stack;
      }

      public int getSlot() {
        return slotIndex;
      }
    }
  }
}

package com.aranaira.arcanearchives.inventories;

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

public interface ITrackingHandler extends IItemHandlerModifiable {
  Int2IntOpenHashMap getItemReference();

  void invalidate();

  default void manualRecount() {
    Int2IntOpenHashMap itemReference = getItemReference();
    itemReference.clear();
    for (int i = 0; i < getSlots(); i++) {
      ItemStack stack = getStackInSlot(i);
      if (!stack.isEmpty()) {
        int packed = RecipeItemHelper.pack(stack);
        itemReference.put(packed, itemReference.get(packed) + stack.getCount());
      }
    }
  }

  default boolean isSingleItemInventory () {
    return false;
  }
}

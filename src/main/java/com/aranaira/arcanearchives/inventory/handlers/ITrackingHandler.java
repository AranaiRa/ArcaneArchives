package com.aranaira.arcanearchives.inventory.handlers;

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

public interface ITrackingHandler extends IItemHandlerModifiable {
	Int2IntOpenHashMap getItemReference ();

	default void manualRecount () {
		Int2IntOpenHashMap itemReference = getItemReference();
		for (int i = 0; i < getSlots(); i++) {
			ItemStack stack = getStackInSlot(i);
			if (!stack.isEmpty()) {
				int packed = RecipeItemHelper.pack(stack);
				itemReference.put(packed, itemReference.get(packed) + stack.getCount());
			}
		}
	}

	default void addition (ItemStack stack, ItemStack spare) {
		Int2IntOpenHashMap itemReference = getItemReference();
		if (stack.isEmpty()) {
			return;
		}

		int ref = RecipeItemHelper.pack(stack);
		int count = stack.getCount();

		if (!spare.isEmpty()) {
			count -= spare.getCount();
		}

		int curCount = itemReference.get(ref);
		itemReference.put(ref, Math.max(-1, count + curCount));
	}

	default void subtraction (ItemStack stack, int count) {
		Int2IntOpenHashMap itemReference = getItemReference();
		if (stack.isEmpty()) {
			return;
		}
		if (count == -1) {
			count = stack.getCount();
		}

		int ref = RecipeItemHelper.pack(stack);
		int curCount = itemReference.get(ref);
		if (curCount > 0) {
			itemReference.put(ref, Math.max(curCount - count, 0));
		}
	}
}

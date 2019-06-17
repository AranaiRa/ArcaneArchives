package com.aranaira.arcanearchives.capabilities.tracking;

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import net.minecraft.item.ItemStack;

public interface IItemTracking {
	int emptySlots ();

	boolean contains (ItemStack stack);
	boolean contains (int packed);

	int quantity (ItemStack stack);
	int quantity (int packed);

	Int2IntOpenHashMap internal();
}

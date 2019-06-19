package com.aranaira.arcanearchives.capabilities.tracking;

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public interface IItemTracking {
	boolean contains (ItemStack stack);
	boolean contains (int packed);

	int quantity (ItemStack stack);
	int quantity (int packed);

	IItemHandler internal();
}

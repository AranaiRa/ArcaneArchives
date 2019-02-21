package com.aranaira.arcanearchives.util;

import net.minecraft.item.ItemStack;

public class ItemComparison
{	
	public static boolean areStacksEqualIgnoreSize(ItemStack stackA, ItemStack stackB)
	{
		return ItemStack.areItemsEqual(stackA, stackB) && ItemStack.areItemStackTagsEqual(stackA, stackB);
	}
}

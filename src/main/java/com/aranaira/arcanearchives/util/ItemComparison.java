package com.aranaira.arcanearchives.util;

import net.minecraft.item.ItemStack;

public class ItemComparison
{
	public static boolean AreItemsEqual(ItemStack stackA, ItemStack stackB)
	{
		ItemStack tempA = stackA.copy();
		tempA.setCount(1);
		ItemStack tempB = stackB.copy();
		tempB.setCount(1);

		return ItemStack.areItemsEqual(tempA, tempB) && ItemStack.areItemStackTagsEqual(tempA, tempB);
	}
}

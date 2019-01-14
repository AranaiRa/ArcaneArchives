package com.aranaira.arcanearchives.util;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemStackConsolidator 
{
	public static NonNullList<ItemStack> ConsolidateItems(NonNullList<ItemStack> list)
	{
		NonNullList<ItemStack> tempList = NonNullList.create();
		
		for (ItemStack s : list)
		{
			boolean added = false;
			for (ItemStack s2 : tempList)
			{
				if (ItemComparison.AreItemsEqual(s, s2))
				{
					s2.setCount(s2.getCount() + s.getCount());
					added = true;
				}
			}
			if (added)
				continue;
			tempList.add(s.copy());
		}
		
		return tempList;
	}
}

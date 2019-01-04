package com.aranaira.arcanearchives.util;

import java.util.List;

import com.aranaira.arcanearchives.ArcaneArchives;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class RadiantChestPlaceHolder
{
	public BlockPos mPos;
	public List<ItemStack> mItems;
	
	public RadiantChestPlaceHolder(BlockPos pos, List<ItemStack> items)
	{
		mPos = pos;
		mItems = items;
	}

	
	public boolean Contains(ItemStack item)
	{
		for (ItemStack s : mItems)
		{
			if (ItemStack.areItemsEqual(item, s))
			{
				return true;
			}
		}
		return false;
	}
}

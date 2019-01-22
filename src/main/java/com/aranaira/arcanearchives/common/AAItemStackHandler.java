package com.aranaira.arcanearchives.common;

import com.aranaira.arcanearchives.util.handlers.ConfigHandler;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class AAItemStackHandler extends ItemStackHandler
{

	public AAItemStackHandler(int i)
	{
		super(i);
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return 64 * ConfigHandler.ConfigValues.iRadiantChestMultiplier;
	}

	@Override
	protected int getStackLimit(int slot, ItemStack stack)
	{
		if(stack.getMaxStackSize() == 1)
		{
			return 1;
		}
		//return Math.min(getSlotLimit(slot), stack.getMaxStackSize() * 4);
		return getSlotLimit(slot);
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate)
	{
		return super.extractItem(slot, amount, simulate);
	}
}

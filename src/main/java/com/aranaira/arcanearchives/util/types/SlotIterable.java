package com.aranaira.arcanearchives.util.types;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SlotIterable implements Iterable<ItemStack>
{
	private IItemHandler inventory;
	private SlotIterator iter;

	public SlotIterable(IItemHandler inventory)
	{
		this.inventory = inventory;
	}

	@Override
	public Iterator<ItemStack> iterator()
	{
		this.iter = new SlotIterator();
		return iter;
	}

	public int getSlot()
	{
		return this.iter.cursor;
	}

	public class SlotIterator implements Iterator<ItemStack>
	{
		int cursor;
		int lastRet = -1;
		int size = inventory.getSlots();

		SlotIterator()
		{
		}

		@Override
		public boolean hasNext()
		{
			return cursor != size;
		}

		@Override
		public ItemStack next()
		{
			int i = cursor;
			if(i >= inventory.getSlots()) throw new NoSuchElementException();
			cursor = i + 1;
			return inventory.getStackInSlot(lastRet = i);
		}
	}
}

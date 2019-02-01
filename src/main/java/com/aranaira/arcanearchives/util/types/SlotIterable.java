package com.aranaira.arcanearchives.util.types;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import java.util.Iterator;

public class SlotIterable implements Iterable<ItemStack>
{
	private IItemHandler inventory;
	private boolean skipEmpty;
	private SlotIterator iter;

	public SlotIterable (IItemHandler inventory) {
		this.skipEmpty = true;
		this.inventory = inventory;
	}

	public SlotIterable (IItemHandler inventory, boolean skipEmpty) {
		this.skipEmpty = skipEmpty;
		this.inventory = inventory;
	}

	@Override
	public Iterator<ItemStack> iterator()
	{
		this.iter = new SlotIterator();
		return iter;
	}

	public int getSlot () {
		return this.iter.slot;
	}

	public class SlotIterator implements Iterator<ItemStack>
	{
		private int slot;

		SlotIterator()
		{
		}

		@Override
		public boolean hasNext()
		{
			return slot < inventory.getSlots();
		}

		@Override
		public ItemStack next()
		{
			if(this.hasNext())
			{
				ItemStack res = inventory.getStackInSlot(slot++);
				if (res.isEmpty() && skipEmpty) return next();
				return res;
			} else
			{
				return null;
			}
		}
	}
}

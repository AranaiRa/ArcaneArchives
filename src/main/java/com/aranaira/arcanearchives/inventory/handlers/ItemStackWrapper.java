package com.aranaira.arcanearchives.inventory.handlers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

public class ItemStackWrapper implements IItemHandler {
	private int indexStart;
	private int indexStop;
	private IItemHandler handler;

	public ItemStackWrapper (int indexStart, int indexStop, IItemHandler handler) {
		this.indexStart = indexStart;
		this.indexStop = indexStop;
		this.handler = handler;
	}

	public ItemStackWrapper (int index, IItemHandler handler) {
		this.indexStart = index;
		this.indexStop = index;
		this.handler = handler;
	}

	@Override
	public int getSlots () {
		return Math.max(1, indexStop - indexStart);
	}

	@Nonnull
	@Override
	public ItemStack getStackInSlot (int slot) {
		if (indexStart + slot > indexStop) {
			return ItemStack.EMPTY;
		}

		return this.handler.getStackInSlot(indexStart + slot);
	}

	@Nonnull
	@Override
	public ItemStack insertItem (int slot, @Nonnull ItemStack stack, boolean simulate) {
		slot = indexStart + slot;
		if (slot > indexStop) {
			return stack;
		}

		return this.handler.insertItem(slot, stack, simulate);
	}

	@Nonnull
	@Override
	public ItemStack extractItem (int slot, int amount, boolean simulate) {
		slot = indexStart + slot;
		if (slot > indexStop) {
			return ItemStack.EMPTY;
		}

		return this.handler.extractItem(slot, amount, simulate);
	}

	@Override
	public int getSlotLimit (int slot) {
		ItemStack cur = this.getStackInSlot(slot);
		if (!cur.isEmpty()) {
			return cur.getMaxStackSize();
		}
		return 64;
	}
}


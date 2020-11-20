/*
package com.aranaira.arcanearchives.inventory.handlers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;

public class ItemStackWrapper implements IItemHandlerModifiable {
	private int indexStart;
	private int indexStop;
	private IItemHandlerModifiable handler;

	public ItemStackWrapper (int indexStart, int indexStop, IItemHandlerModifiable handler) {
		this.indexStart = indexStart;
		this.indexStop = indexStop;
		this.handler = handler;
	}


	public ItemStackWrapper (int index, IItemHandlerModifiable handler) {
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

	@Override
	public void setStackInSlot (int slot, @Nonnull ItemStack stack) {
		slot = indexStart + slot;
		if (slot > indexStop) {
			return;
		}

		this.handler.setStackInSlot(slot, stack);
	}
}

*/


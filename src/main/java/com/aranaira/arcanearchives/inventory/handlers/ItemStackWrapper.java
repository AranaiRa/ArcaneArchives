package com.aranaira.arcanearchives.inventory.handlers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

public class ItemStackWrapper implements IItemHandler {
	private int index;
	private IItemHandler handler;

	public ItemStackWrapper (int index, IItemHandler handler) {
		this.index = index;
		this.handler = handler;
	}

	@Override
	public int getSlots () {
		return 1;
	}

	@Nonnull
	@Override
	public ItemStack getStackInSlot (int slot) {
		return this.handler.getStackInSlot(this.index);
	}

	@Nonnull
	@Override
	public ItemStack insertItem (int slot, @Nonnull ItemStack stack, boolean simulate) {
		return this.handler.insertItem(this.index, stack, simulate);
	}

	@Nonnull
	@Override
	public ItemStack extractItem (int slot, int amount, boolean simulate) {
		return this.handler.extractItem(this.index, amount, simulate);
	}

	@Override
	public int getSlotLimit (int slot) {
		ItemStack cur = this.getStackInSlot(0);
		if (!cur.isEmpty()) {
			return cur.getMaxStackSize();
		}
		return 64;
	}
}


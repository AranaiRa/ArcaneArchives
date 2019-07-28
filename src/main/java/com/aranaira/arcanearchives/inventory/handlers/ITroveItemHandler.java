package com.aranaira.arcanearchives.inventory.handlers;

import com.aranaira.arcanearchives.util.ItemUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

public interface ITroveItemHandler extends IItemHandler {

	ItemStack getReference ();

	int getCount ();

	int getMaxCount ();

	void setCount (int count);

	boolean isVoiding ();

	void update ();

	ItemStack getItem ();

	ItemStack getItemCurrent ();

	void setReference (ItemStack reference);

	boolean isEmpty ();

	@Override
	default int getSlotLimit (int slot) {
		return getMaxCount();
	}

	@Override
	default boolean isItemValid (int slot, @Nonnull ItemStack stack) {
		return ItemUtils.areStacksEqualIgnoreSize(getReference(), stack);
	}

	@Nonnull
	@Override
	default ItemStack insertItem (int slot, @Nonnull ItemStack stack, boolean simulate) {
		if (stack.isEmpty()) {
			return stack;
		}

		ItemStack reference = getReference();

		if (reference.isEmpty()) {
			setReference(stack);
		}

		int count = getCount();
		if (ItemUtils.areStacksEqualIgnoreSize(reference, stack) || reference.isEmpty()) {
			if (reference.isEmpty()) {
				reference = stack.copy();
				reference.setCount(1);
			}
			int thisCount = stack.getCount();
			int diff = 0;

			if (count + thisCount > getMaxCount()) {
				diff = (count + thisCount) - getMaxCount();
			}

			if (simulate) {
				if (diff == 0) {
					return ItemStack.EMPTY;
				}
				ItemStack result = stack.copy();
				result.setCount(diff);
				if (isVoiding()) {
					return ItemStack.EMPTY;
				}
				return result;
			}

			if (diff != 0) {
				setCount(count + thisCount - diff);
				ItemStack result = stack.copy();
				result.setCount(diff);
				update();
				if (isVoiding()) {
					return ItemStack.EMPTY;
				}
				return result;
			} else {
				setCount(count + stack.getCount());
				update();
				return ItemStack.EMPTY;
			}
		} else {
			return stack;
		}
	}

	@Nonnull
	@Override
	default ItemStack getStackInSlot (int slot) {
		ItemStack reference = getReference();
		if (reference == null || reference.isEmpty()) {
			return ItemStack.EMPTY;
		}
		if (slot == 0) {
			ItemStack result = reference.copy();
			result.setCount(getCount());

			return result;
		} else {
			return ItemStack.EMPTY;
		}
	}

	@Nonnull
	@Override
	default ItemStack extractItem (int slot, int amount, boolean simulate) {
		ItemStack reference = getReference();
		if (reference == null || reference.isEmpty()) {
			return ItemStack.EMPTY;
		}
		int count = getCount();
		if (amount > reference.getMaxStackSize()) {
			amount = reference.getMaxStackSize();
		}

		if (count < amount) {
			amount = count;
		}

		ItemStack result = getStackInSlot(0);

		if (amount < result.getCount()) {
			result.setCount(amount);
		}
		if (simulate) {
			return result;
		}

		this.setCount(count - amount);
		update();

		return result;
	}
}

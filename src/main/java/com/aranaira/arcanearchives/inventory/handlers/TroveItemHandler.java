package com.aranaira.arcanearchives.inventory.handlers;

import com.aranaira.arcanearchives.util.ItemUtilities;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

public class TroveItemHandler implements IItemHandler, INBTSerializable<NBTTagCompound> {
	private static int BASE_COUNT = 64 * 1024;
	private int upgrades = 0;
	private int count = 0;
	private ItemStack reference = ItemStack.EMPTY;
	private final Runnable updater;

	public TroveItemHandler (Runnable updater) {
		this.updater = updater;
	}

	private void update () {
		this.updater.run();
	}

	@Override
	public int getSlots () {
		return 2;
	}

	@Nonnull
	@Override
	public ItemStack getStackInSlot (int slot) {
		if (slot == 0) {
			ItemStack result = reference.copy();
			result.setCount(Math.min(this.count, result.getMaxStackSize()));

			return result;
		} else {
			return ItemStack.EMPTY;
		}
	}

	public void setUpgrades (int upgrades) {
		this.upgrades = upgrades;
	}

	public int getUpgrades () {
		return upgrades;
	}

	public int getMaxCount () {
		return getMaxCount(this.upgrades);
	}

	public int getMaxCount (int upgrades) {
		return BASE_COUNT * (upgrades + 1);
	}

	@Nonnull
	@Override
	public ItemStack insertItem (int slot, @Nonnull ItemStack stack, boolean simulate) {
		if (ItemUtilities.areStacksEqualIgnoreSize(reference, stack) || reference.isEmpty()) {
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
				return result;
			}

			if (diff != 0) {
				count += thisCount - diff;
				ItemStack result = stack.copy();
				result.setCount(diff);
				update();
				return result;
			} else {
				count += stack.getCount();
				update();
				return ItemStack.EMPTY;
			}
		} else {
			return stack;
		}
	}

	public ItemStack getItemCurrent () {
		if (count == 0) {
			return ItemStack.EMPTY;
		}

		return this.reference;
	}

	@Nonnull
	@Override
	public ItemStack extractItem (int slot, int amount, boolean simulate) {
		if (amount > reference.getMaxStackSize()) {
			amount = reference.getMaxStackSize();
		}

		if (this.count < amount) {
			amount = this.count;
		}

		ItemStack result = getStackInSlot(0);

		if (amount < result.getCount()) {
			result.setCount(amount);
		}
		if (simulate) {
			return result;
		}

		this.count -= amount;
		update();

		return result;
	}

	@Override
	public int getSlotLimit (int slot) {
		return reference.getMaxStackSize();
	}

	@Override
	public boolean isItemValid (int slot, @Nonnull ItemStack stack) {
		return ItemUtilities.areStacksEqualIgnoreSize(reference, stack);
	}

	public int getCount () {
		return count;
	}

	public ItemStack getItem () {
		return this.reference;
	}

	public int getPacked () {
		return RecipeItemHelper.pack(this.reference);
	}

	public void setItem (ItemStack reference) {
		this.reference = reference.copy();
		this.reference.setCount(1);
		update();
	}

	public boolean isEmpty () {
		return count == 0;
	}

	@Override
	public NBTTagCompound serializeNBT () {
		NBTTagCompound result = new NBTTagCompound();
		result.setInteger(Tags.COUNT, this.count);
		result.setTag(Tags.REFERENCE, this.reference.serializeNBT());
		result.setInteger(Tags.UPGRADES, this.upgrades);
		return result;
	}

	@Override
	public void deserializeNBT (NBTTagCompound nbt) {
		this.count = nbt.getInteger(Tags.COUNT);
		this.reference = new ItemStack(nbt.getCompoundTag(Tags.REFERENCE));
		this.upgrades = nbt.getInteger(Tags.UPGRADES);
	}

	public static class Tags {
		public static final String COUNT = "COUNT";
		public static final String REFERENCE = "REFERENCE";
		public static final String UPGRADES = "UPGRADES";

		public Tags () {
		}
	}
}

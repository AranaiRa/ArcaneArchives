package com.aranaira.arcanearchives.inventory.handlers;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;

public abstract class SizeUpgradeItemHandler implements IItemHandler, IItemHandlerModifiable, INBTSerializable<NBTTagList> {
	protected boolean[] upgrades = new boolean[]{false, false, false};

	private static int SLOT_1 = 2;
	private static int SLOT_2 = 3;
	private static int SLOT_3 = 4;

	@Override
	public int getSlots () {
		return 3;
	}

	@Override
	public abstract ItemStack getStackInSlot (int slot);

	public abstract Item getUpgradeForSlot (int slot);

	@Override
	public boolean isItemValid (int slot, @Nonnull ItemStack stack) {
		return stack.getItem() == getUpgradeForSlot(slot);
	}

	@Nonnull
	@Override
	public ItemStack insertItem (int slot, @Nonnull ItemStack stack, boolean simulate) {
		if (slot >= getSlots()) return stack;

		if (stack.getItem() == getUpgradeForSlot(slot) && resolveUpgradesIncluding(slot)) {
			if (!simulate) {
				upgrades[slot] = true;
			}

			if (stack.getCount() > 1) {
				ItemStack copy = stack.copy();
				copy.shrink(1);
				return copy;
			} else {
				return ItemStack.EMPTY;
			}
		}

		return stack;
	}

	@Nonnull
	@Override
	public ItemStack extractItem (int slot, int amount, boolean simulate) {
		if (amount != 1) return ItemStack.EMPTY;

		if (slot >= getSlots()) return ItemStack.EMPTY;

		if (slot == 0 || slot == 1) {
			if (slot == 0 && resolveUpgradesIncluding(2) || resolveUpgradesIncluding(1)) {
				return ItemStack.EMPTY;
			} else if (slot == 1 && resolveUpgradesIncluding(2)) {
				return ItemStack.EMPTY;
			}
		}

		if (!simulate) {
			upgrades[slot] = false;
		}

		return getStackInSlot(slot);
	}

	@Override
	public int getSlotLimit (int slot) {
		return 1;
	}

	public boolean resolveUpgradesUntil (int slot) {
		if (slot == 0) return true;
		return resolveUpgradesIncluding(slot-1);
	}

	public boolean resolveUpgradesIncluding (int slot) {
		int i = 0;
		for (boolean bool : upgrades) {
			if (i <= slot && !bool) return false;
		}
		return true;
	}

	@Override
	public NBTTagList serializeNBT () {
		NBTTagList result = new NBTTagList();
		for (boolean bool : upgrades) {
			result.appendTag(new NBTTagByte((byte)(bool ? 1 : 0)));
		}
		return result;
	}

	@Override
	public void deserializeNBT (NBTTagList tags) {
		assert tags.tagCount() == 3;
		int i = 0;
		for (NBTBase tag : tags) {
			assert tag.getId() == NBT.TAG_BYTE;

			upgrades[i] = ((NBTTagByte) tag).getByte() == 1;
		}
	}

	public int getUpgradesCount () {
		int count = 0;
		if (resolveUpgradesIncluding(0)) count += SLOT_1;
		if (resolveUpgradesIncluding(1)) count += SLOT_2;
		if (resolveUpgradesIncluding(2)) count += SLOT_3;
		return count;
	}

	@Override
	public void setStackInSlot(int slot, ItemStack stack) {
		if (getUpgradeForSlot(slot) == stack.getItem() && resolveUpgradesUntil(slot)) {
			upgrades[slot] = true;
		}
	}
}

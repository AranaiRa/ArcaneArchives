package com.aranaira.arcanearchives.inventory.handlers;

import com.aranaira.arcanearchives.items.IUpgradeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;

public abstract class SizeUpgradeItemHandler implements IItemHandler, IItemHandlerModifiable, INBTSerializable<NBTTagCompound> {
	protected boolean[] upgrades = new boolean[]{false, false, false};

	@Override
	public int getSlots () {
		return upgrades.length;
	}

	public abstract Item getUpgradeForSlot (int slot);

	// Most be override in the individual classes in order to rely on closures
	public boolean canReduceMultiplierTo (int size) {
		return false;
	}

	@Override
	public boolean isItemValid (int slot, @Nonnull ItemStack stack) {
		return stack.getItem() == getUpgradeForSlot(slot);
	}

	@Nonnull
	@Override
	public ItemStack insertItem (int slot, @Nonnull ItemStack stack, boolean simulate) {
		if (slot >= getSlots()) {
			return stack;
		}

		if (stack.getItem() == getUpgradeForSlot(slot) && resolveUpgradesUntil(slot)) {
			if (!simulate) {
				upgrades[slot] = true;
				onContentsChanged();
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
		if (amount != 1) {
			return ItemStack.EMPTY;
		}

		if (slot >= getSlots()) {
			return ItemStack.EMPTY;
		}

		if ((slot == 0 && (upgrades[1] || upgrades[2])) || (slot == 1 && upgrades[2])) {
			return ItemStack.EMPTY;
		}

		if (slot > 0) {
			if (!canReduceMultiplierTo(getUpgradesCount(slot))) {
				return ItemStack.EMPTY;
			}
		}

		if (!simulate) {
			upgrades[slot] = false;
			onContentsChanged();
		}

		return new ItemStack(getUpgradeForSlot(slot));
	}

	@Override
	public int getSlotLimit (int slot) {
		return 1;
	}

	public boolean resolveUpgradesUntil (int slot) {
		if (slot == 0) {
			return true;
		}
		return resolveUpgradesIncluding(slot - 1);
	}

	public boolean resolveUpgradesIncluding (int slot) {
		if (slot >= 3) {
			return false;
		}

		for (int i = 0; i <= slot; i++) {
			if (!upgrades[i]) {
				return false;
			}
		}

		return true;
	}

	@Override
	public NBTTagCompound serializeNBT () {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setBoolean("0", upgrades[0]);
		tag.setBoolean("1", upgrades[1]);
		tag.setBoolean("2", upgrades[2]);
		return tag;
	}

	@Override
	public void deserializeNBT (NBTTagCompound tags) {
		upgrades[0] = tags.getBoolean("0");
		upgrades[1] = tags.getBoolean("1");
		upgrades[2] = tags.getBoolean("2");
	}

	// Returns the total number of upgrades
	public int getUpgradesCount (int slot) {
		int count = 0;
		for (int i = 0; i <= slot; i++) {
			count += ((IUpgradeItem) getUpgradeForSlot(i)).getUpgradeSize(ItemStack.EMPTY);
		}
		return count;
	}

	public int getUpgradesCount () {
		int count = 0;
		if (resolveUpgradesIncluding(0)) {
			count += ((IUpgradeItem) getUpgradeForSlot(0)).getUpgradeSize(ItemStack.EMPTY);
		}
		if (resolveUpgradesIncluding(1)) {
			count += ((IUpgradeItem) getUpgradeForSlot(1)).getUpgradeSize(ItemStack.EMPTY);
		}
		if (resolveUpgradesIncluding(2)) {
			count += ((IUpgradeItem) getUpgradeForSlot(2)).getUpgradeSize(ItemStack.EMPTY);
		}
		return count;
	}

	@Override
	public void setStackInSlot (int slot, ItemStack stack) {
		if (getUpgradeForSlot(slot) == stack.getItem() && resolveUpgradesUntil(slot)) {
			upgrades[slot] = true;
		}

		onContentsChanged();
	}

	@Override
	public ItemStack getStackInSlot (int slot) {
		if (slot < 3 && upgrades[slot]) {
			return new ItemStack(getUpgradeForSlot(slot));
		}

		return ItemStack.EMPTY;
	}

	public void onContentsChanged () {
	}
}

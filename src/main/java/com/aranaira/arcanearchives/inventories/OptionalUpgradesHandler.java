/*package com.aranaira.arcanearchives.inventory.handlers;

import com.aranaira.arcanearchives.items.IUpgradeItem;
import com.aranaira.arcanearchives.types.enums.UpgradeType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class OptionalUpgradesHandler extends ItemStackHandler {
	public OptionalUpgradesHandler () {
		super(3);
	}

	public boolean hasUpgrade (UpgradeType type) {
		for (int i = 0; i < getSlots(); i++) {
			ItemStack stack = getStackInSlot(i);
			if (stack.getItem() instanceof IUpgradeItem && ((IUpgradeItem) stack.getItem()).getUpgradeType(stack) == type) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isItemValid (int slot, @Nonnull ItemStack stack) {
		if (!(stack.getItem() instanceof IUpgradeItem)) {
			return false;
		}

		IUpgradeItem upgrade = (IUpgradeItem) stack.getItem();
		if (upgrade.getUpgradeType(stack) == UpgradeType.SIZE) {
			return false;
		}

		return !hasUpgrade(upgrade.getUpgradeType(stack));
	}

	@Override
	public int getSlotLimit (int slot) {
		return 1;
	}

	public int getTotalUpgradesQuantity () {
		int count = 0;
		if (getIsUpgradePresent(0)) {
			count++;
		}
		if (getIsUpgradePresent(1)) {
			count++;
		}
		if (getIsUpgradePresent(2)) {
			count++;
		}
		return count;
	}

	*//**
 * Returns whether an upgrade is present in the slot or not.
 *
 * @param slot Which slot ID to check
 * @return true if an upgrade is present, false otherwise
 *//*
	public boolean getIsUpgradePresent (int slot) {
		return !getStackInSlot(slot).isEmpty();
	}
}*/

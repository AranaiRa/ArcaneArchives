package com.aranaira.arcanearchives.inventory.handlers;

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
}

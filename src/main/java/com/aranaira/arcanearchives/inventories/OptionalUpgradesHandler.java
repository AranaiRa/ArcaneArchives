package com.aranaira.arcanearchives.inventories;

import com.aranaira.arcanearchives.items.upgrades.IUpgrade;
import com.aranaira.arcanearchives.types.UpgradeType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public abstract class OptionalUpgradesHandler extends ItemStackHandler {
  public OptionalUpgradesHandler() {
    super(3);
  }

  public boolean hasUpgrade(UpgradeType type) {
    for (int i = 0; i < getSlots(); i++) {
      ItemStack stack = getStackInSlot(i);
      if (stack.getItem() instanceof IUpgrade && ((IUpgrade) stack.getItem()).getType(stack) == type) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
    if (!(stack.getItem() instanceof IUpgrade)) {
      return false;
    }

    IUpgrade upgrade = (IUpgrade) stack.getItem();
    if (upgrade.getType(stack) == UpgradeType.SIZE) {
      return false;
    }

    return !hasUpgrade(upgrade.getType(stack));
  }

  @Override
  public int getSlotLimit(int slot) {
    return 1;
  }

  public int getTotalUpgradesQuantity() {
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

  public boolean getIsUpgradePresent(int slot) {
    return !getStackInSlot(slot).isEmpty();
  }
}

package com.aranaira.arcanearchives.inventory;

import com.aranaira.arcanearchives.api.inventory.IArcaneInventory;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public abstract class AbstractArcaneItemHandler implements IArcaneInventory {
  public abstract boolean dynamicallySized();
  public abstract int size ();
  public abstract int getSlotLimit(int slot);

  @Override
  public void setStackInSlot(int slot, @Nonnull ItemStack stack) {

  }

  @Nonnull
  @Override
  public ItemStack getStackInSlot(int slot) {
    return null;
  }

  @Nonnull
  @Override
  public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
    return null;
  }

  @Nonnull
  @Override
  public ItemStack extractItem(int slot, int amount, boolean simulate) {
    return null;
  }

  @Override
  public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
    return false;
  }
}

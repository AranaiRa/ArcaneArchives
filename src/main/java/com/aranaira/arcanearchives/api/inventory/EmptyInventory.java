package com.aranaira.arcanearchives.api.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import org.apache.commons.lang3.NotImplementedException;

import javax.annotation.Nonnull;

public class EmptyInventory implements IArcaneInventory<EmptyInventory> {
  public static final EmptyInventory INSTANCE = new EmptyInventory();

  @Override
  public int size() {
    return 0;
  }

  @Override
  public long getCountInSlot(int slot) {
    return 0;
  }

  @Override
  public void enlarge(int size) {
  }

  @Override
  public CompoundNBT serialize() {
    throw new NotImplementedException("Serialize method not implemented on EmptyInventory");
  }

  @Override
  public void deserialize(CompoundNBT result) {
    throw new NotImplementedException("Deserialize method not implemented on EmptyInventory");
  }

  @Override
  public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
    throw new NotImplementedException("SetStackInSlot method not implemented on EmptyInventory");
  }

  @Nonnull
  @Override
  public ItemStack getStackInSlot(int slot) {
    return ItemStack.EMPTY;
  }

  @Nonnull
  @Override
  public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
    return stack;
  }

  @Nonnull
  @Override
  public ItemStack extractItem(int slot, int amount, boolean simulate) {
    return ItemStack.EMPTY;
  }

  @Override
  public int getSlotLimit(int slot) {
    return 0;
  }

  @Override
  public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
    return false;
  }
}

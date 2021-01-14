package com.aranaira.arcanearchives.api.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nonnull;

public class ArcaneItemHandler extends AbstractArcaneItemHandler {
  @Override
  public int getSlotLimit(int slot) {
    return Integer.MAX_VALUE;
  }

  @Override
  public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
    return true;
  }

  @Override
  public void deserialize(CompoundNBT result) {
    int size = result.getInt("slots");
    enlarge(size);
    for (int i = 0; i < size; i++) {
      ItemStackEntry entry = ItemStackEntry.deserialize(result.get("" + i));
      stacks.set(i, entry);
    }
  }

  @Override
  public Builder getBuilder() {
    return ArcaneItemHandler::create;
  }

  public static ArcaneItemHandler create (CompoundNBT input) {
    ArcaneItemHandler result = new ArcaneItemHandler();
    result.deserialize(input);
    return result;
  }
}

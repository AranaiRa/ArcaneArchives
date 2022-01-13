package com.aranaira.arcanearchives.inventory.handlers;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import noobanidus.libs.noobutil.recipe.LargeItemHandler;
import noobanidus.libs.noobutil.type.ItemStackEntry;

import javax.annotation.Nonnull;

public class CrystalWorkbenchInventory extends LargeItemHandler {
  public static EmptyArcaneWorkbenchInventory getEmpty() {
    return new EmptyArcaneWorkbenchInventory(18);
  }

  public CrystalWorkbenchInventory() {
    this(0);
  }

  public CrystalWorkbenchInventory(int size) {
    super(size);
  }

  public CrystalWorkbenchInventory(NonNullList<ItemStackEntry> stacks) {
    super(stacks);
  }

  @Override
  public int getSlotLimit(int slot) {
    // TODO
    return 64 * 4;
  }

  @Override
  public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
    // TODO
    return true;
  }

  public static class EmptyArcaneWorkbenchInventory extends CrystalWorkbenchInventory {
    public EmptyArcaneWorkbenchInventory() {
    }

    public EmptyArcaneWorkbenchInventory(int size) {
      super(size);
    }

    public EmptyArcaneWorkbenchInventory(NonNullList<ItemStackEntry> stacks) {
      super(stacks);
    }
  }
}

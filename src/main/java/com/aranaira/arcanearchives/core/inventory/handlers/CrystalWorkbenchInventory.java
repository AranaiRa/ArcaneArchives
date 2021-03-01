package com.aranaira.arcanearchives.core.inventory.handlers;

import com.aranaira.arcanearchives.api.inventory.AbstractArcaneItemHandler;
import com.aranaira.arcanearchives.api.inventory.ItemStackEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;

public class CrystalWorkbenchInventory extends AbstractArcaneItemHandler {
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

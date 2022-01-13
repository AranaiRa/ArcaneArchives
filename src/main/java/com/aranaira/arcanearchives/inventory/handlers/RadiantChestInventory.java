package com.aranaira.arcanearchives.inventory.handlers;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import noobanidus.libs.noobutil.recipe.AbstractLargeItemHandler;
import noobanidus.libs.noobutil.type.ItemStackEntry;

import javax.annotation.Nonnull;

// TODO: LOTS OF MARKING DIRTY
// LET'S GET SUPER DIRTY
// AND SAVING THE WORLD DATA ON CLOSING
public class RadiantChestInventory extends AbstractLargeItemHandler {
  public static EmptyRadiantChestInventory getEmpty() {
    return new EmptyRadiantChestInventory(54);
  }

  public RadiantChestInventory() {
    this(0);
  }

  public RadiantChestInventory(int size) {
    super(size);
  }

  public RadiantChestInventory(NonNullList<ItemStackEntry> stacks) {
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

  public static class EmptyRadiantChestInventory extends RadiantChestInventory {
    public EmptyRadiantChestInventory() {
    }

    public EmptyRadiantChestInventory(int size) {
      super(size);
    }

    public EmptyRadiantChestInventory(NonNullList<ItemStackEntry> stacks) {
      super(stacks);
    }
  }
}
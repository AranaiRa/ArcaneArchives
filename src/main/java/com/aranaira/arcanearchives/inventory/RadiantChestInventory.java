package com.aranaira.arcanearchives.inventory;

import com.aranaira.arcanearchives.api.inventory.AbstractArcaneItemHandler;
import com.aranaira.arcanearchives.api.inventory.ItemStackEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;

public class RadiantChestInventory extends AbstractArcaneItemHandler<RadiantChestInventory> {
  public RadiantChestInventory() {
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
}

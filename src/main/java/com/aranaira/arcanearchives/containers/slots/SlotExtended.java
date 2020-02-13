package com.aranaira.arcanearchives.containers.slots;

import com.aranaira.arcanearchives.inventories.ExtendedItemStackHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class SlotExtended extends Slot {

  private static IInventory emptyInventory = new InventoryBasic("[Null]", true, 0);
  private final ExtendedItemStackHandler itemHandler;
  private final int index;

  public SlotExtended(ExtendedItemStackHandler itemHandler, int index, int xPosition, int yPosition) {
    super(emptyInventory, index, xPosition, yPosition);
    this.itemHandler = itemHandler;
    this.index = index;
  }

  @Override
  public boolean isItemValid(@Nonnull ItemStack stack) {
    if (stack.isEmpty()) {
      return false;
    }

    ExtendedItemStackHandler handler = this.getItemHandler();
    ItemStack currentStack = getItemHandler().getStackInSlot(index);

    getItemHandler().setStackInSlot(index, ItemStack.EMPTY);

    ItemStack remainder = getItemHandler().insertItem(index, stack, true);

    getItemHandler().setStackInSlot(index, currentStack);

    return remainder.isEmpty() || remainder.getCount() < stack.getCount();
  }

  @Override
  @Nonnull
  public ItemStack getStack() {
    return this.getItemHandler().getStackInSlot(index);
  }

  @Override
  public void putStack(@Nonnull ItemStack stack) {
    this.getItemHandler().setStackInSlot(index, stack);
    this.onSlotChanged();
  }

  @Override
  public void onSlotChange(@Nonnull ItemStack p_75220_1_, @Nonnull ItemStack p_75220_2_) {
    getItemHandler().onContentsChanged(index);
  }

  @Override
  public int getSlotStackLimit() {
    return this.itemHandler.getSlotLimit(this.index);
  }

  @Override
  public int getItemStackLimit(@Nonnull ItemStack stack) {
    return this.itemHandler.getStackLimit(this.index, stack);
  }

  @Override
  public boolean canTakeStack(EntityPlayer playerIn) {
    return !this.getItemHandler().extractItem(index, 1, true).isEmpty();
  }

  @Override
  @Nonnull
  public ItemStack decrStackSize(int amount) {
    return this.getItemHandler().extractItem(index, amount, false);
  }

  public ExtendedItemStackHandler getItemHandler() {
    return itemHandler;
  }

  @Override
  public boolean isSameInventory(Slot other) {
    return other instanceof SlotExtended && ((SlotExtended) other).getItemHandler() == this.itemHandler;
  }

}

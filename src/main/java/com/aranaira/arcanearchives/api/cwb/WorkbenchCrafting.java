package com.aranaira.arcanearchives.api.cwb;

import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;

public class WorkbenchCrafting {
  private final Container container;
  private final TileEntity tileEntity;
  private final IItemHandlerModifiable inventory;

  public WorkbenchCrafting(Container container, TileEntity tileEntity, IItemHandlerModifiable inventory) {
    this.container = container;
    this.tileEntity = tileEntity;
    this.inventory = inventory;
  }

  public Container getContainer() {
    return container;
  }

  public TileEntity getTileEntity() {
    return tileEntity;
  }

  public IItemHandlerModifiable getInventory() {
    return inventory;
  }

  public ItemStack extractItem(int slot, int amount, boolean simulate) {
    return inventory.extractItem(slot, amount, simulate);
  }

  public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
    return inventory.insertItem(slot, stack, simulate);
  }

  public ItemStack getStackInSlot(int slot) {
    return inventory.getStackInSlot(slot);
  }

  public int getSlots() {
    return inventory.getSlots();
  }
}

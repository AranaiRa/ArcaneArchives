package com.aranaira.arcanearchives.inventories;

import com.aranaira.arcanearchives.tileentities.NetworkedBaseTile;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;

public class MonitoringWrapper implements ITrackingHandler, INBTSerializable<NBTTagCompound> {
  private final IItemHandler internal;
  private final NetworkedBaseTile parent;
  private final Int2IntOpenHashMap itemReference = new Int2IntOpenHashMap();
  private boolean invalid;

  public MonitoringWrapper(NetworkedBaseTile parent, IItemHandler internal) {
    this.internal = internal;
    this.parent = parent;
    this.invalid = true;
  }

  @Override
  public Int2IntOpenHashMap getItemReference() {
    if (invalid) {
      invalid = false;
      manualRecount();
    }
    return itemReference;
  }

  @Override
  public void invalidate() {
    this.invalid = true;
  }

  @Override
  public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
    throw new IllegalStateException("MonitoringWrapper::setStackInSlot is not supported.");
  }

  @Override
  public int getSlots() {
    return this.internal.getSlots();
  }

  @Nonnull
  @Override
  public ItemStack getStackInSlot(int slot) {
    return this.internal.getStackInSlot(slot);
  }

  @Nonnull
  @Override
  public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
    throw new IllegalStateException("MonitoringWrapper::insertItem is not supported.");

/*    if (!simulate) {
      invalidate();
    }
    ItemStack result = this.internal.insertItem(slot, stack, simulate);
    if (!simulate && (result.isEmpty() || result.getCount() != stack.getCount())) {
      parent.markDirty();
    }
    return result;*/
  }

  @Nonnull
  @Override
  public ItemStack extractItem(int slot, int amount, boolean simulate) {
    if (!simulate) {
      invalidate();
    }
    ItemStack result = this.internal.extractItem(slot, amount, simulate);
    if (!simulate && !result.isEmpty()) {
      parent.markDirty();
    }
    return result;
  }

  @Override
  public int getSlotLimit(int slot) {
    return this.internal.getSlotLimit(slot);
  }

  @Override
  public NBTTagCompound serializeNBT() {
    return new NBTTagCompound();
  }

  @Override
  public void deserializeNBT(NBTTagCompound nbt) {
  }
}

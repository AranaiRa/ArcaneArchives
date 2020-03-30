package com.aranaira.arcanearchives.inventories;

import com.aranaira.arcanearchives.tileentities.NetworkedBaseTile;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

public class TrackingExtendedHandler extends ExtendedHandler implements ITrackingHandler {
  private Int2IntOpenHashMap itemReference = new Int2IntOpenHashMap();
  private boolean invalid = true;
  private NetworkedBaseTile parent;

  public TrackingExtendedHandler(NetworkedBaseTile parent, int size) {
    super(size);
    this.parent = parent;
    itemReference.defaultReturnValue(0);
  }

  @SuppressWarnings("ConstantConditions")
  @Override
  public void deserializeNBT(NBTTagCompound nbt) {
    super.deserializeNBT(nbt);
    if (parent.getWorld() == null || !parent.getWorld().isRemote) {
      manualRecount();
    }
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
    invalid = true;
  }

  @Override
  public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
    invalidate();
    super.setStackInSlot(slot, stack);
    parent.markDirty();
  }

  @Nonnull
  @Override
  public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
    if (!simulate) {
      invalidate();
    }
    ItemStack result = super.insertItem(slot, stack, simulate);
    if (!simulate && (result.isEmpty() || result.getCount() != stack.getCount())) {
      parent.markDirty();
    }
    return result;
  }

  @Nonnull
  @Override
  public ItemStack extractItem(int slot, int amount, boolean simulate) {
    if (!simulate) {
      invalidate();
    }
    ItemStack result = super.extractItem(slot, amount, simulate);
    if (!simulate && !result.isEmpty()) {
      parent.markDirty();
    }
    return result;
  }
}

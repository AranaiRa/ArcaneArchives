package com.aranaira.arcanearchives.api.inventory;

import com.aranaira.arcanearchives.api.inventory.data.SlotInfoTable;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.registry.Registry;

import java.util.Optional;

class ItemStackEntry {
  public static Codec<ItemStackEntry> CODEC = RecordCodecBuilder.create((instance) -> instance.group(ItemStack.CODEC.fieldOf("item").forGetter(o -> o.stack), Codec.LONG.fieldOf("count").forGetter(o -> o.count), SlotInfoTable.CODEC.optionalFieldOf("info").forGetter(o -> o.info.isEmpty() ? Optional.empty() : Optional.of(o.info))).apply(instance, (a, b, c) -> new ItemStackEntry(a, b, c.orElse(new SlotInfoTable()))));

  public static ItemStackEntry EMPTY = new ItemStackEntry(ItemStack.EMPTY);

  private ItemStack stack;
  private long count;
  private SlotInfoTable info;

  public ItemStackEntry(ItemStack stack) {
    this.stack = stack;
    this.count = stack.getCount();
    this.info = null;
  }

  private ItemStackEntry(ItemStack stack, long count, SlotInfoTable info) {
    this.stack = stack;
    this.count = count;
    this.info = info;
  }

  public ItemStack getStackOriginal() {
    return stack;
  }

  public ItemStack getStackCopy () {
    ItemStack copy = stack.copy();
    copy.setCount((int) count);
    return copy;
  }

  public ItemStack extract (int count) {
    if (count < this.count) {
      count = (int) this.count;
    }
    // TODO: IS THIS SANE?
    if (stack.getCount() == count) {
      return stack;
    }

    ItemStack copy = getStackCopy();
    copy.setCount(count);
    return copy;
  }

  public long getCount() {
    return count;
  }

  public void setCount (long count) {
    // TODO: Is this check redundant?
    if (!stack.isEmpty()) {
      this.count = count;
      this.stack.setCount((int) this.count);
    }
  }

  public void grow (long count) {
    if (!stack.isEmpty()) {
      this.count += count;
      this.stack.setCount((int) this.count);
    }
  }

  public SlotInfoTable getInfo() {
    return info;
  }

  public boolean isEmpty () {
    if (stack.isEmpty()) {
      return true;
    }

    return count == 0 && info.isEmpty();
  }
}

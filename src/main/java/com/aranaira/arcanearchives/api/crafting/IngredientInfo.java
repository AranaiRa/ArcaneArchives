package com.aranaira.arcanearchives.api.crafting;

import com.aranaira.arcanearchives.api.network.AbstractNetworkObject;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.IntArrayNBT;
import net.minecraftforge.common.util.INBTSerializable;

public class IngredientInfo extends AbstractNetworkObject<ByteBuf> implements INBTSerializable<IntArrayNBT> {
  private int slot;
  private int found;
  private int remaining;

  public IngredientInfo(ByteBuf incoming) {
    this.slot = incoming.readInt();
    this.found = incoming.readInt();
    this.remaining = incoming.readInt();
  }

  public IngredientInfo(int slot, int found, int remaining) {
    this.slot = slot;
    this.found = found;
    this.remaining = remaining;
  }

  public int getSlot() {
    return slot;
  }

  public int getFound() {
    return found;
  }

  public int getRemaining() {
    return remaining;
  }

  public int getNeeded() {
    return getFound() + getRemaining();
  }

  public boolean isComplete() {
    return getRemaining() == 0 && getFound() == getNeeded();
  }

  @Override
  public IntArrayNBT serializeNBT() {
    return new IntArrayNBT(new int[]{slot, found, remaining});
  }

  @Override
  public void deserializeNBT(IntArrayNBT nbt) {
    int[] info = nbt.getIntArray();
    this.slot = info[0];
    this.found = info[1];
    this.remaining = info[2];
  }

  @Override
  public void serialize(ByteBuf buffer) {
    buffer.writeInt(slot);
    buffer.writeInt(found);
    buffer.writeInt(remaining);
  }

  public static IngredientInfo deserialize (ByteBuf buf) {
    return AbstractNetworkObject.deserialize(buf, IngredientInfo::new);
  }
}

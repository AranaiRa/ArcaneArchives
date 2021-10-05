package com.aranaira.arcanearchives.api.crafting.ingredients;

import com.aranaira.arcanearchives.api.network.AbstractNetworkObject;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.IntArrayNBT;
import net.minecraftforge.common.util.INBTSerializable;

public class IngredientInfo extends AbstractNetworkObject<ByteBuf> implements INBTSerializable<IntArrayNBT> {
  private int slot;
  private int index;
  private int found;

  public IngredientInfo(ByteBuf incoming) {
    this.slot = incoming.readInt();
    this.index = incoming.readInt();
    this.found = incoming.readInt();
  }

  public IngredientInfo(int slot, int index, int found) {
    this.slot = slot;
    this.index = index;
    this.found = found;
  }

  public int getSlot() {
    return slot;
  }

  public int getFound() {
    return found;
  }

  public int getIndex() {
    return index;
  }

  @Override
  public IntArrayNBT serializeNBT() {
    return new IntArrayNBT(new int[]{slot, index, found});
  }

  @Override
  public void deserializeNBT(IntArrayNBT nbt) {
    int[] info = nbt.getAsIntArray();
    this.slot = info[0];
    this.index = info[1];
    this.found = info[2];
  }

  @Override
  public void serialize(ByteBuf buffer) {
    buffer.writeInt(slot);
    buffer.writeInt(index);
    buffer.writeInt(found);
  }

  public static IngredientInfo deserialize (ByteBuf buf) {
    return AbstractNetworkObject.deserialize(buf, IngredientInfo::new);
  }
}

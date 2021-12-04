package com.aranaira.arcanearchives.api.crafting.ingredients;

import com.aranaira.arcanearchives.api.network.AbstractNetworkObject;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.IntArrayNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.INBTSerializable;
import noobanidus.libs.noobutil.util.EnumUtil;

public class IngredientInfo extends AbstractNetworkObject<PacketBuffer> implements INBTSerializable<IntArrayNBT> {
  public enum SlotType {
    CONTAINER,
    PLAYER_INVENTORY,
    PLAYER_OFFHAND,
    NOT_FOUND,
    COLLATED
  }

  private int slot;
  private int index;
  private int found;
  private int required;
  private SlotType type;

  public IngredientInfo(PacketBuffer incoming) {
    this.slot = incoming.readInt();
    this.index = incoming.readInt();
    this.found = incoming.readInt();
    this.required = incoming.readInt();
    this.type = incoming.readEnum(SlotType.class);
  }

  public IngredientInfo(int slot, int index, int required, int found, SlotType type) {
    this.slot = slot;
    this.index = index;
    this.found = found;
    this.type = type;
    this.required = required;
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

  public int getRequired() {
    return required;
  }

  public SlotType getType() {
    return type;
  }

  @Override
  public IntArrayNBT serializeNBT() {
    return new IntArrayNBT(new int[]{slot, index, found, required, type.ordinal()});
  }

  @Override
  public void deserializeNBT(IntArrayNBT nbt) {
    int[] info = nbt.getAsIntArray();
    this.slot = info[0];
    this.index = info[1];
    this.found = info[2];
    this.required = info[3];
    this.type = EnumUtil.fromOrdinal(SlotType.class, info[3]);
  }

  @Override
  public void serialize(PacketBuffer buffer) {
    buffer.writeInt(slot);
    buffer.writeInt(index);
    buffer.writeInt(found);
    buffer.writeInt(required);
    buffer.writeEnum(type);
  }

  public static IngredientInfo deserialize (PacketBuffer buf) {
    return AbstractNetworkObject.deserialize(buf, IngredientInfo::new);
  }
}

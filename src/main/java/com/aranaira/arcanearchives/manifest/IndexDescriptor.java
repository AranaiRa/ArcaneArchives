package com.aranaira.arcanearchives.manifest;

import com.aranaira.arcanearchives.types.IEnumOrdinal;
import com.aranaira.arcanearchives.types.ISerializePacketBuffer;
import net.minecraft.network.PacketBuffer;

import javax.annotation.Nullable;

public class IndexDescriptor implements ISerializePacketBuffer<IndexDescriptor> {
  private final IndexType type;
  private final String description;

  public IndexDescriptor(IndexType type, @Nullable String description) {
    this.type = type;
    this.description = description;
  }

  public IndexType getType() {
    return type;
  }

  public String getDescription() {
    return description;
  }

  @Override
  public void toPacket(PacketBuffer buf) {
    buf.writeInt(type.ordinal());
    buf.writeBoolean(description == null || description.isEmpty());
    if (description != null && !description.isEmpty()) {
      buf.writeString(description);
    }
  }

  @Nullable
  public static IndexDescriptor fromPacket (PacketBuffer buf) {
    IndexType type = IndexType.fromOrdinal(buf.readInt());
    String description = null;
    if (buf.readBoolean()) {
      description = buf.readString(32767);
    }

    if (type == null) {
      return null;
    }

    return new IndexDescriptor(type, description);
  }

  public enum IndexType implements IEnumOrdinal<IndexType> {
    TROVE, CHEST;

    @Nullable
    public static IndexType fromOrdinal (int ordinal) {
      return TROVE.byOrdinal(values(), ordinal, null);
    }

    @Override
    public int index() {
      return ordinal();
    }
  }
}

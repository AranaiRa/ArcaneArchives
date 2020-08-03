package com.aranaira.arcanearchives.types;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;

public interface ISerializePacketBuffer<T> extends ISerializeByteBuf<T> {
  T fromPacket(PacketBuffer buf);

  void toPacket(PacketBuffer buf);

  default T fromBytes(ByteBuf buf) {
    return fromPacket(new PacketBuffer(buf));
  }

  default void toBytes(ByteBuf buf) {
    toPacket(new PacketBuffer(buf));
  }
}

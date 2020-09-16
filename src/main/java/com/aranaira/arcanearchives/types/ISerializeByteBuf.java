package com.aranaira.arcanearchives.types;

import io.netty.buffer.ByteBuf;

public interface ISerializeByteBuf<T> {
/*  T fromBytes(ByteBuf buf);*/

  void toBytes(ByteBuf buf);
}

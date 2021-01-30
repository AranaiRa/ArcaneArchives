package com.aranaira.arcanearchives.api.network;

import io.netty.buffer.ByteBuf;

import java.util.function.Function;

public abstract class AbstractNetworkObject<T extends ByteBuf> {
  public abstract void serialize (T buffer);

  protected static <T extends ByteBuf, O extends AbstractNetworkObject<T>> O deserialize (T buffer, Function<T, O> builder) {
    return builder.apply(buffer);
  }
}

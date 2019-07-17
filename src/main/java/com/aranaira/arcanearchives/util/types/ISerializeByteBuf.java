package com.aranaira.arcanearchives.util.types;

import io.netty.buffer.ByteBuf;

public interface ISerializeByteBuf<T> {
	T fromBytes (ByteBuf buf);

	void toBytes (ByteBuf buf);
}

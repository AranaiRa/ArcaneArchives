package com.aranaira.arcanearchives.data.types;

import com.aranaira.arcanearchives.types.ISerializeByteBuf;
import io.netty.buffer.ByteBuf;

public class SynchroniseInfo implements ISerializeByteBuf<SynchroniseInfo> {
	public int totalResonators = 0;
	public int totalCores = 0;

	@Override
	public SynchroniseInfo fromBytes (ByteBuf buf) {
		this.totalResonators = buf.readInt();
		this.totalCores = buf.readInt();
		return this;
	}

	@Override
	public void toBytes (ByteBuf buf) {
		buf.writeInt(totalResonators);
		buf.writeInt(totalCores);
	}

	public static SynchroniseInfo deserialize (ByteBuf buf) {
		SynchroniseInfo info = new SynchroniseInfo();
		return info.fromBytes(buf);
	}
}

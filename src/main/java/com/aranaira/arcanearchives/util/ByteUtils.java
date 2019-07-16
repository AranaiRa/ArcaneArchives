package com.aranaira.arcanearchives.util;

import io.netty.buffer.ByteBuf;

import java.util.UUID;

public class ByteUtils {
	public static void writeUUID (ByteBuf buf, UUID uuid) {
		buf.writeLong(uuid.getMostSignificantBits());
		buf.writeLong(uuid.getLeastSignificantBits());
	}

	public static UUID readUUID (ByteBuf buf) {
		long most = buf.readLong();
		long least = buf.readLong();
		UUID result = new UUID(most, least);
		return result;
	}
}

package com.aranaira.arcanearchives.data;

import com.aranaira.arcanearchives.types.ISerializeByteBuf;
import io.netty.buffer.ByteBuf;

public class HiveMembershipInfo implements ISerializeByteBuf<HiveMembershipInfo> {
	public boolean isOwner = false;
	public boolean inHive = false;

	@Override
	public HiveMembershipInfo fromBytes (ByteBuf buf) {
		isOwner = buf.readBoolean();
		inHive = buf.readBoolean();
		return this;
	}

	@Override
	public void toBytes (ByteBuf buf) {
		buf.writeBoolean(isOwner);
		buf.writeBoolean(inHive);
	}

	public static HiveMembershipInfo deserialize (ByteBuf buf) {
		HiveMembershipInfo info = new HiveMembershipInfo();
		return info.fromBytes(buf);
	}
}

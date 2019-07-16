package com.aranaira.arcanearchives.network;

import com.aranaira.arcanearchives.util.ByteUtils;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.UUID;

public abstract class TileMessage implements IMessage {
	private UUID tileId;

	public TileMessage () {
	}

	public TileMessage (UUID tileId) {
		this.tileId = tileId;
	}

	@Override
	public void fromBytes (ByteBuf buf) {
		tileId = ByteUtils.readUUID(buf);
	}

	@Override
	public void toBytes (ByteBuf buf) {
		ByteUtils.writeUUID(buf, tileId);
	}

	public UUID getTileId () {
		return this.tileId;
	}
}

package com.aranaira.arcanearchives.network;

import com.aranaira.arcanearchives.network.Handlers.BaseHandler;
import com.aranaira.arcanearchives.network.Handlers.ClientHandler;
import com.aranaira.arcanearchives.network.Handlers.ServerHandler;
import com.aranaira.arcanearchives.network.Handlers.TileHandlerServer;
import com.aranaira.arcanearchives.util.ByteUtils;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.UUID;

public class Messages {
	public interface EmptyMessage<T extends IMessage> extends IMessage, BaseHandler<T> {
		default void fromBytes (ByteBuf buf) {
		}

		@Override
		default void toBytes (ByteBuf buf) {
		}
	}

	public interface EmptyMessageServer<T extends IMessage> extends EmptyMessage<T>, ServerHandler<T> {
	}

	public interface EmptyMessageClient<T extends IMessage> extends EmptyMessage<T>, ClientHandler<T> {

	}

	public abstract static class TileMessage implements IMessage {
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

	public static abstract class EmptyTileMessageServer<T extends TileMessage> extends TileMessage implements TileHandlerServer<T>, EmptyMessage<T> {
		public EmptyTileMessageServer () {
		}

		public EmptyTileMessageServer (UUID tileId) {
			super(tileId);
		}
	}
}

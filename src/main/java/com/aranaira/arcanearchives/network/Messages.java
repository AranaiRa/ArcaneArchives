package com.aranaira.arcanearchives.network;

import com.aranaira.arcanearchives.network.Handlers.BaseHandler;
import com.aranaira.arcanearchives.network.Handlers.ClientHandler;
import com.aranaira.arcanearchives.network.Handlers.ServerHandler;
import com.aranaira.arcanearchives.network.Handlers.TileHandlerServer;
import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import com.aranaira.arcanearchives.util.ByteUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
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
		private UUID tileId = null;
		private BlockPos pos = null;
		private int dimension = -9999;

		public TileMessage () {
		}

		public TileMessage (UUID tileId) {
			this.tileId = tileId;
		}

		public TileMessage (BlockPos pos, int dimension) {
			this.pos = pos;
			this.dimension = dimension;
		}

		@Override
		public void fromBytes (ByteBuf buf) {
			boolean uuid = buf.readBoolean();
			if (uuid) {
				tileId = ByteUtils.readUUID(buf);
			} else {
				pos = BlockPos.fromLong(buf.readLong());
				dimension = buf.readInt();
			}
		}

		@Override
		public void toBytes (ByteBuf buf) {
			if (tileId != null) {
				buf.writeBoolean(true);
				ByteUtils.writeUUID(buf, tileId);
			} else {
				buf.writeBoolean(false);
				buf.writeLong(pos.toLong());
				buf.writeInt(dimension);
			}
		}

		public UUID getTileId () {
			return this.tileId;
		}

		public BlockPos getPos () {
			return pos;
		}

		public int getDimension () {
			return dimension;
		}
	}

	public static abstract class EmptyTileMessageServer<T extends TileMessage, V extends ImmanenceTileEntity> extends TileMessage implements TileHandlerServer<T, V>, EmptyMessage<T> {
		public EmptyTileMessageServer () {
		}

		public EmptyTileMessageServer (UUID tileId) {
			super(tileId);
		}
	}

	public static abstract class ConfigPacket<T> implements IMessage {
		protected T value;

		public ConfigPacket () {
		}

		public ConfigPacket (T value) {
			this.value = value;
		}

		public T getValue () {
			return value;
		}

		public void setValue (T value) {
			this.value = value;
		}

		@Override
		@SuppressWarnings("unchecked")
		public void fromBytes (ByteBuf buf) {
			if (value.getClass() == Integer.class) {
				setValue((T) (Integer) buf.readInt());
			} else if (value.getClass() == Boolean.class) {
				setValue((T) (Boolean) buf.readBoolean());
			} else if (value.getClass() == String.class) {
				setValue((T) ByteBufUtils.readUTF8String(buf));
			}
		}

		@Override
		public void toBytes (ByteBuf buf) {
			if (value.getClass() == Integer.class) {
				buf.writeInt((Integer) getValue());
			} else if (value.getClass() == Boolean.class) {
				buf.writeBoolean((Boolean) getValue());
			} else if (value.getClass() == String.class) {
				ByteBufUtils.writeUTF8String(buf, (String) getValue());
			}
		}
	}
}

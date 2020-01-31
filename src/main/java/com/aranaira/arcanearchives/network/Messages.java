/*package com.aranaira.arcanearchives.network;

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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Messages {
	public interface EmptyMessage<T extends IMessage> extends IMessage, BaseHandler<T> {
		@Override
		default void fromBytes (ByteBuf buf) {
		}

		@Override
		default void toBytes (ByteBuf buf) {
		}
	}

	public interface EmptyMessageServer<T extends IMessage> extends EmptyMessage<T>, ServerHandler<T> {}

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
	}

	public static abstract class ConfigIntegerPacket extends ConfigPacket<Integer> {
		public ConfigIntegerPacket () {
		}

		public ConfigIntegerPacket (Integer value) {
			super(value);
		}

		@Override
		public void fromBytes (ByteBuf buf) {
			setValue(buf.readInt());
		}

		@Override
		public void toBytes (ByteBuf buf) {
			buf.writeInt(getValue());
		}
	}

	public static abstract class ConfigBooleanPacket extends ConfigPacket<Boolean> {
		public ConfigBooleanPacket () {
		}

		public ConfigBooleanPacket (Boolean value) {
			super(value);
		}

		@Override
		public void fromBytes (ByteBuf buf) {
			setValue(buf.readBoolean());
		}

		@Override
		public void toBytes (ByteBuf buf) {
			buf.writeBoolean(getValue());
		}
	}

	public static abstract class ConfigStringPacket extends ConfigPacket<String> {
		public ConfigStringPacket () {
		}

		public ConfigStringPacket (String value) {
			super(value);
		}

		@Override
		public void fromBytes (ByteBuf buf) {
			setValue(ByteBufUtils.readUTF8String(buf));
		}

		@Override
		public void toBytes (ByteBuf buf) {
			ByteBufUtils.writeUTF8String(buf, getValue());
		}
	}

	public static abstract class ConfigStringArrayPacket extends ConfigPacket<String[]> {
		public ConfigStringArrayPacket (String[] value) {
			super(value);
		}

		@Override
		public void fromBytes (ByteBuf buf) {
			int count = buf.readInt();
			List<String> values = new ArrayList<>();
			for (int i = 0; i < count; i++) {
				values.add(ByteBufUtils.readUTF8String(buf));
			}
			setValue(values.toArray(new String[0]));
		}

		@Override
		public void toBytes (ByteBuf buf) {
			buf.writeInt(value.length);
			for (String s : value) {
				ByteBufUtils.writeUTF8String(buf, s);
			}
		}
	}
}*/

package com.aranaira.arcanearchives.network;

import com.aranaira.arcanearchives.blocks.Brazier;
import com.aranaira.arcanearchives.network.Messages.EmptyTileMessageServer;
import com.aranaira.arcanearchives.network.Handlers.TileHandlerServer;
import com.aranaira.arcanearchives.tileentities.BrazierTileEntity;
import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class PacketBrazier {
	public static class SetRadius extends Messages.TileMessage {
		private int radius;

		public SetRadius (int radius, UUID tileId) {
			super(tileId);
			this.radius = radius;
		}

		public SetRadius () {
		}

		@Override
		public void fromBytes (ByteBuf buf) {
			super.fromBytes(buf);
			radius = buf.readInt();
		}

		@Override
		public void toBytes (ByteBuf buf) {
			super.toBytes(buf);
			buf.writeInt(radius);
		}

		public static class Handler implements TileHandlerServer<SetRadius, BrazierTileEntity> {
			@Override
			public void processMessage (SetRadius message, MessageContext ctx, BrazierTileEntity tile) {
				tile.setRadius(message.radius);
				tile.markDirty();
				tile.defaultServerSideUpdate();
			}
		}
	}

	public static class SetSubnetworkMode extends Messages.TileMessage {
		private boolean mode;

		public SetSubnetworkMode () {
		}

		public SetSubnetworkMode (boolean mode, UUID tileId) {
			super(tileId);
			this.mode = mode;
		}

		@Override
		public void fromBytes (ByteBuf buf) {
			super.fromBytes(buf);
			mode = buf.readBoolean();
		}

		@Override
		public void toBytes (ByteBuf buf) {
			super.toBytes(buf);
			buf.writeBoolean(mode);
		}

		public static class Handler implements TileHandlerServer<SetSubnetworkMode, BrazierTileEntity> {
			@Override
			public void processMessage (SetSubnetworkMode message, MessageContext ctx, BrazierTileEntity tile) {
				tile.setNetworkMode(message.mode);
				tile.markDirty();
				tile.defaultServerSideUpdate();
			}
		}
	}

	public static class IncrementRadius extends EmptyTileMessageServer<IncrementRadius, BrazierTileEntity> {
		public IncrementRadius (UUID tileId) {
			super(tileId);
		}

		public IncrementRadius () {
		}

		@Override
		public void processMessage (IncrementRadius message, MessageContext ctx, BrazierTileEntity tile) {
			tile.increaseRadius();
			tile.markDirty();
			tile.defaultServerSideUpdate();
		}
	}

	public static class DecrementRadius extends EmptyTileMessageServer<DecrementRadius, BrazierTileEntity> {
		public DecrementRadius (UUID tileId) {
			super(tileId);
		}

		public DecrementRadius () {
		}

		@Override
		public void processMessage (DecrementRadius message, MessageContext ctx, BrazierTileEntity tile) {
			tile.reduceRadius();
			tile.markDirty();
			tile.defaultServerSideUpdate();
		}
	}
}

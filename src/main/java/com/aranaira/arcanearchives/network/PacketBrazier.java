package com.aranaira.arcanearchives.network;

import com.aranaira.arcanearchives.network.NetworkHandler.EmptyTileMessageServer;
import com.aranaira.arcanearchives.network.NetworkHandler.TileHandlerServer;
import com.aranaira.arcanearchives.tileentities.BrazierTileEntity;
import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class PacketBrazier {
	public static class SetRadius extends TileMessage {
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

		public static class Handler implements TileHandlerServer<SetRadius> {
			@Override
			public void processMessage (SetRadius message, MessageContext ctx, ImmanenceTileEntity tile) {
				if (!(tile instanceof BrazierTileEntity)) {
					return;
				}

				BrazierTileEntity bte = (BrazierTileEntity) tile;
				bte.setRadius(message.radius);
				bte.markDirty();
				bte.defaultServerSideUpdate();
			}
		}
	}

	public static class SetSubnetworkMode extends TileMessage {
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

		public static class Handler implements TileHandlerServer<SetSubnetworkMode> {
			@Override
			public void processMessage (SetSubnetworkMode message, MessageContext ctx, ImmanenceTileEntity tile) {
				if (!(tile instanceof BrazierTileEntity)) {
					return;
				}

				BrazierTileEntity bte = (BrazierTileEntity) tile;
				bte.setNetworkMode(message.mode);
				bte.markDirty();
				bte.defaultServerSideUpdate();
			}
		}
	}

	public static class IncrementRadius extends EmptyTileMessageServer<IncrementRadius> {
		public IncrementRadius (UUID tileId) {
			super(tileId);
		}

		public IncrementRadius () {
		}

		@Override
		public void processMessage (IncrementRadius message, MessageContext ctx, ImmanenceTileEntity tile) {
			if (!(tile instanceof BrazierTileEntity)) {
				return;
			}

			BrazierTileEntity bte = (BrazierTileEntity) tile;
			bte.increaseRadius();
			bte.markDirty();
			bte.defaultServerSideUpdate();
		}
	}

	public static class DecrementRadius extends EmptyTileMessageServer<DecrementRadius> {
		public DecrementRadius (UUID tileId) {
			super(tileId);
		}

		public DecrementRadius () {
		}

		@Override
		public void processMessage (DecrementRadius message, MessageContext ctx, ImmanenceTileEntity tile) {
			if (!(tile instanceof BrazierTileEntity)) {
				return;
			}

			BrazierTileEntity bte = (BrazierTileEntity) tile;
			bte.reduceRadius();
			bte.markDirty();
			bte.defaultServerSideUpdate();
		}
	}
}

package com.aranaira.arcanearchives.network;

import com.aranaira.arcanearchives.tileentities.RadiantChestTileEntity;
import com.aranaira.arcanearchives.util.WorldUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketRadiantChest
{
	public static class SetName implements IMessage {

		private BlockPos pos;
		private String name;
		private int dimension;

		@SuppressWarnings("unused")
		public SetName() {
		}

		public SetName(BlockPos pos, String name, int dimensionID) {
			this.pos = pos;
			this.name = (name == null) ? "" : name;
			dimension = dimensionID;
		}

		@Override
		public void fromBytes(ByteBuf buf) {
			name = ByteBufUtils.readUTF8String(buf);
			pos = BlockPos.fromLong(buf.readLong());
			dimension = buf.readInt();
		}

		@Override
		public void toBytes(ByteBuf buf) {
			ByteBufUtils.writeUTF8String(buf, name);
			buf.writeLong(pos.toLong());
			buf.writeInt(dimension);
		}

		public static class Handler extends NetworkHandler.ServerHandler<SetName> {
			public void processMessage(SetName message, MessageContext ctx) {
				RadiantChestTileEntity te = WorldUtil.getTileEntity(RadiantChestTileEntity.class, message.dimension, message.pos);
				if(te != null) {
					te.setChestName(message.name);
				}
			}
		}
	}
}

package com.aranaira.arcanearchives.network;

import com.aranaira.arcanearchives.tileentities.RadiantChestTileEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketRadiantChest
{
	public static class SetName implements IMessage
	{

		private BlockPos mPos;
		private String mName;
		private int mDimensionID;

		public SetName()
		{
		}

		public SetName(BlockPos pos, String name, int dimensionID)
		{
			mPos = pos;
			mName = (name == null) ? "" : name;
			mDimensionID = dimensionID;
		}

		@Override
		public void fromBytes(ByteBuf buf)
		{
			mName = ByteBufUtils.readUTF8String(buf);
			mPos = BlockPos.fromLong(buf.readLong());
			mDimensionID = buf.readInt();

		}

		@Override
		public void toBytes(ByteBuf buf)
		{
			ByteBufUtils.writeUTF8String(buf, mName);
			buf.writeLong(mPos.toLong());
			buf.writeInt(mDimensionID);
		}

		public static class SetNameHandler implements IMessageHandler<SetName, IMessage>
		{

			@Override
			public IMessage onMessage(final SetName message, final MessageContext ctx)
			{
				FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));

				return null;
			}

			private void processMessage(SetName message, MessageContext ctx)
			{
				MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
				if(server != null)
				{
					World world = DimensionManager.getWorld(message.mDimensionID);
					TileEntity te = world.getTileEntity(message.mPos);
					if(te instanceof RadiantChestTileEntity)
					{
						((RadiantChestTileEntity) te).setChestName(message.mName);
					}
				}
			}
		}
	}
}

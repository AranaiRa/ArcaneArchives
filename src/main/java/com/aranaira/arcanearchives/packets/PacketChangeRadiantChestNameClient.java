package com.aranaira.arcanearchives.packets;

import com.aranaira.arcanearchives.tileentities.RadiantChestTileEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.nio.charset.Charset;

public class PacketChangeRadiantChestNameClient implements IMessage
{
	private int mDimension;
	private String mName;
	private BlockPos mPos;

	public PacketChangeRadiantChestNameClient()
	{
	}

	public PacketChangeRadiantChestNameClient(BlockPos pos, int dimension, String name)
	{
		mDimension = dimension;
		mName = name;
		mPos = pos;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		mDimension = buf.readInt();
		mName = (String) buf.readCharSequence(buf.readInt(), Charset.defaultCharset());
		mPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(mDimension);
		buf.writeInt(mName.length());
		buf.writeCharSequence(mName, Charset.defaultCharset());
		buf.writeInt(mPos.getX());
		buf.writeInt(mPos.getY());
		buf.writeInt(mPos.getZ());
	}

	public static class PacketChangeRadiantChestNameClientHandler implements IMessageHandler<PacketChangeRadiantChestNameClient, IMessage>
	{
		public IMessage onMessage(final PacketChangeRadiantChestNameClient message, final MessageContext ctx)
		{
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));

			return null;
		}

		private void processMessage(PacketChangeRadiantChestNameClient message, MessageContext ctx)
		{
			if(Minecraft.getMinecraft().world.provider.getDimension() == message.mDimension)
			{
				TileEntity te = Minecraft.getMinecraft().world.getTileEntity(message.mPos);
				if(te instanceof RadiantChestTileEntity)
				{
					((RadiantChestTileEntity) te).chestName = message.mName;
					((RadiantChestTileEntity) te).markDirty();
					te.updateContainingBlockInfo();
				}
			}
		}
	}
}

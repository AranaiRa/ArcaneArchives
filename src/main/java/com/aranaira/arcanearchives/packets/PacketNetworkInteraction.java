package com.aranaira.arcanearchives.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.nio.charset.Charset;

public class PacketNetworkInteraction implements IMessage
{
	public static int INSERT = 0;
	public static int REMOVE = 1;
	public static int GET_LIST = 2;
	public static int GET_FILTERED_LIST = 3;

	//TODO: Have packet get sent to the matrix core | Have that read the packet and deal with whatever it needs to do.

	public PacketNetworkInteraction()
	{
	}

	public ItemStack mItem;
	public int mAction;
	public String mSearchText;

	public PacketNetworkInteraction(ItemStack item, int action, String searchText)
	{
		mItem = item;
		mAction = action;
		mSearchText = searchText;
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeItemStack(buf, mItem);
		buf.writeByte(mAction);
		buf.writeShort(mSearchText.length());
		buf.writeCharSequence(mSearchText, Charset.defaultCharset());
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		mItem = ByteBufUtils.readItemStack(buf);
		mAction = buf.readByte();
		int i = buf.readShort();
		buf.readCharSequence(i, Charset.defaultCharset());
	}

	public static class PacketNetworkInteractionHandler implements IMessageHandler<PacketNetworkInteraction, IMessage>
	{

		@Override
		public IMessage onMessage(final PacketNetworkInteraction message, final MessageContext ctx)
		{
			EntityPlayerMP serverPlayer = ctx.getServerHandler().player;

			int amount = message.mAction;

			return null;
		}
	}
}

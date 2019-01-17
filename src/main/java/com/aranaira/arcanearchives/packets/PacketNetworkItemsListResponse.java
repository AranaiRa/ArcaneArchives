package com.aranaira.arcanearchives.packets;

import com.aranaira.arcanearchives.common.ManifestItemHandler;
import com.aranaira.arcanearchives.util.RadiantChestPlaceHolder;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketNetworkItemsListResponse implements IMessage {

	@Override
	public void fromBytes(ByteBuf buf) {

	}

	@Override
	public void toBytes(ByteBuf buf) {

	}

	public static class PacketNetworkItemsListResponseHandler implements IMessageHandler<PacketNetworkItemsListResponse, IMessage>
	{

		@Override 
		public IMessage onMessage(final PacketNetworkItemsListResponse message, final MessageContext ctx) 
		{
		    FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));

		    return null;
		}
		
		private void processMessage(PacketNetworkItemsListResponse message, MessageContext ctx)
		{
			
		}
	}
}

package com.aranaira.arcanearchives.network;

import com.aranaira.arcanearchives.init.ItemRegistry;
import com.aranaira.arcanearchives.items.RadiantAmphoraItem.AmphoraUtil;
import com.aranaira.arcanearchives.network.NetworkHandler.ServerHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketRadiantAmphora implements IMessage {

	@Override
	public void fromBytes (ByteBuf buf) {
	}

	@Override
	public void toBytes (ByteBuf buf) {
	}

	public static class Handler extends ServerHandler<PacketRadiantAmphora> {
		@Override
		public void processMessage (PacketRadiantAmphora packet, MessageContext context) {
			EntityPlayerMP player = context.getServerHandler().player;
			ItemStack stack = player.getHeldItemMainhand();
			if (stack.getItem() == ItemRegistry.RADIANT_AMPHORA) {
				AmphoraUtil util = new AmphoraUtil(stack);
				util.toggleMode();
			}
		}
	}
}

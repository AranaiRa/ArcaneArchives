package com.aranaira.arcanearchives.network;

import com.aranaira.arcanearchives.AAGuiHandler;
import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.inventory.handlers.GemSocketHandler;
import com.aranaira.arcanearchives.network.NetworkHandler.ServerHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketGemSocket implements IMessage {
	public PacketGemSocket () {
	}

	@Override
	public void fromBytes (ByteBuf buf) {
	}

	@Override
	public void toBytes (ByteBuf buf) {
	}

	public static class Handler extends ServerHandler<PacketGemSocket> {
		@Override
		public void processMessage (PacketGemSocket message, MessageContext ctx) {
			EntityPlayer player = ctx.getServerHandler().player;
			ItemStack stack = GemSocketHandler.findSocket(player);
			if (!stack.isEmpty()) {
				player.openGui(ArcaneArchives.instance, AAGuiHandler.BAUBLE_GEMSOCKET, player.world, (int) player.posX, (int) player.posY, (int) player.posZ);
			}
		}
	}
}

package com.aranaira.arcanearchives.network;

import com.aranaira.arcanearchives.items.gems.*;
import com.aranaira.arcanearchives.network.NetworkHandler.ServerHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketArcaneGemToggle implements IMessage {

	public PacketArcaneGemToggle () {
	}

	@Override
	public void fromBytes (ByteBuf buf) {
	}

	@Override
	public void toBytes (ByteBuf buf) {
	}

	public static class Handler extends ServerHandler<PacketArcaneGemToggle> {
		@Override
		public void processMessage (PacketArcaneGemToggle packet, MessageContext context) {
			//ArcaneArchives.logger.info("Received toggle packet");
			EntityPlayerMP player = context.getServerHandler().player;
			GemUtil.swapToggle(player.getHeldItemMainhand());
		}
	}
}

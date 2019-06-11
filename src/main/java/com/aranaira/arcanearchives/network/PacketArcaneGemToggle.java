package com.aranaira.arcanearchives.network;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.items.gems.ArcaneGemItem;
import com.aranaira.arcanearchives.network.NetworkHandler.ServerHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.Random;

public class PacketArcaneGemToggle implements IMessage {

	public PacketArcaneGemToggle() {
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
			ArcaneArchives.logger.info("Received toggle packet");
			EntityPlayerMP player = context.getServerHandler().player;
			ArcaneGemItem.GemUtil.swapToggle(player.getHeldItemMainhand());
		}
	}
}

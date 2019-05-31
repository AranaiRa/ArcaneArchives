package com.aranaira.arcanearchives.network;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.init.ItemRegistry;
import com.aranaira.arcanearchives.items.RadiantAmphoraItem.AmphoraUtil;
import com.aranaira.arcanearchives.items.gems.ArcaneGemItem;
import com.aranaira.arcanearchives.network.NetworkHandler.ServerHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketDoParticles implements IMessage {

	//private Vec3d pos1, pos2;
	//ArcaneGemItem.GemCut cut;
	//ArcaneGemItem.GemColor color;

	public PacketDoParticles() {
	}

	public PacketDoParticles(Vec3d pos1, Vec3d pos2) {
	}

	@Override
	public void fromBytes (ByteBuf buf) {
	}

	@Override
	public void toBytes (ByteBuf buf) {
	}

	public static class Handler extends ServerHandler<PacketDoParticles> {
		@Override
		public void processMessage (PacketDoParticles packet, MessageContext context) {
			ArcaneArchives.logger.info("Received particles packet");
		}
	}
}

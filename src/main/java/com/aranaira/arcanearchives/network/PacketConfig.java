package com.aranaira.arcanearchives.network;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.config.client.ManifestConfig;
import com.aranaira.arcanearchives.data.NetworkHelper;
import com.aranaira.arcanearchives.data.ServerNetwork;
import com.aranaira.arcanearchives.network.NetworkHandler.ClientHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketConfig {
	public static class MaxDistance implements IMessage {
		int distance;

		public MaxDistance () {
			distance = 0;
		}

		public MaxDistance (int distance) {
			this.distance = distance;
		}

		@Override
		public void fromBytes (ByteBuf buf) {
			this.distance = buf.readInt();
		}

		@Override
		public void toBytes (ByteBuf buf) {
			buf.writeInt(this.distance);
		}

		public static class Handler extends NetworkHandler.ServerHandler<MaxDistance> {
			public void processMessage (MaxDistance message, MessageContext context) {
				MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
				if (server == null) {
					ArcaneArchives.logger.error("Server was null when processing sync packet");
					return;
				}

				EntityPlayerMP player = context.getServerHandler().player;

				ServerNetwork network = NetworkHelper.getServerNetwork(player.getUniqueID(), server.getWorld(0));
				if (network == null) {
					ArcaneArchives.logger.error(() -> "Network was null when processing sync packet for " + player.getUniqueID());
					return;
				}

				network.setMaxDistance(message.distance);
			}
		}
	}

	public static class RequestMaxDistance implements IMessage {
		public RequestMaxDistance () {
		}

		@Override
		public void fromBytes (ByteBuf buf) {
		}

		@Override
		public void toBytes (ByteBuf buf) {
		}

		public static class Handler extends ClientHandler<RequestMaxDistance> {
			@Override
			public void processMessage (RequestMaxDistance message, MessageContext ctx) {
				MaxDistance packet = new MaxDistance(ManifestConfig.MaxDistance);
				NetworkHandler.CHANNEL.sendToServer(packet);
			}
		}
	}
}

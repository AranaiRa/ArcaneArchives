package com.aranaira.arcanearchives.network;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.config.ConfigHandler;
import com.aranaira.arcanearchives.data.NetworkHelper;
import com.aranaira.arcanearchives.data.ServerNetwork;
import com.aranaira.arcanearchives.network.NetworkHandler.EmptyMessageClient;
import com.aranaira.arcanearchives.network.NetworkHandler.EmptyMessageServer;
import com.aranaira.arcanearchives.network.NetworkHandler.ServerHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import scala.tools.nsc.doc.model.Def;

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

		public static class Handler implements ServerHandler<MaxDistance> {
			@Override
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

	public static class RequestMaxDistance implements EmptyMessageClient<RequestMaxDistance> {
		public RequestMaxDistance () {
		}

		@Override
		public void processMessage (RequestMaxDistance message, MessageContext ctx) {
			MaxDistance packet = new MaxDistance(ConfigHandler.ManifestConfig.MaxDistance);
			NetworkHandler.CHANNEL.sendToServer(packet);
		}
	}

	public static class RequestDefaultRoutingType implements EmptyMessageClient<RequestDefaultRoutingType> {
		public RequestDefaultRoutingType () {
		}

		@Override
		public void processMessage (RequestDefaultRoutingType message, MessageContext ctx) {
			DefaultRoutingType packet = new DefaultRoutingType(ConfigHandler.defaultRoutingNoNewItems);
			NetworkHandler.CHANNEL.sendToServer(packet);
		}
	}

	public static class DefaultRoutingType implements IMessage {
		private boolean value;

		public DefaultRoutingType () {
		}

		public DefaultRoutingType (boolean value) {
			this.value = value;
		}

		@Override
		public void fromBytes (ByteBuf buf) {
			this.value = buf.readBoolean();
		}

		@Override
		public void toBytes (ByteBuf buf) {
			buf.writeBoolean(this.value);
		}

		public static class Handler implements ServerHandler<DefaultRoutingType> {
			@Override
			public void processMessage (DefaultRoutingType message, MessageContext context) {
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

				network.setNoNewDefault(message.value);
			}
		}
	}
}

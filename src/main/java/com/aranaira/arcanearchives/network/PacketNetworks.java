package com.aranaira.arcanearchives.network;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.data.ClientNetwork;
import com.aranaira.arcanearchives.data.NetworkHelper;
import com.aranaira.arcanearchives.data.ServerNetwork;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class PacketNetworks
{
	public enum SynchroniseType
	{
		INVALID("invalid"), DATA("data"), MANIFEST("manifest"), NETWORK_ITEMS("network_items");

		private String key;

		SynchroniseType(String key)
		{
			this.key = key;
		}

		public static SynchroniseType fromKey(String key)
		{
			for(SynchroniseType type : values())
			{
				if(type.key().equals(key)) return type;
			}

			return INVALID;
		}

		public String key()
		{
			return this.key;
		}
	}

	public static class Request implements IMessage
	{
		SynchroniseType type;
		UUID playerId;

		public Request()
		{
			this.type = SynchroniseType.DATA;
			this.playerId = null;
		}

		public Request(SynchroniseType type, UUID playerId)
		{
			this.type = type;
			this.playerId = playerId;
		}

		@Override
		public void fromBytes(ByteBuf buf)
		{
			this.type = SynchroniseType.fromKey(ByteBufUtils.readUTF8String(buf));
			this.playerId = UUID.fromString(ByteBufUtils.readUTF8String(buf));
		}

		@Override
		public void toBytes(ByteBuf buf)
		{
			ByteBufUtils.writeUTF8String(buf, this.type.key());
			ByteBufUtils.writeUTF8String(buf, this.playerId.toString());
		}

		public static class Handler extends NetworkHandler.ServerHandler<Request>
		{
			public void processMessage(Request message, MessageContext context)
			{
				MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
				if(server == null)
				{
					ArcaneArchives.logger.error("Server was null when processing sync packet");
					return;
				}

				ServerNetwork network = NetworkHelper.getServerNetwork(message.playerId, server.getWorld(0));
				if(network == null)
				{
					ArcaneArchives.logger.error(() -> "Network was null when processing sync packet for " + message.playerId);
					return;
				}

				NBTTagCompound output = null;

				switch(message.type)
				{
					case DATA:
						output = network.buildSynchroniseData();
						break;
					case MANIFEST:
						output = network.buildSynchroniseManifest();
						break;
				}

				Response response = new Response(message.type, message.playerId, output);
				EntityPlayerMP player = server.getPlayerList().getPlayerByUUID(message.playerId);

				if(output != null)
				{
					NetworkHandler.CHANNEL.sendTo(response, player);
				}
			}
		}
	}

	public static class Response extends Request
	{
		private NBTTagCompound data;

		public Response()
		{
			super();
		}

		public Response(SynchroniseType type, UUID player, NBTTagCompound data)
		{
			super(type, player);
			this.data = data;

		}

		@Override
		public void fromBytes(ByteBuf buf)
		{
			super.fromBytes(buf);
			this.data = ByteBufUtils.readTag(buf);
		}

		@Override
		public void toBytes(ByteBuf buf)
		{
			super.toBytes(buf);
			ByteBufUtils.writeTag(buf, this.data);
		}

		public static class Handler extends NetworkHandler.ClientHandler<Response>
		{
			public void processMessage(Response message, MessageContext context)
			{
				ClientNetwork network = NetworkHelper.getClientNetwork(message.playerId);

				switch(message.type)
				{
					case DATA:
						ArcaneArchives.logger.info("[DEBUG ONLY MESSAGE] Received synchronise packet.");
						network.deserializeData(message.data);
						break;
					case MANIFEST:
						network.deserializeManifest(message.data);
						break;
				}
			}
		}
	}
}

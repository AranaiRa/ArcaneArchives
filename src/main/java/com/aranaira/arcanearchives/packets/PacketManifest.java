package com.aranaira.arcanearchives.packets;

import com.aranaira.arcanearchives.data.ArcaneArchivesClientNetwork;
import com.aranaira.arcanearchives.data.ArcaneArchivesNetwork;
import com.aranaira.arcanearchives.data.NetworkHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class PacketManifest
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

	public static class PacketSynchroniseRequest implements IMessage
	{
		SynchroniseType type;
		UUID playerId;

		public PacketSynchroniseRequest()
		{
			this.type = SynchroniseType.DATA;
			this.playerId = null;
		}

		public PacketSynchroniseRequest(SynchroniseType type, UUID playerId)
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

		public static class PacketSynchroniseRequestHandler implements IMessageHandler<PacketSynchroniseRequest, IMessage>
		{

			@Override
			public IMessage onMessage(PacketSynchroniseRequest message, MessageContext ctx)
			{
				FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));

				return null;
			}

			// Fired on the server
			private void processMessage(PacketSynchroniseRequest message, MessageContext context)
			{
				MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
				if(server == null) return;

				ArcaneArchivesNetwork network = NetworkHelper.getArcaneArchivesNetwork(message.playerId);
				if(network == null) return;

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

				PacketSynchroniseResponse response = new PacketSynchroniseResponse(message.type, message.playerId, output);
				EntityPlayerMP player = server.getPlayerList().getPlayerByUUID(message.playerId);

				if(output != null)
				{
					AAPacketHandler.CHANNEL.sendTo(response, player);
				}
			}
		}
	}

	public static class PacketSynchroniseResponse extends PacketSynchroniseRequest
	{

		private NBTTagCompound data;

		public PacketSynchroniseResponse()
		{
			super();
		}

		public PacketSynchroniseResponse(SynchroniseType type, UUID player, NBTTagCompound data)
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

		public static class PacketSynchroniseResponseHandler implements IMessageHandler<PacketSynchroniseResponse, IMessage>
		{

			@Override
			public IMessage onMessage(PacketSynchroniseResponse message, MessageContext ctx)
			{
				FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));

				return null;
			}

			// This arrives on the client side!
			private void processMessage(PacketSynchroniseResponse message, MessageContext context)
			{
				ArcaneArchivesClientNetwork network = NetworkHelper.getArcaneArchivesClientNetwork(message.playerId);

				switch(message.type)
				{
					case DATA:
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

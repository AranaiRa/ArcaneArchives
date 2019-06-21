package com.aranaira.arcanearchives.network;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.data.ClientNetwork;
import com.aranaira.arcanearchives.data.HiveNetwork;
import com.aranaira.arcanearchives.data.NetworkHelper;
import com.aranaira.arcanearchives.data.ServerNetwork;
import com.typesafe.config.ConfigException.Null;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketNetworks {
	public enum SynchroniseType {
		INVALID("invalid"), DATA("data"), MANIFEST("manifest"), HIVE_STATUS("hive_status");

		private String key;

		SynchroniseType (String key) {
			this.key = key;
		}

		public static SynchroniseType fromOrdinal (int i) {
			for (SynchroniseType type : values()) {
				if (type.ordinal() == i) {
					return type;
				}
			}

			return INVALID;
		}

		public String key () {
			return this.key;
		}
	}

	public static class Request implements IMessage {
		SynchroniseType type;

		public Request () {
			this.type = SynchroniseType.DATA;
		}

		public Request (SynchroniseType type) {
			this.type = type;
		}

		@Override
		public void fromBytes (ByteBuf buf) {
			this.type = SynchroniseType.fromOrdinal(buf.readShort());
		}

		@Override
		public void toBytes (ByteBuf buf) {
			buf.writeShort(this.type.ordinal());
		}

		public static class Handler extends NetworkHandler.ServerHandler<Request> {
			public void processMessage (Request message, MessageContext context) {
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

				NBTTagCompound output = null;

				switch (message.type) {
					case DATA:
						output = network.buildSynchroniseData();
						break;
					case HIVE_STATUS:
						// TODO:
						// is_member, is_owner
						output = network.buildHiveMembershipData();
						break;
					case MANIFEST:
						if (network.isHiveMember()) {
							HiveNetwork hive = network.getHiveNetwork();
							output = hive.buildHiveManifest(player);
						} else {
							output = network.buildSynchroniseManifest();
						}
						break;
				}

				Response response = new Response(message.type, output);

				if (output != null) {
					NetworkHandler.CHANNEL.sendTo(response, player);
				}
			}
		}
	}

	public static class Response extends Request {
		private NBTTagCompound data;

		public Response () {
			super();
		}

		public Response (SynchroniseType type, NBTTagCompound data) {
			super(type);
			this.data = data;
		}

		@Override
		public void fromBytes (ByteBuf buf) {
			super.fromBytes(buf);
			this.data = ByteBufUtils.readTag(buf);
		}

		@Override
		public void toBytes (ByteBuf buf) {
			super.toBytes(buf);
			ByteBufUtils.writeTag(buf, this.data);
		}

		public static class Handler extends NetworkHandler.ClientHandler<Response> {
			@SideOnly(Side.CLIENT)
			public void processMessage (Response message, MessageContext context) {
				EntityPlayer player;
				try {
					player = Minecraft.getMinecraft().player;
				} catch (NullPointerException e) {
					System.out.println("Exception: missing player or Minecraft when handling packet: " + this.getClass().toString());
					return;
				}

				ClientNetwork network = NetworkHelper.getClientNetwork(player.getUniqueID());

				switch (message.type) {
					case DATA:
						network.deserializeData(message.data);
						break;
					case MANIFEST:
						network.deserializeManifest(message.data);
						break;
					case HIVE_STATUS:
						network.deserializeHive(message.data);
						break;
				}
			}
		}
	}
}

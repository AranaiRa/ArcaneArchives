package com.aranaira.arcanearchives.network;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.data.ClientNetwork;
import com.aranaira.arcanearchives.data.HiveNetwork;
import com.aranaira.arcanearchives.data.NetworkHelper;
import com.aranaira.arcanearchives.data.ServerNetwork;
import com.aranaira.arcanearchives.network.PacketNetworks.SynchroniseType;
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
}

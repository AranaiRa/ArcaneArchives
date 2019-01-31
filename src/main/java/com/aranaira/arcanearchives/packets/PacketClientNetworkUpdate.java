package com.aranaira.arcanearchives.packets;

import com.aranaira.arcanearchives.data.ArcaneArchivesClientNetwork;
import com.aranaira.arcanearchives.data.NetworkHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class PacketClientNetworkUpdate implements IMessage
{
	private NBTTagCompound sync;
	private UUID id;

	public PacketClientNetworkUpdate()
	{
	}

	public PacketClientNetworkUpdate(NBTTagCompound data, UUID playerId)
	{
		this.sync = data;
		this.id = id;
	}

	public void fromBytes(ByteBuf buf)
	{
		ByteBufUtils.writeTag(buf, sync);
	}

	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.readTag(buf);
	}

	public static class PacketClientNetworkUpdateHandler implements IMessageHandler<PacketClientNetworkUpdate, IMessage> {

		@Override
		public IMessage onMessage(PacketClientNetworkUpdate message, MessageContext ctx)
		{
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));

			return null;
		}

		private void processMessage (PacketClientNetworkUpdate message, MessageContext context) {
			ArcaneArchivesClientNetwork network = NetworkHelper.getArcaneArchivesClientNetwork(message.id);
			network.deserializeNBT(message.sync);
		}
	}
}

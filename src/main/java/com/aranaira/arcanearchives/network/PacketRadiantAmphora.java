package com.aranaira.arcanearchives.network;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.init.ItemRegistry;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketRadiantAmphora implements IMessage {

	@Override
	public void fromBytes (ByteBuf buf) { }

	@Override
	public void toBytes (ByteBuf buf) { }

	public static class Handler implements IMessageHandler<PacketRadiantAmphora, IMessage> {
		@Override
		public IMessage onMessage(PacketRadiantAmphora packet, MessageContext context) {
			FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> handleMessage(packet, context));

			return null;
		}

		private void handleMessage(PacketRadiantAmphora packet, MessageContext context) {
			EntityPlayerMP player = context.getServerHandler().player;
			ItemStack stack = player.getHeldItemMainhand();
			if(stack.getItem() == ItemRegistry.RADIANT_AMPHORA) {
				NBTTagCompound nbt = stack.getTagCompound();
				if(nbt.hasKey("isEmptyMode")) {
					boolean emptyMode = nbt.getBoolean("isEmptyMode");
					emptyMode = !emptyMode;
					nbt.setBoolean("isEmptyMode", emptyMode);
				}
			}
		}
	}
}

package com.aranaira.arcanearchives.network;

import java.io.IOException;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import com.aranaira.arcanearchives.inventory.ContainerNetherChest;
import com.aranaira.arcanearchives.util.NetworkUtils;

public class MessageSyncExtendedSlotContents implements IMessage {

	private int windowId = 0;
	private int slot = 0;
	private ItemStack stack = ItemStack.EMPTY;
	
	public MessageSyncExtendedSlotContents() {
		
	}
	
	public MessageSyncExtendedSlotContents(int windowId, int slot, ItemStack stack) {
		this.windowId = windowId;
		this.slot = slot;
		this.stack = stack.copy();
	}
	
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.windowId = buf.readByte();
        this.slot = buf.readInt();
        try {
			this.stack = NetworkUtils.readExtendedItemStack(buf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeByte(this.windowId);
        buf.writeInt(this.slot);
		NetworkUtils.writeExtendedItemStack(buf, stack);
	}
	
	public static class Handler implements IMessageHandler<MessageSyncExtendedSlotContents, IMessage> {

		@SideOnly(Side.CLIENT)
		@Override
		public IMessage onMessage(final MessageSyncExtendedSlotContents message, final MessageContext ctx) {
			Minecraft mc = FMLClientHandler.instance().getClient();
			EntityPlayer player = mc.player;
			
			if (player == null) return null;
			
			mc.addScheduledTask(new Runnable() {
                public void run() {
                    processMessage(message, player);
                }
            });

			 
			return null;
		}
		
		public void processMessage(final MessageSyncExtendedSlotContents message, EntityPlayer player) {
			if (player.openContainer instanceof ContainerNetherChest && message.windowId == player.openContainer.windowId) {
                player.openContainer.inventorySlots.get(message.slot).putStack(message.stack);
            }
		}
		
	}

}

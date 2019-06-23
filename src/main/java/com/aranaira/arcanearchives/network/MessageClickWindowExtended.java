package com.aranaira.arcanearchives.network;

import java.io.IOException;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketConfirmTransaction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import com.aranaira.arcanearchives.inventory.ContainerNetherChest;
import com.aranaira.arcanearchives.util.NetworkUtils;

public class MessageClickWindowExtended implements IMessage {
	
    private int windowId;
    private int slot;
    private int mouseButton;
    private short transactionId;
    private ItemStack clickedItem = ItemStack.EMPTY;
    private ClickType mode;
    
    public MessageClickWindowExtended() {
    	
    }
    
    public MessageClickWindowExtended(int windowId, int slot, int mouseButton, ClickType mode, ItemStack clickedItem, short transactionId) {
    	this.windowId = windowId;
    	this.slot = slot;
    	this.mouseButton = mouseButton;
    	this.mode = mode;
    	this.clickedItem = clickedItem;
    	this.transactionId = transactionId;
    }
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.windowId = buf.readByte();
		this.slot = buf.readInt();
		this.mouseButton = buf.readByte();
		this.transactionId = buf.readShort();
		this.mode = ClickType.values()[buf.readInt()];
		try {
			this.clickedItem = NetworkUtils.readExtendedItemStack(buf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeByte(this.windowId);
		buf.writeInt(this.slot);
		buf.writeByte(this.mouseButton);
		buf.writeShort(this.transactionId);
		buf.writeInt(this.mode.ordinal());
		NetworkUtils.writeExtendedItemStackFromClientToServer(buf, this.clickedItem);
	}
	
	public static class Handler implements IMessageHandler<MessageClickWindowExtended, IMessage> {

		@Override
		public IMessage onMessage(final MessageClickWindowExtended message, final MessageContext ctx) {
			EntityPlayerMP player = ctx.getServerHandler().player;
			
			if (player == null) return null;
			
			player.getServerWorld().addScheduledTask(new Runnable() {
                public void run() {
                    processMessage(message, player);
                }
            });
			
			return null;
		}
		
		public void processMessage(final MessageClickWindowExtended message, EntityPlayerMP player) {
			player.markPlayerActive();
			
			Container container = player.openContainer;
			
			if (container.windowId == message.windowId && container.getCanCraft(player)) {
				if (player.isSpectator()) {
	                if (container instanceof ContainerNetherChest) {
        				((ContainerNetherChest) container).syncInventory(player);
        			} else {
        				NonNullList<ItemStack> nonnulllist = NonNullList.<ItemStack>create();
    	                for (int i = 0; i < container.inventorySlots.size(); ++i) {
    	                    nonnulllist.add(container.inventorySlots.get(i).getStack());
    	                }
        				player.sendAllContents(container, nonnulllist);
        			}
	            } else {
	            	ItemStack itemstack2 = container.slotClick(message.slot, message.mouseButton, message.mode, player);

	                if (ItemStack.areItemStacksEqualUsingNBTShareTag(message.clickedItem, itemstack2)) {
	                    player.connection.sendPacket(new SPacketConfirmTransaction(message.windowId, message.transactionId, true));
	                    player.isChangingQuantityOnly = true;
	                    player.openContainer.detectAndSendChanges();
	                    player.updateHeldItem();
	                    player.isChangingQuantityOnly = false;
	                } else {
	                    //this.pendingTransactions.addKey(this.player.openContainer.windowId, Short.valueOf(packetIn.getActionNumber()));
	                    //this.player.connection.sendPacket(new SPacketConfirmTransaction(packetIn.getWindowId(), packetIn.getActionNumber(), false));
	                    //this.player.openContainer.setCanCraft(this.player, false);
	                    if (container instanceof ContainerNetherChest) {
	        				((ContainerNetherChest) container).syncInventory(player);
	        			} else {
	        				NonNullList<ItemStack> nonnulllist1 = NonNullList.<ItemStack>create();
		                    for (int j = 0; j < container.inventorySlots.size(); ++j) {
		                        ItemStack itemstack = ((Slot)container.inventorySlots.get(j)).getStack();
		                        ItemStack itemstack1 = itemstack.isEmpty() ? ItemStack.EMPTY : itemstack;
		                        nonnulllist1.add(itemstack1);
		                    }
	        				player.sendAllContents(container, nonnulllist1);
	        			}
	                }
	            }
			}
		}
		
	}

}

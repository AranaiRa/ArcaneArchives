package com.aranaira.arcanearchives.packets;

import com.aranaira.arcanearchives.util.LargeItemNBTUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class PacketsRadiantChest
{
	public static class WindowItemsPacket implements IMessage
	{
		int windowId;
		List<ItemStack> itemStacks;

		public WindowItemsPacket()
		{
		}

		public WindowItemsPacket(int windowId, NonNullList<ItemStack> items)
		{
			this.windowId = windowId;
			this.itemStacks = items.stream().map(ItemStack::copy).collect(Collectors.toList());
		}

		@Override
		public void fromBytes(ByteBuf buf)
		{
			this.windowId = buf.readUnsignedByte();
			int i = buf.readShort();
			this.itemStacks = NonNullList.withSize(i, ItemStack.EMPTY);

			for(int j = 0; j < i; ++j)
			{
				ItemStack newStack;
				try
				{
					newStack = LargeItemNBTUtil.readFromBuf(buf);
				} catch(IOException e)
				{
					e.printStackTrace();
					throw new RuntimeException(String.format("Unable to read ItemStack from container WindowItemsPacket, w:%d, slot:%d", windowId, j));
				}
				this.itemStacks.set(j, newStack);
			}
		}

		@Override
		public void toBytes(ByteBuf buf)
		{
			buf.writeByte(this.windowId);
			buf.writeShort(this.itemStacks.size());

			for(ItemStack stack : this.itemStacks)
			{
				LargeItemNBTUtil.writeToBuf(buf, stack);
			}
		}

		public static class WindowItemsHandler implements IMessageHandler<PacketsRadiantChest.WindowItemsPacket, IMessage>
		{
			@Override
			public IMessage onMessage(WindowItemsPacket message, MessageContext ctx)
			{
				FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));

				return null;
			}

			// Fired on the server
			@SideOnly(Side.CLIENT)
			private void processMessage(WindowItemsPacket message, MessageContext context)
			{
				EntityPlayer player = Minecraft.getMinecraft().player;
				if(message.windowId == player.openContainer.windowId)
				{
					player.openContainer.setAll(message.itemStacks);
				}
			}
		}
	}

	public static class SetSlotPacket implements IMessage
	{
		int windowId;
		int slot;
		ItemStack stack = ItemStack.EMPTY;

		public SetSlotPacket()
		{
		}

		public SetSlotPacket(int windowId, int slot, ItemStack item)
		{
			this.windowId = windowId;
			this.slot = slot;
			this.stack = item.copy();
		}

		@Override
		public void fromBytes(ByteBuf buf)
		{
			this.windowId = buf.readUnsignedByte();
			this.slot = buf.readShort();
			try
			{
				this.stack = LargeItemNBTUtil.readFromBuf(buf);
			} catch(IOException e)
			{
				e.printStackTrace();
				throw new RuntimeException(String.format("Unable to read ItemStack from container SetSlotPacket, w:%d, slot:%d", windowId, slot));
			}
		}

		@Override
		public void toBytes(ByteBuf buf)
		{
			buf.writeByte(this.windowId);
			buf.writeShort(this.slot);
			LargeItemNBTUtil.writeToBuf(buf, this.stack);
		}

		public static class SetSlotPacketHandler implements IMessageHandler<PacketsRadiantChest.SetSlotPacket, IMessage>
		{
			@Override
			public IMessage onMessage(SetSlotPacket message, MessageContext ctx)
			{
				FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));

				return null;
			}

			// Fired on the client
			@SideOnly(Side.CLIENT)
			private void processMessage(SetSlotPacket message, MessageContext context)
			{
				Minecraft mc = Minecraft.getMinecraft();
				EntityPlayer player = mc.player;
				ItemStack itemstack = message.stack;
				int i = message.slot;
				int w = message.windowId;
				GuiScreen currentScreen = mc.currentScreen;

				if(w == -1)
				{
					player.inventory.setItemStack(itemstack);
				} else if(w == -2)
				{
					player.inventory.setInventorySlotContents(i, itemstack);
				} else
				{
					boolean flag = false;

					if(currentScreen instanceof GuiContainerCreative)
					{
						GuiContainerCreative guicontainercreative = (GuiContainerCreative) currentScreen;
						flag = guicontainercreative.getSelectedTabIndex() != CreativeTabs.INVENTORY.getIndex();
					}

					if(w == 0 && i >= 36 && i < 45)
					{
						if(!itemstack.isEmpty())
						{
							ItemStack itemstack1 = player.inventoryContainer.getSlot(i).getStack();

							if(itemstack1.isEmpty() || itemstack1.getCount() < itemstack.getCount())
							{
								itemstack.setAnimationsToGo(5);
							}
						}

						player.inventoryContainer.putStackInSlot(i, itemstack);
					} else if(w == player.openContainer.windowId && (w != 0 || !flag))
					{
						player.openContainer.putStackInSlot(i, itemstack);
					}
				}
			}
		}
	}
}

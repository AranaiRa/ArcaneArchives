package com.aranaira.arcanearchives.network;

import com.aranaira.arcanearchives.data.NetworkHelper;
import com.aranaira.arcanearchives.data.ServerNetwork;
import com.aranaira.arcanearchives.inventory.ContainerRadiantChest;
import com.aranaira.arcanearchives.network.NetworkHandler.EmptyMessageServer;
import com.aranaira.arcanearchives.network.NetworkHandler.ServerHandler;
import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantChestTileEntity;
import com.aranaira.arcanearchives.util.NetworkUtils;
import com.aranaira.arcanearchives.util.WorldUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.play.server.SPacketConfirmTransaction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.UUID;

public class PacketRadiantChest {
	public static class SetName implements IMessage {

		private BlockPos pos;
		private String name;
		private int dimension;

		@SuppressWarnings("unused")
		public SetName () {
		}

		public SetName (BlockPos pos, String name, int dimensionID) {
			this.pos = pos;
			this.name = (name == null) ? "" : name;
			dimension = dimensionID;
		}

		@Override
		public void fromBytes (ByteBuf buf) {
			name = ByteBufUtils.readUTF8String(buf);
			pos = BlockPos.fromLong(buf.readLong());
			dimension = buf.readInt();
		}

		@Override
		public void toBytes (ByteBuf buf) {
			ByteBufUtils.writeUTF8String(buf, name);
			buf.writeLong(pos.toLong());
			buf.writeInt(dimension);
		}

		public static class Handler implements ServerHandler<SetName> {
			@Override
			public void processMessage (SetName message, MessageContext ctx) {
				RadiantChestTileEntity te = WorldUtil.getTileEntity(RadiantChestTileEntity.class, message.dimension, message.pos);
				if (te != null) {
					te.setChestName(message.name);
					te.markDirty();
					te.defaultServerSideUpdate();
				}
			}
		}
	}

	public static class ToggleBrazier implements IMessage {
		private UUID networkId = NetworkHelper.INVALID;
		private UUID tileId = NetworkHelper.INVALID;

		public ToggleBrazier () {
		}

		public ToggleBrazier (UUID networkId, UUID tileId) {
			this.networkId = networkId;
			this.tileId = tileId;
		}

		@Override
		public void fromBytes (ByteBuf buf) {
			long most = buf.readLong();
			long least = buf.readLong();
			this.networkId = new UUID(most, least);
			most = buf.readLong();
			least = buf.readLong();
			this.tileId = new UUID(most, least);
		}

		@Override
		public void toBytes (ByteBuf buf) {
			if (networkId == null) networkId = NetworkHelper.INVALID;
			if (tileId == null) tileId = NetworkHelper.INVALID;

			long most = networkId.getMostSignificantBits();
			long least = networkId.getLeastSignificantBits();
			buf.writeLong(most);
			buf.writeLong(least);
			most = tileId.getMostSignificantBits();
			least = tileId.getLeastSignificantBits();
			buf.writeLong(most);
			buf.writeLong(least);
		}

		public static class Handler implements ServerHandler<ToggleBrazier> {
			@Override
			public void processMessage (ToggleBrazier message, MessageContext ctx) {
				if (message.networkId.equals(NetworkHelper.INVALID) || message.tileId.equals(NetworkHelper.INVALID)) return;

				ServerNetwork network = NetworkHelper.getServerNetwork(message.networkId, ctx.getServerHandler().player.world);
				if (network != null) {
					ImmanenceTileEntity tile = network.getImmanenceTile(message.tileId);
					if (tile instanceof RadiantChestTileEntity) {
						((RadiantChestTileEntity) tile).toggleRoutingType();
						tile.defaultServerSideUpdate();
					}
				}
			}
		}
	}

	public static class MessageClickWindowExtended implements IMessage {

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
					@Override
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
						if (container instanceof ContainerRadiantChest) {
							((ContainerRadiantChest) container).syncInventory(player);
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
							if (container instanceof ContainerRadiantChest) {
								((ContainerRadiantChest) container).syncInventory(player);
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

	public static class MessageSyncExtendedSlotContents implements IMessage {

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
					@Override
					public void run() {
						processMessage(message, player);
					}
				});


				return null;
			}

			public void processMessage(final MessageSyncExtendedSlotContents message, EntityPlayer player) {
				if (player.openContainer instanceof ContainerRadiantChest && message.windowId == player.openContainer.windowId) {
					player.openContainer.inventorySlots.get(message.slot).putStack(message.stack);
				}
			}

		}

	}
}

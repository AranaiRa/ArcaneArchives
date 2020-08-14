package com.aranaira.arcanearchives.network;

import com.aranaira.arcanearchives.containers.RadiantChestContainer;
import com.aranaira.arcanearchives.util.ByteUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketConfirmTransaction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

public class PacketRadiantChest {
/*    public SetName(BlockPos pos, String name, int dimensionID) {
      super(pos, dimensionID);
      this.name = (name == null) ? "" : name;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
      super.fromBytes(buf);
      name = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
      super.toBytes(buf);
      ByteBufUtils.writeUTF8String(buf, name);
    }

    public static class Handler implements TileHandlerServer<SetName, RadiantChestTileEntity> {
      @Override
      public void processMessage(SetName message, MessageContext ctx, RadiantChestTileEntity tile) {
        if (message.name.isEmpty()) {
          ArcaneArchives.logger.debug("Incoming packet for tile entity in " + tile.dimension + " at " + tile.getPos().toString() + " had an empty name.");
        }
        tile.setChestName(message.name);
        SyncChestName packet = new SyncChestName(tile.getPos(), tile.dimension, tile.getChestName());
        Networking.sendToAllTracking(packet, tile);
      }
    }
  }

  public static class UnsetName extends TileMessage {
    @SuppressWarnings("unused")
    public UnsetName() {
    }

    public UnsetName(BlockPos pos, int dimensionID) {
      super(pos, dimensionID);
    }

    public static class Handler implements TileHandlerServer<UnsetName, RadiantChestTileEntity> {
      @Override
      public void processMessage(UnsetName message, MessageContext ctx, RadiantChestTileEntity tile) {
        tile.unsetChestName();
        SyncChestName packet = new SyncChestName(tile.getPos(), tile.dimension, tile.getChestName());
        Networking.sendToAllTracking(packet, tile);
      }
    }
  }*/

/*  public static class SetItemAndFacing extends TileMessage {
    private ItemStack stack;
    private EnumFacing facing;

    @SuppressWarnings("unused")
    public SetItemAndFacing() {
    }

    public SetItemAndFacing(BlockPos pos, int dimension, ItemStack stack, EnumFacing facing) {
      super(pos, dimension);
      this.stack = stack;
      this.facing = facing;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
      super.fromBytes(buf);
      this.stack = ByteBufUtils.readItemStack(buf);
      this.facing = EnumFacing.byIndex(buf.readInt());
    }

    @Override
    public void toBytes(ByteBuf buf) {
      super.toBytes(buf);
      ByteBufUtils.writeItemStack(buf, this.stack);
      buf.writeInt(this.facing.ordinal());
    }

    public static class Handler implements TileHandlerServer<SetItemAndFacing, RadiantChestTileEntity> {
      @Override
      public void processMessage(SetItemAndFacing message, MessageContext ctx, RadiantChestTileEntity tile) {
        if (message.stack.isEmpty()) {
          ArcaneArchives.logger.debug("Incoming packet for tile entity in " + tile.dimension + " at " + tile.getPos().toString() + " had an empty itemstack for display.");
        }
        tile.setDisplay(message.stack, message.facing);
        SyncChestDisplay packet = new SyncChestDisplay(tile.getPos(), tile.dimension, tile.getDisplayStack(), tile.getDisplayFacing());
        Networking.sendToAllTracking(packet, tile);
      }
    }
  }*/

/*  public static class UnsetItem extends TileMessage {

    @SuppressWarnings("unused")
    public UnsetItem() {
    }

    public UnsetItem(BlockPos pos, int dimension) {
      super(pos, dimension);
    }

    public static class Handler implements TileHandlerServer<UnsetItem, RadiantChestTileEntity> {
      @Override
      public void processMessage(UnsetItem message, MessageContext ctx, RadiantChestTileEntity tile) {
        tile.unsetDisplayStack();
        SyncChestDisplay packet = new SyncChestDisplay(tile.getPos(), tile.dimension, tile.getDisplayStack(), tile.getDisplayFacing());
        Networking.sendToAllTracking(packet, tile);
      }
    }
  }*/

/*  public static class SyncChestName extends TileMessage {
    private String chestName;

    public SyncChestName() {
    }

    public SyncChestName(BlockPos pos, int dimension, String chestName) {
      super(pos, dimension);
      this.chestName = chestName;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
      super.fromBytes(buf);
      this.chestName = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
      super.toBytes(buf);
      ByteBufUtils.writeUTF8String(buf, this.chestName);
    }

    public static class Handler implements TileHandlerClient<SyncChestName, RadiantChestTileEntity> {
      @Override
      @SideOnly(Side.CLIENT)
      public void processMessage(SyncChestName message, MessageContext ctx, RadiantChestTileEntity tile) {
        if (message.chestName.isEmpty()) {
          ArcaneArchives.logger.debug("Incoming packet for tile entity in " + tile.dimension + " at " + tile.getPos().toString() + " had an empty chest name.");
          tile.unsetChestName();
        } else {
          tile.setChestName(message.chestName);
        }
      }
    }
  }*/

  /*  public static class SyncChestDisplay extends TileMessage {
      private ItemStack stack;
      private EnumFacing facing;

      public SyncChestDisplay() {
      }

      public SyncChestDisplay(BlockPos pos, int dimension, ItemStack stack, EnumFacing facing) {
        super(pos, dimension);
        this.stack = stack;
        this.facing = facing;
      }

      @Override
      public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        this.stack = ByteBufUtils.readItemStack(buf);
        this.facing = EnumFacing.byIndex(buf.readInt());
      }

      @Override
      public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        ByteBufUtils.writeItemStack(buf, this.stack);
        buf.writeInt(this.facing.ordinal());
      }

      public static class Handler implements TileHandlerClient<SyncChestDisplay, RadiantChestTileEntity> {
        @Override
        @SideOnly(Side.CLIENT)
        public void processMessage(SyncChestDisplay message, MessageContext ctx, RadiantChestTileEntity tile) {
          if (message.stack.isEmpty()) {
            ArcaneArchives.logger.debug("Incoming packet for tile entity in " + tile.dimension + " at " + tile.getPos().toString() + " had an empty itemstack for display.");
          }
          tile.setDisplay(message.stack, message.facing);
        }
      }
    }*/
  /*
  public static class ToggleBrazier implements IMessage {
    private UUID networkId = DataHelper.INVALID;
    private UUID tileId = DataHelper.INVALID;

    public ToggleBrazier() {
    }

    public ToggleBrazier(UUID networkId, UUID tileId) {
      this.networkId = networkId;
      this.tileId = tileId;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
      long most = buf.readLong();
      long least = buf.readLong();
      this.networkId = new UUID(most, least);
      most = buf.readLong();
      least = buf.readLong();
      this.tileId = new UUID(most, least);
    }

    @Override
    public void toBytes(ByteBuf buf) {
      if (networkId == null) {
        networkId = DataHelper.INVALID;
      }
      if (tileId == null) {
        tileId = DataHelper.INVALID;
      }

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
      public void processMessage(ToggleBrazier message, MessageContext ctx) {
        if (message.networkId.equals(DataHelper.INVALID) || message.tileId.equals(DataHelper.INVALID)) {
          return;
        }

        ServerNetwork network = DataHelper.getServerNetwork(message.networkId);
        if (network != null) {
          ImmanenceTileEntity tile = network.getImmanenceTile(message.tileId);
          if (tile instanceof RadiantChestTileEntity) {
            ((RadiantChestTileEntity) tile).toggleRoutingType();
            tile.markDirty();
            tile.defaultServerSideUpdate();
          }
        }
      }
    }
  }
*/

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
      this.clickedItem = ByteUtils.readExtendedItemStack(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
      buf.writeByte(this.windowId);
      buf.writeInt(this.slot);
      buf.writeByte(this.mouseButton);
      buf.writeShort(this.transactionId);
      buf.writeInt(this.mode.ordinal());
      ByteUtils.writeExtendedItemStackFromClientToServer(buf, this.clickedItem);
    }

    public static class Handler implements IMessageHandler<MessageClickWindowExtended, IMessage> {
      @Override
      public IMessage onMessage(final MessageClickWindowExtended message, final MessageContext ctx) {
        EntityPlayerMP player = ctx.getServerHandler().player;

        if (player == null) {
          return null;
        }

        player.getServerWorld().addScheduledTask(() -> processMessage(message, player));

        return null;
      }

      public void processMessage(final MessageClickWindowExtended message, EntityPlayerMP player) {
        player.markPlayerActive();

        Container container = player.openContainer;

        if (container.windowId == message.windowId && container.getCanCraft(player)) {
          if (player.isSpectator()) {
            if (container instanceof RadiantChestContainer) {
              ((RadiantChestContainer) container).syncInventory(player);
            } else {
              NonNullList<ItemStack> nonnulllist = NonNullList.create();
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
              if (container instanceof RadiantChestContainer) {
                ((RadiantChestContainer) container).syncInventory(player);
              } else {
                NonNullList<ItemStack> nonnulllist1 = NonNullList.create();
                for (int j = 0; j < container.inventorySlots.size(); ++j) {
                  ItemStack itemstack = container.inventorySlots.get(j).getStack();
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
      this.stack = ByteUtils.readExtendedItemStack(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
      buf.writeByte(this.windowId);
      buf.writeInt(this.slot);
      ByteUtils.writeExtendedItemStack(buf, stack);
    }

    public static class Handler implements IMessageHandler<MessageSyncExtendedSlotContents, IMessage> {
      @SideOnly(Side.CLIENT)
      @Override
      public IMessage onMessage(final MessageSyncExtendedSlotContents message, final MessageContext ctx) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;

        if (player == null) {
          return null;
        }

        mc.addScheduledTask(() -> processMessage(message, player));
        return null;
      }

      public void processMessage(final MessageSyncExtendedSlotContents message, EntityPlayer player) {
        if (player.openContainer instanceof RadiantChestContainer && message.windowId == player.openContainer.windowId) {
          player.openContainer.inventorySlots.get(message.slot).putStack(message.stack);
        }
      }
    }
  }
}

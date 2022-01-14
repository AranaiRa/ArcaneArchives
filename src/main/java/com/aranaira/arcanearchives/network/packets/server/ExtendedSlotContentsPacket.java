package com.aranaira.arcanearchives.network.packets.server;

import com.aranaira.arcanearchives.api.network.IPacket;
import com.aranaira.arcanearchives.inventory.container.AbstractLargeContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import noobanidus.libs.noobutil.getter.Getter;
import noobanidus.libs.noobutil.network.ExtendedItemStackPacketBuffer;

public class ExtendedSlotContentsPacket implements IPacket {
  private int windowId = 0;
  private int slot = 0;
  private ItemStack stack = ItemStack.EMPTY;

  public ExtendedSlotContentsPacket() {
  }

  public ExtendedSlotContentsPacket(int windowId, int slot, ItemStack stack) {
    this.windowId = windowId;
    this.slot = slot;
    this.stack = stack.copy();
  }

  public ExtendedSlotContentsPacket(PacketBuffer buf) {
    ExtendedItemStackPacketBuffer ebuf = new ExtendedItemStackPacketBuffer(buf);
    this.windowId = ebuf.readByte();
    this.slot = ebuf.readInt();
    this.stack = ebuf.readExtendedItemStack();
  }

  public void encode(PacketBuffer buf) {
    ExtendedItemStackPacketBuffer ebuf = new ExtendedItemStackPacketBuffer(buf);
    ebuf.writeByte(this.windowId);
    ebuf.writeInt(this.slot);
    ebuf.writeExtendedItemStack(stack);
  }

  public void handle(NetworkEvent.Context ctx) {
    PlayerEntity player = Getter.getPlayer();
    if (player == null) return;

    if (player.containerMenu instanceof AbstractLargeContainer && windowId == player.containerMenu.containerId) {
      player.containerMenu.slots.get(slot).set(stack);
    }
  }
}

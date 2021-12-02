package com.aranaira.arcanearchives.core.network.packets;

import com.aranaira.arcanearchives.core.inventory.container.AbstractLargeContainer;
import com.aranaira.arcanearchives.core.inventory.container.RadiantChestContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import noobanidus.libs.noobutil.getter.Getter;
import noobanidus.libs.noobutil.types.ExtendedItemStackPacketBuffer;

import java.util.function.Supplier;

public class ExtendedSlotContentsPacket {
  private int windowId = 0;
  private int slot = 0;
  private ItemStack stack = ItemStack.EMPTY;

  public ExtendedSlotContentsPacket() {}

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

    public void handle(Supplier<NetworkEvent.Context> ctx) {
      PlayerEntity player = Getter.getPlayer();
      if (player == null) return;

      ctx.get().enqueueWork(() -> {
        if (player.containerMenu instanceof AbstractLargeContainer && windowId == player.containerMenu.containerId) {
          player.containerMenu.slots.get(slot).set(stack);
        }
      });


      ctx.get().setPacketHandled(true);
    }
}

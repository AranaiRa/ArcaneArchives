package com.aranaira.arcanearchives.network;

import com.aranaira.arcanearchives.api.inventory.container.RadiantChestContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;
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
      PlayerEntity player = DistExecutor.callWhenOn(Dist.CLIENT, () -> () -> Minecraft.getInstance().player);
      if (player == null) return;

      ctx.get().enqueueWork(() -> {
        if (player.openContainer instanceof RadiantChestContainer && windowId == player.openContainer.windowId) {
          player.openContainer.inventorySlots.get(slot).putStack(stack);
        }
      });


      ctx.get().setPacketHandled(true);
    }
}

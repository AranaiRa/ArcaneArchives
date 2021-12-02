package com.aranaira.arcanearchives.core.network.packets;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class RequestSyncPacket {
  private int containerId;

  public RequestSyncPacket() {
  }

  public RequestSyncPacket(int containerId) {
    this.containerId = containerId;
  }

  public RequestSyncPacket(PacketBuffer buf) {
    this.containerId = buf.readInt();
  }

  public void encode(PacketBuffer buf) {
    buf.writeInt(this.containerId);
  }

  public static void handle(RequestSyncPacket packet, Supplier<NetworkEvent.Context> context) {
    NetworkEvent.Context ctx = context.get();
    ctx.enqueueWork(() -> handle(ctx.getSender(), packet.containerId));
    ctx.setPacketHandled(true);
  }

  public static void handle(ServerPlayerEntity player, int containerId) {
    Container menu = player.containerMenu;
    if (menu.containerId == containerId) {
      menu.broadcastChanges();
    }
  }
}

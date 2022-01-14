package com.aranaira.arcanearchives.network.packets.client;

import com.aranaira.arcanearchives.api.network.IPacket;
import com.aranaira.arcanearchives.inventory.container.CrystalWorkbenchContainer;
import com.aranaira.arcanearchives.network.Networking;
import com.aranaira.arcanearchives.network.packets.server.RecipeSyncPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class RequestSyncPacket implements IPacket {
  private final int containerId;

  public RequestSyncPacket(int containerId) {
    this.containerId = containerId;
  }

  public RequestSyncPacket(PacketBuffer buffer) {
    this.containerId = buffer.readInt();
  }

  public void encode(PacketBuffer buffer) {
    buffer.writeInt(this.containerId);
  }

  public void handle(NetworkEvent.Context ctx) {
    ServerPlayerEntity player = ctx.getSender();
    if (player != null && player.containerMenu.containerId == this.containerId && player.containerMenu instanceof CrystalWorkbenchContainer) {
      Networking.sendTo(new RecipeSyncPacket(((CrystalWorkbenchContainer) player.containerMenu).getRecipe(), this.containerId), player);
      player.containerMenu.broadcastChanges();
    }
  }
}

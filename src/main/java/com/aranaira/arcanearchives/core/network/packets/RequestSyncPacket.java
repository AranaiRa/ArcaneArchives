package com.aranaira.arcanearchives.core.network.packets;

import com.aranaira.arcanearchives.core.inventory.container.CrystalWorkbenchContainer;
import com.aranaira.arcanearchives.core.network.Networking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import noobanidus.libs.noobutil.getter.Getter;

import java.util.function.Supplier;

public class RequestSyncPacket {
  private final int containerId;

  public RequestSyncPacket(int containerId) {
    this.containerId = containerId;
  }

  public RequestSyncPacket(PacketBuffer buffer) {
    this.containerId = buffer.readInt();
  }

  public void encode (PacketBuffer buffer){
    buffer.writeInt(this.containerId);
  }

  public void handle (Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      ServerPlayerEntity player = ctx.get().getSender();
      if (player != null && player.containerMenu.containerId == this.containerId && player.containerMenu instanceof CrystalWorkbenchContainer) {
        Networking.sendTo(new RecipeSyncPacket(((CrystalWorkbenchContainer)player.containerMenu).getRecipe(), this.containerId), player);
        player.containerMenu.broadcastChanges();
      }
    });
    ctx.get().setPacketHandled(true);
  }
}
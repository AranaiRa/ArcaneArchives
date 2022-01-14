package com.aranaira.arcanearchives.network.packets.client;

import com.aranaira.arcanearchives.api.data.DataStorage;
import com.aranaira.arcanearchives.api.data.UUIDNameData;
import com.aranaira.arcanearchives.api.network.IPacket;
import com.aranaira.arcanearchives.network.Networking;
import com.aranaira.arcanearchives.network.packets.server.NetworkNamesSyncPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Map;
import java.util.UUID;

public class RequestNetworkNamesPacket implements IPacket {
  public RequestNetworkNamesPacket() {
  }

  public RequestNetworkNamesPacket(PacketBuffer buf) {
  }

  @Override
  public void handle(NetworkEvent.Context ctx) {
    ServerPlayerEntity player = ctx.getSender();
    if (player != null) {
      Map<UUID, UUIDNameData.Name> data = DataStorage.getNetworkNames();
      if (!data.isEmpty()) {
        NetworkNamesSyncPacket packet = new NetworkNamesSyncPacket(data);
        Networking.sendTo(packet, player);
      }
    }
  }

  @Override
  public void encode(PacketBuffer buffer) {
  }
}

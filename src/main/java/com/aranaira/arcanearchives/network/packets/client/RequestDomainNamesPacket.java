package com.aranaira.arcanearchives.network.packets.client;

import com.aranaira.arcanearchives.api.data.DataStorage;
import com.aranaira.arcanearchives.api.data.UUIDNameData;
import com.aranaira.arcanearchives.api.network.IPacket;
import com.aranaira.arcanearchives.network.Networking;
import com.aranaira.arcanearchives.network.packets.server.DomainNamesSyncPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Map;
import java.util.UUID;

public class RequestDomainNamesPacket implements IPacket {
  private final UUID domainId;

  public RequestDomainNamesPacket(UUID domainId) {
    this.domainId = domainId;
  }

  public RequestDomainNamesPacket(PacketBuffer buf) {
    this.domainId = buf.readUUID();
  }

  @Override
  public void handle(NetworkEvent.Context ctx) {
    ServerPlayerEntity player = ctx.getSender();
    if (player != null) {
      Map<UUID, UUIDNameData.Name> data = DataStorage.getDomainNames(this.domainId);
      if (!data.isEmpty()) {
        DomainNamesSyncPacket packet = new DomainNamesSyncPacket(data);
        Networking.sendTo(packet, player);
      }
    }
  }

  @Override
  public void encode(PacketBuffer buffer) {
    buffer.writeUUID(this.domainId);
  }
}

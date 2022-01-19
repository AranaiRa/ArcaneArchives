package com.aranaira.arcanearchives.network.packets.client;

import com.aranaira.arcanearchives.api.domain.DomainManager;
import com.aranaira.arcanearchives.api.domain.impl.Domain;
import com.aranaira.arcanearchives.api.network.IPacket;
import com.aranaira.arcanearchives.network.Networking;
import com.aranaira.arcanearchives.network.packets.server.ManifestSyncPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;

public class RequestManifestSyncPacket implements IPacket {
  private final long lastUpdate;
  private final UUID domainId;

  public RequestManifestSyncPacket(long lastUpdate, UUID domainId) {
    this.lastUpdate = lastUpdate;
    this.domainId = domainId;
  }

  public RequestManifestSyncPacket(PacketBuffer buffer) {
    this.lastUpdate = buffer.readVarLong();
    this.domainId = buffer.readUUID();
  }

  @Override
  public void handle(NetworkEvent.Context context) {
    Domain domain = DomainManager.getDomain(domainId);
    if (lastUpdate == -1 || lastUpdate != domain.getLastUpdated()) {
      ServerPlayerEntity player = context.getSender();
      ManifestSyncPacket packet = new ManifestSyncPacket(domain);
      Networking.sendTo(packet, player);
    }
  }

  @Override
  public void encode(PacketBuffer buffer) {
    buffer.writeVarLong(lastUpdate);
    buffer.writeUUID(domainId);
  }
}

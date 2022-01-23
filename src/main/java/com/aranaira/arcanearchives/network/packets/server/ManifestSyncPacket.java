package com.aranaira.arcanearchives.network.packets.server;

import com.aranaira.arcanearchives.api.domain.impl.DomainImpl;
import com.aranaira.arcanearchives.api.network.IPacket;
import com.aranaira.arcanearchives.client.ManifestStorage;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import noobanidus.libs.noobutil.tracking.ItemTracking;

import java.util.UUID;

public class ManifestSyncPacket implements IPacket {
  private final UUID domainId;
  private final ItemTracking tracking;
  private final long lastUpdated;

  public ManifestSyncPacket(DomainImpl domain) {
    this(domain.getDomainId(), domain.getTracking(), domain.getLastUpdated());
  }

  public ManifestSyncPacket(UUID domainId, ItemTracking tracking, long lastUpdated) {
    this.domainId = domainId;
    this.lastUpdated = lastUpdated;
    this.tracking = tracking;
  }

  public ManifestSyncPacket(PacketBuffer buffer) {
    this.domainId = buffer.readUUID();
    this.lastUpdated = buffer.readVarLong();
    this.tracking = ItemTracking.deserialize(buffer);
  }

  @Override
  public void handle(NetworkEvent.Context context) {
    ManifestStorage.updateTracking(this.domainId, this.tracking, this.lastUpdated);
  }

  @Override
  public void encode(PacketBuffer buffer) {
    buffer.writeUUID(this.domainId);
    buffer.writeVarLong(this.lastUpdated);
    tracking.serialize(buffer);
  }
}

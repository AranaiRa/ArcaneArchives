package com.aranaira.arcanearchives.network.packets.client;

import com.aranaira.arcanearchives.api.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;

public class RequestManifestUpdate implements IPacket {
  private long lastUpdate;
  private UUID networkId;

  public RequestManifestUpdate(long lastUpdate, UUID networkId) {
    this.lastUpdate = lastUpdate;
    this.networkId = networkId;
  }

  public RequestManifestUpdate(PacketBuffer buffer) {
    this.lastUpdate = buffer.readVarLong();
    this.networkId = buffer.readUUID();
  }

  @Override
  public void handle(NetworkEvent.Context context) {

  }

  @Override
  public void encode(PacketBuffer buffer) {
    buffer.writeVarLong(lastUpdate);
    buffer.writeUUID(networkId);
  }
}

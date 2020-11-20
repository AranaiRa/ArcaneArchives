/*package com.aranaira.arcanearchives.data.client;

import com.aranaira.arcanearchives.data.ServerNetworkData;
import com.aranaira.arcanearchives.types.ISerializePacketBuffer;
import net.minecraft.network.PacketBuffer;

import java.util.UUID;

public class ClientNetworkData implements ISerializePacketBuffer<ClientNetworkData> {
  private UUID networkId;

  public ClientNetworkData() {
  }

  public static ClientNetworkData fromServer (ServerNetworkData incoming) {
    ClientNetworkData data = new ClientNetworkData();
    data.setNetworkId(incoming.getNetworkId());
    return data;
  }

  public UUID getNetworkId() {
    return networkId;
  }

  public void setNetworkId(UUID networkId) {
    this.networkId = networkId;
  }

  public static ClientNetworkData fromPacket(PacketBuffer buf) {
    ClientNetworkData data = new ClientNetworkData();
    data.networkId = buf.readUniqueId();
    return data;
  }

  @Override
  public void toPacket(PacketBuffer buf) {
    buf.writeUniqueId(networkId);
  }
}*/

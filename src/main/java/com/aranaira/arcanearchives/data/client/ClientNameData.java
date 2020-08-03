package com.aranaira.arcanearchives.data.client;

import com.aranaira.arcanearchives.data.NameData;
import com.aranaira.arcanearchives.data.NetworkName;
import com.aranaira.arcanearchives.types.ISerializePacketBuffer;
import net.minecraft.network.PacketBuffer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClientNameData implements ISerializePacketBuffer<ClientNameData> {
  private Map<UUID, NetworkName> map = new HashMap<>();

  public ClientNameData() {
  }

  public static ClientNameData fromServer (NameData incoming) {
    ClientNameData data = new ClientNameData();
    data.map = incoming.getData();
    return data;
  }

  @Override
  public ClientNameData fromPacket(PacketBuffer buf) {
    int total = buf.readInt();
    map.clear();
    for (int i = 0; i < total; i++) {
      UUID uuid = buf.readUniqueId();
      map.put(uuid, NetworkName.fromByeBuf(buf));
    }
    return this;
  }

  @Override
  public void toPacket(PacketBuffer buf) {
    buf.writeInt(map.size());
    for (Map.Entry<UUID, NetworkName> entry : map.entrySet()) {
      buf.writeUniqueId(entry.getKey());
      entry.getValue().toBytes(buf);
    }
  }
}

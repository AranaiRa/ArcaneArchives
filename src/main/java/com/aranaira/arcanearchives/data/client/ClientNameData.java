/*package com.aranaira.arcanearchives.data.client;

import com.aranaira.arcanearchives.data.NameData;
import com.aranaira.arcanearchives.tilenetwork.NetworkName;
import com.aranaira.arcanearchives.types.ISerializePacketBuffer;
import net.minecraft.network.PacketBuffer;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClientNameData implements ISerializePacketBuffer<ClientNameData> {
  private Map<UUID, NetworkName> map = new HashMap<>();

  public ClientNameData() {
  }

  @Nullable
  public NetworkName getNameFor (UUID uuid) {
    return map.get(uuid);
  }

  public static ClientNameData fromServer (NameData incoming) {
    ClientNameData data = new ClientNameData();
    data.map = incoming.getData();
    return data;
  }

  public static ClientNameData fromPacket(PacketBuffer buf) {
    ClientNameData data = new ClientNameData();
    int total = buf.readInt();
    data.map.clear();
    for (int i = 0; i < total; i++) {
      UUID uuid = buf.readUniqueId();
      data.map.put(uuid, NetworkName.fromBytes(buf));
    }
    return data;
  }

  @Override
  public void toPacket(PacketBuffer buf) {
    buf.writeInt(map.size());
    for (Map.Entry<UUID, NetworkName> entry : map.entrySet()) {
      buf.writeUniqueId(entry.getKey());
      entry.getValue().toBytes(buf);
    }
  }
}*/

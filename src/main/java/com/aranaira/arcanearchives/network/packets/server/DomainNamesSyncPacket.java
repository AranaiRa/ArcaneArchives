package com.aranaira.arcanearchives.network.packets.server;

import com.aranaira.arcanearchives.api.data.UUIDNameData;
import com.aranaira.arcanearchives.api.network.IPacket;
import com.aranaira.arcanearchives.client.NameStorage;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DomainNamesSyncPacket implements IPacket {
  private final Map<UUID, UUIDNameData.Name> nameData;

  public DomainNamesSyncPacket(Map<UUID, UUIDNameData.Name> nameData) {
    this.nameData = nameData;
  }

  public DomainNamesSyncPacket(PacketBuffer buf) {
    int count = buf.readVarInt();
    nameData = new HashMap<>();
    for (int i = 0; i < count; i++) {
      UUIDNameData.Name name = UUIDNameData.Name.fromBuffer(buf);
      nameData.put(name.getUuid(), name);
    }
  }

  @Override
  public void handle(NetworkEvent.Context context) {
    NameStorage.updateMap(nameData);
  }

  @Override
  public void encode(PacketBuffer buffer) {
    buffer.writeVarInt(nameData.size());
    for (UUIDNameData.Name name : nameData.values()) {
      name.serialize(buffer);
    }
  }
}

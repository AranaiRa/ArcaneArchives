package com.aranaira.arcanearchives.client;

import com.aranaira.arcanearchives.api.data.UUIDNameData;
import com.aranaira.arcanearchives.network.Networking;
import com.aranaira.arcanearchives.network.packets.client.RequestNetworkNamesPacket;
import net.minecraft.client.Minecraft;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NameStorage {
  private static final Map<UUID, UUIDNameData.Name> data = new HashMap<>();
  private static long lastUpdated = -1;

  public static void updateMap (Map<UUID, UUIDNameData.Name> data) {
    NameStorage.data.clear();
    NameStorage.data.putAll(data);
    if (Minecraft.getInstance().level != null) {
      lastUpdated = Minecraft.getInstance().level.getGameTime();
    }
  }

  @Nullable
  public static UUIDNameData.Name getName (UUID uuid) {
    return data.get(uuid);
  }

  public static void update (UUID wantedId) {
    Minecraft mc = Minecraft.getInstance();
    if (mc.level == null) {
      return;
    }
    long time = mc.level.getGameTime();
    if ((time - lastUpdated) < 35) {
      return;
    }
    Networking.sendToServer(new RequestNetworkNamesPacket(wantedId));
  }
}

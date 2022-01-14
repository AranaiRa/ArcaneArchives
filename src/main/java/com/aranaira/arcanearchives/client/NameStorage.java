package com.aranaira.arcanearchives.client;

import com.aranaira.arcanearchives.api.data.UUIDNameData;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NameStorage {
  private static final Map<UUID, UUIDNameData.Name> data = new HashMap<>();

  public static void updateMap (Map<UUID, UUIDNameData.Name> data) {
    NameStorage.data.clear();
    NameStorage.data.putAll(data);
  }

  @Nullable
  public static UUIDNameData.Name getName (UUID uuid) {
    return data.get(uuid);
  }
}

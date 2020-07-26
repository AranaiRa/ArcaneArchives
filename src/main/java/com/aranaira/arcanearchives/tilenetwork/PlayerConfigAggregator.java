package com.aranaira.arcanearchives.tilenetwork;

import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerConfigAggregator {
  public static final Map<UUID, PlayerNetworkConfig> storage = new HashMap<>();

  public static PlayerNetworkConfig byId (UUID uuid) {
    PlayerNetworkConfig result = storage.get(uuid);
    if (result == null) {
      updatePlayer(uuid, PlayerNetworkConfig.defaultConfig(uuid));
      result = storage.get(uuid);
    }

    return result;
  }

  public static PlayerNetworkConfig byPlayer (EntityPlayer player) {
    return byId(player.getUniqueID());
  }

  public static void updatePlayer (UUID player, PlayerNetworkConfig config) {
    storage.put(player, config);
  }

  public static void updatePlayer (EntityPlayer player, PlayerNetworkConfig config) {
    updatePlayer(player.getUniqueID(), config);
  }
}

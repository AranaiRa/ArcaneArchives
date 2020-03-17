package com.aranaira.arcanearchives.tilenetwork;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NetworkAggregator {
  public static Map<UUID, Network> storage = new HashMap<>();

  public static Network byId (UUID networkId) {
    return storage.computeIfAbsent(networkId, (key) -> new Network(networkId));
  }
}

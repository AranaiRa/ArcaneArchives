package com.aranaira.arcanearchives.client;

import noobanidus.libs.noobutil.tracking.ItemTracking;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ManifestStorage {
  private static final Map<UUID, ManifestTracking> trackingInfo = new HashMap<>();

  public static void refresh(UUID domainId) {
  }

  public static class ManifestTracking {
    private long lastUpdated = -1;
    private UUID networkId;
    private ItemTracking tracking;
  }
}

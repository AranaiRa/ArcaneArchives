package com.aranaira.arcanearchives.client;

import com.aranaira.arcanearchives.network.Networking;
import com.aranaira.arcanearchives.network.packets.client.RequestManifestSyncPacket;
import noobanidus.libs.noobutil.tracking.ItemTracking;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ManifestStorage {
  private static final Map<UUID, ManifestTracking> trackingInfo = new HashMap<>();

  public static void refresh(UUID domainId) {
    ManifestTracking tracking = trackingInfo.get(domainId);
    long lastUpdated = -1;
    if (tracking != null) {
      lastUpdated = tracking.lastUpdated;
    }
    RequestManifestSyncPacket packet = new RequestManifestSyncPacket(lastUpdated, domainId);
    Networking.sendToServer(packet);
  }

  public static void updateTracking (UUID domainId, ItemTracking tracking, long lastUpdated) {
    trackingInfo.put(domainId, new ManifestTracking(domainId, lastUpdated, tracking));
  }

  public static class ManifestTracking {
    private UUID domainId;
    private long lastUpdated = -1;
    private ItemTracking tracking;

    public ManifestTracking(UUID domainId, long lastUpdated, ItemTracking tracking) {
      this.domainId = domainId;
      this.lastUpdated = lastUpdated;
      this.tracking = tracking;
    }
  }
}

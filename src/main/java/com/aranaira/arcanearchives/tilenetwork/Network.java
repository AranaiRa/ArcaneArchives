package com.aranaira.arcanearchives.tilenetwork;

import com.aranaira.arcanearchives.api.immanence.IImmanenceSubscriber;
import com.aranaira.arcanearchives.immanence.ImmanenceBus;
import com.aranaira.arcanearchives.tiles.NetworkedBaseTile;
import com.google.common.collect.ForwardingMap;

import javax.annotation.Nullable;
import java.util.*;

public class Network extends ForwardingMap<UUID, NetworkEntry> {
  private final Map<UUID, NetworkEntry> entries = new HashMap<>();
  private final UUID networkId;
  private final ImmanenceBus bus;

  public Network(UUID networkId) {
    this.networkId = networkId;
    this.bus = new ImmanenceBus(this);
  }

  public NetworkEntry add(NetworkedBaseTile tile) {
    NetworkEntry entry = new NetworkEntry(tile);
    return put(entry.uuid, entry);
  }

  public NetworkEntry remove(NetworkedBaseTile tile) {
    return remove(tile.getTileId());
  }

  @Nullable
  public NetworkEntry getEntryByTileId(UUID tileId) {
    return entries.get(tileId);
  }

  public List<NetworkEntry> getEntriesByClass(Class<?> clazz) {
    List<NetworkEntry> result = new ArrayList<>();
    for (NetworkEntry entry : entries.values()) {
      if (entry.clazz == clazz) {
        result.add(entry);
      }
    }
    return result;
  }

  public List<NetworkEntry> getEntriesByInstance(Class<?> parent) {
    List<NetworkEntry> result = new ArrayList<>();
    for (NetworkEntry entry : entries.values()) {
      if (entry.clazz.isAssignableFrom(parent)) {
        result.add(entry);
      }
    }
    return result;
  }

  public List<NetworkEntry> getImmanenceSubscribers () {
    return getEntriesByInstance(IImmanenceSubscriber.class);
  }

  public boolean containsTile(UUID tileId) {
    return entries.containsKey(tileId);
  }

  public UUID getNetworkId() {
    return networkId;
  }

  public ImmanenceBus getImmanenceBus() {
    return bus;
  }

  @Override
  protected Map<UUID, NetworkEntry> delegate() {
    return entries;
  }
}

package com.aranaira.arcanearchives.manifest;

import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

public class ManifestEntry {
  private Long2ObjectOpenHashMap<IndexDescriptor> descriptors = new Long2ObjectOpenHashMap<>();
  private Long2LongOpenHashMap quantities = new Long2LongOpenHashMap();
  private long quantity = 0;

  public ManifestEntry() {
  }

  public void accept (IndexEntry entry) {
    quantity += entry.getQuantity();
    long pos = entry.getPosition().toLong();
    descriptors.put(pos, entry.getDescriptor());
    quantities.put(pos, entry.getQuantity());
  }

  public long getTotalQuantity() {
    return quantity;
  }

  public Long2ObjectOpenHashMap<IndexDescriptor> getDescriptors() {
    return descriptors;
  }

  public Long2LongOpenHashMap getQuantities() {
    return quantities;
  }
}

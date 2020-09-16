package com.aranaira.arcanearchives.manifest;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;

public class ManifestItemEntry {
  private int dimension;
  private Long2ObjectOpenHashMap<IndexDescriptor> descriptors;

  private long quantity;

  public ManifestItemEntry(Long2ObjectOpenHashMap<IndexDescriptor> descriptors, long quantity, int dimension) {
    this.descriptors = descriptors;
    this.quantity = quantity;
    this.dimension = dimension;
  }

  public int getDimension() {
    return dimension;
  }

  public Map<BlockPos, IndexDescriptor> getDescriptors() {
    return descriptors;
  }

  public long getQuantity() {
    return quantity;
  }
}

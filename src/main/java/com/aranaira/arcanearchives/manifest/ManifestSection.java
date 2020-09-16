package com.aranaira.arcanearchives.manifest;

import net.minecraft.util.math.BlockPos;

public class ManifestSection {
  private final ManifestEntry outOfDimension;
  private final ManifestEntry outOfRange;
  private final ManifestEntry inRange;

  public ManifestSection() {
    this.outOfDimension = new ManifestEntry();
    this.outOfRange = new ManifestEntry();
    this.inRange = new ManifestEntry();
  }

  public void accept(BlockPos pos, int dimension, int maxRange, IndexEntry entry) {
    if (dimension != entry.getDimension()) {
      outOfDimension.accept(entry);
    }
    if (entry.getPosition().distanceSq(pos.getX(), pos.getY(), pos.getZ()) > maxRange * maxRange) {
      outOfRange.accept(entry);
    } else {
      inRange.accept(entry);
    }
  }
}

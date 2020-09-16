package com.aranaira.arcanearchives.manifest;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class ManifestSection {
  private final ManifestEntry outOfDimension;
  private final ManifestEntry outOfRange;
  private final ManifestEntry inRange;

  public ManifestSection(ItemStack reference) {
    this.outOfDimension = new ManifestEntry(reference, true, false);
    this.outOfRange = new ManifestEntry(reference, false, true);
    this.inRange = new ManifestEntry(reference, false, false);
  }

  public void accept(BlockPos pos, int dimension, int maxRange, IndexEntry entry) {
    if (dimension != entry.getDimension()) {
      outOfDimension.accept(pos, entry);
    }
    if (entry.getPosition().distanceSq(pos.getX(), pos.getY(), pos.getZ()) > maxRange * maxRange) {
      outOfRange.accept(pos, entry);
    } else {
      inRange.accept(pos, entry);
    }
  }
}

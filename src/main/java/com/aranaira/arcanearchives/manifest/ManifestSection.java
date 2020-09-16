package com.aranaira.arcanearchives.manifest;

import net.minecraft.item.ItemStack;

import java.util.List;

public class ManifestSection {
  private final ItemStack reference;
  private final List<ManifestItemEntry> outOfDimension;
  private final List<ManifestItemEntry> outOfRange;
  private final List<ManifestItemEntry> inRange;

  public ManifestSection(ItemStack reference, List<ManifestItemEntry> outOfDimension, List<ManifestItemEntry> outOfRange, List<ManifestItemEntry> inRange) {
    this.reference = reference;
    this.outOfDimension = outOfDimension;
    this.outOfRange = outOfRange;
    this.inRange = inRange;
  }
}

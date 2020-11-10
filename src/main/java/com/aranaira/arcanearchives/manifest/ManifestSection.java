package com.aranaira.arcanearchives.manifest;

import net.minecraft.item.ItemStack;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ManifestSection {
  private final ManifestEntry outOfDimension;
  private final ManifestEntry outOfRange;
  private final ManifestEntry inRange;

  public ManifestSection(ItemStack reference) {
    this.outOfDimension = new ManifestEntry(reference, true, false);
    this.outOfRange = new ManifestEntry(reference, false, true);
    this.inRange = new ManifestEntry(reference, false, false);
  }

  public void accept(BlockPos pos, RegistryKey<World> key, int maxRange, IndexEntry entry) {
    if (key != entry.getDimension()) {
      outOfDimension.accept(key, pos, entry);
    }
    if (entry.getPosition().distanceSq(pos) > maxRange * maxRange) {
      outOfRange.accept(key, pos, entry);
    } else {
      inRange.accept(key, pos, entry);
    }
  }
}

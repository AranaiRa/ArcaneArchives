package com.aranaira.arcanearchives.manifest;

import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ManifestEntry {
  private Long2ObjectOpenHashMap<IndexDescriptor> descriptorMap = new Long2ObjectOpenHashMap<>();
  private List<ManifestDescriptor> descriptors = new ArrayList<>();
  private Long2LongOpenHashMap quantities = new Long2LongOpenHashMap();
  private long quantity = 0;
  private final boolean outOfRange;
  private final boolean wrongDimension;
  private final ItemStack reference;

  public ManifestEntry(ItemStack reference, boolean outOfRange, boolean wrongDimension) {
    this.reference = reference;
    this.outOfRange = outOfRange;
    this.wrongDimension = wrongDimension;
  }

  public long getQuantity() {
    return quantity;
  }

  public ItemStack getStack() {
    return reference;
  }

  public boolean isOutOfRange() {
    return outOfRange;
  }

  public boolean isWrongDimension() {
    return wrongDimension;
  }

  public void accept (RegistryKey<World> key, BlockPos player, IndexEntry entry) {
    quantity += entry.getQuantity();
    long pos = entry.getPosition().toLong();
    descriptorMap.put(pos, entry.getDescriptor());
    descriptors.add(ManifestDescriptor.fromEntry(key, player, entry));
    quantities.put(pos, entry.getQuantity());
  }

  public Long2ObjectOpenHashMap<IndexDescriptor> getDescriptorMap() {
    return descriptorMap;
  }

  public Long2LongOpenHashMap getQuantities() {
    return quantities;
  }

  public void finalise () {
    descriptors.sort(Comparator.comparingDouble(ManifestDescriptor::getDistance));
  }
}

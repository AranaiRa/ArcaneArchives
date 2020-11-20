package com.aranaira.arcanearchives.manifest;

import net.minecraft.item.ItemStack;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class IndexSection {
  private IndexEntry noTag;
  private final List<IndexEntry> entries = new ArrayList<>();
  private final BlockPos position;
  private final RegistryKey<World> key;
  private final IndexDescriptor descriptor;

  public IndexSection(ItemStack reference, BlockPos position, RegistryKey<World> key, IndexDescriptor descriptor) {
    ItemStack noTag = reference.copy();
    noTag.setCount(1);
    noTag.setTag(null);
    this.descriptor = descriptor;
    this.noTag = new IndexEntry(noTag, position, key, descriptor);
    this.position = position;
    this.key = key;
  }

  public void receive(ItemStack stack, int slot) {
    if (noTag.test(stack)) {
      noTag.add(stack, slot);
    } else {
      for (IndexEntry entry : entries) {
        if (entry.test(stack)) {
          entry.add(stack, slot);
          return;
        }
      }

      IndexEntry newEntry = new IndexEntry(stack, position, key, descriptor);
      newEntry.add(stack, slot);
      entries.add(newEntry);
    }
  }

  public IndexEntry getNoTag() {
    return noTag;
  }

  public List<IndexEntry> getEntries() {
    return entries;
  }

  public BlockPos getPosition() {
    return position;
  }

  public RegistryKey<World> getDimension() {
    return key;
  }

  public IndexDescriptor getDescriptor() {
    return descriptor;
  }
}

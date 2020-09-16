package com.aranaira.arcanearchives.manifest;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class IndexSection {
  private IndexEntry noTag;
  private final List<IndexEntry> entries = new ArrayList<>();
  private final BlockPos position;
  private final int dimension;
  private final IndexDescriptor descriptor;

  public IndexSection(ItemStack reference, BlockPos position, int dimension, IndexDescriptor descriptor) {
    ItemStack noTag = reference.copy();
    noTag.setCount(1);
    noTag.setTagCompound(null);
    this.descriptor = descriptor;
    this.noTag = new IndexEntry(noTag, position, dimension, descriptor);
    this.position = position;
    this.dimension = dimension;
  }

  public void accept (IndexEntry entry) {
    if (entry.getTag() == null) {
      this.noTag = entry;
    } else {
      entries.add(entry);
    }
  }

  public void receive(ItemStack stack) {
    if (noTag.test(stack)) {
      noTag.add(stack);
    } else {
      for (IndexEntry entry : entries) {
        if (entry.test(stack)) {
          entry.add(stack);
          return;
        }
      }

      IndexEntry newEntry = new IndexEntry(stack, position, dimension, descriptor);
      newEntry.add(stack);
      entries.add(newEntry);
    }
  }

  public IndexEntry getNoTag() {
    return noTag;
  }

  public List<IndexEntry> getEntries() {
    return entries;
  }
}

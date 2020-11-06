package com.aranaira.arcanearchives.manifest;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Manifest {
  private final Int2ObjectOpenHashMap<Map<ItemStack, ManifestSection>> packedSections;

  protected Manifest() {
    this.packedSections = new Int2ObjectOpenHashMap<>();
  }

  public static class Builder {
    private boolean built = false;

    private int dimension;
    private int max;
    private BlockPos pos;

    private Manifest manifest = new Manifest();
    private Int2ObjectOpenHashMap<List<IndexEntry>> entries = new Int2ObjectOpenHashMap<>();
    private Int2ObjectOpenHashMap<List<ItemStack>> indexes = new Int2ObjectOpenHashMap<>();

    public Builder(PlayerEntity player, int maxDistance) {
      this.dimension = player.world.provider.getDimension();
      this.max = maxDistance * maxDistance;
      this.pos = player.getPosition();
    }

    private void addEntry (IndexEntry entry) {
      if (built) {
        throw new IllegalStateException("tried to add an entry to an already built manifest");
      }
      entries.computeIfAbsent(entry.getPacked(), p -> new ArrayList<>()).add(entry);
      List<ItemStack> indices = indexes.computeIfAbsent(entry.getPacked(), p -> new ArrayList<>());
      ItemStack index = ItemStack.EMPTY;
      for (ItemStack stack : indices) {
        if (entry.test(stack)) {
          index = stack;
          break;
        }
      }
      if (indices.isEmpty() || index.isEmpty()) {
        index = entry.getReference();
        indices.add(entry.getReference());
      }

      Map<ItemStack, ManifestSection> sectionMap = manifest.packedSections.computeIfAbsent(entry.getPacked(), p -> new HashMap<>());
      ManifestSection section = sectionMap.computeIfAbsent(index, i -> new ManifestSection());
      section.accept(pos, dimension, max, entry);
    }

    public Builder populate (PacketBuffer buf) {
      int count = buf.readInt();
      for (int i = 0; i < count; i++) {
        IndexEntry entry = IndexEntry.fromPacket(buf);
        addEntry(entry);
      }

      return this;
    }

    public Builder populate (List<IndexEntry> entries) {
      entries.forEach(this::addEntry);
      return this;
    }

    public Manifest create () {
      this.built = true;
      return manifest;
    }
  }
}

package com.aranaira.arcanearchives.manifest;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Manifest {
  private final int dimension;
  private final int maxDistance;
  private final Int2ObjectOpenHashMap<Map<ItemStack, ManifestSection>> packedSections;

  protected Manifest(EntityPlayer player, int maxDistance) {
    this.dimension = player.world.provider.getDimension();
    this.maxDistance = maxDistance;
    this.packedSections = new Int2ObjectOpenHashMap<>();
  }

  public static class Builder {
    private final int dimension;
    private final int maxDistance;

    public Builder(EntityPlayer player, int maxDistance, PacketBuffer buf) {
      this.dimension = player.world.provider.getDimension();
      this.maxDistance = maxDistance * maxDistance;

      int count = buf.readInt();
      Int2ObjectOpenHashMap<List<IndexEntry>> entries = new Int2ObjectOpenHashMap<>();
      for (int i = 0; i < count; i++) {
        IndexEntry entry = IndexEntry.fromPacket(buf);
        entries.computeIfAbsent(entry.getPacked(), p -> new ArrayList<>()).add(entry);
      }
    }
  }
}

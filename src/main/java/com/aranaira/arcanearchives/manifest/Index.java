package com.aranaira.arcanearchives.manifest;

import com.aranaira.arcanearchives.types.ISerializePacketBuffer;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class Index implements ISerializePacketBuffer<Index> {
  private final Int2ObjectOpenHashMap<Long2ObjectOpenHashMap<Int2ObjectOpenHashMap<IndexSection>>> storage = new Int2ObjectOpenHashMap<>();

  public Index() {
  }

  private IndexSection resolveSection(int dimension, BlockPos position, int packed, ItemStack reference, IndexDescriptor descriptor) {
    long pos = position.toLong();
    Long2ObjectOpenHashMap<Int2ObjectOpenHashMap<IndexSection>> dimData = storage.computeIfAbsent(dimension, p -> new Long2ObjectOpenHashMap<>());
    Int2ObjectOpenHashMap<IndexSection> posData = dimData.computeIfAbsent(pos, p -> new Int2ObjectOpenHashMap<>());
    return posData.computeIfAbsent(packed, p -> new IndexSection(reference, position, dimension, descriptor));
  }

  public static class Builder {
    private Index index;
    boolean built = false;

    public Builder() {
      this.index = new Index();
    }

    public Builder populate(ItemStack stack, BlockPos position, int dimension, IndexDescriptor descriptor, int slot) {
      if (built) {
        throw new IllegalStateException("tried to populate an already-built builder");
      }
      int packed = RecipeItemHelper.pack(stack);
      IndexSection section = index.resolveSection(dimension, position, packed, stack, descriptor);
      section.receive(stack, slot);
      return this;
    }

    public Index create () {
      built = true;
      return index;
    }
  }

  @Override
  public void toPacket(PacketBuffer buf) {
    List<IndexEntry> fullEntries = new ArrayList<>();
    storage.values().forEach(o -> o.values().forEach(i -> i.values().forEach(u -> {
      fullEntries.add(u.getNoTag());
      fullEntries.addAll(u.getEntries());
    })));
    buf.writeInt(fullEntries.size());
    for (IndexEntry entry : fullEntries) {
      entry.toPacket(buf);
    }
  }
}

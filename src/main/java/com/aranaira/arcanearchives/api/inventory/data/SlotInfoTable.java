package com.aranaira.arcanearchives.api.inventory.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SlotInfoTable {
  public static Codec<SlotInfoTable> CODEC = RecordCodecBuilder.create((instance) -> instance.group(CompoundNBT.CODEC.listOf().fieldOf("info").forGetter(o -> o.info.values().stream().map(SlotInfoBase::getTag).collect(Collectors.toList()))).apply(instance, SlotInfoTable::new));

  private final Map<String, SlotInfoBase<?>> info;

  public SlotInfoTable() {
    this.info = new HashMap<>();
  }

  public SlotInfoTable(List<CompoundNBT> info) {
    this();
    info.stream().map(SlotInfoBase::fromNBT).forEach(this::add);
  }

  public boolean isEmpty () {
    return info.isEmpty();
  }

  @Nullable
  public SlotInfoBase<?> getByKey(String key) {
    return info.get(key);
  }

  public void add(SlotInfoBase<?> value) {
    info.put(value.getKey(), value);
  }
}

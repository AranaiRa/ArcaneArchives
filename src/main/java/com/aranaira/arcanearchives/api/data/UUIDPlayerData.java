package com.aranaira.arcanearchives.api.data;

import com.aranaira.arcanearchives.api.reference.Identifiers;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class UUIDPlayerData<T> extends WorldSavedData {
  private final Map<UUID, Map<UUID, T>> VALUE_MAP = new HashMap<>();
  private final NBTConverter<T> converter;
  private final NBTGetter<T> getter;
  @Nullable
  private final T defaultValue;

  public UUIDPlayerData(String id, NBTConverter<T> converter, NBTGetter<T> getter, @Nullable T defaultValue) {
    super(id);
    this.converter = converter;
    this.getter = getter;
    this.defaultValue = defaultValue;
  }

  public T get(UUID player, UUID id) {
    return VALUE_MAP.computeIfAbsent(id, (k) -> new HashMap<>()).computeIfAbsent(player, (k) -> defaultValue);
  }

  public void set(UUID player, UUID id, T value) {
    VALUE_MAP.computeIfAbsent(id, (k) -> new HashMap<>()).put(player, value);
  }

  @Override
  public void load(CompoundNBT tag) {
    ListNBT pairs = tag.getList(Identifiers.Data.slotDataList, Constants.NBT.TAG_COMPOUND);
    VALUE_MAP.clear();
    for (int i = 0; i < pairs.size(); i++) {
      CompoundNBT thisTag = pairs.getCompound(i);
      VALUE_MAP.computeIfAbsent(thisTag.getUUID(Identifiers.Data.slotUUID), (k) -> new HashMap<>()).put(thisTag.getUUID(Identifiers.Data.slotPlayerId), getter.get(thisTag, Identifiers.Data.slotValue));
    }
  }

  @Override
  public CompoundNBT save(CompoundNBT pCompound) {
    ListNBT pairs = new ListNBT();
    for (Map.Entry<UUID, Map<UUID, T>> set : VALUE_MAP.entrySet()) {
      for (Map.Entry<UUID, T> entry : set.getValue().entrySet()) {
        if (entry.getValue() == null) {
          continue;
        }

        CompoundNBT tag = new CompoundNBT();
        tag.putUUID(Identifiers.Data.slotUUID, entry.getKey());
        converter.put(tag, Identifiers.Data.slotValue, entry.getValue());
        tag.putUUID(Identifiers.Data.slotPlayerId, set.getKey());
      }
    }
    pCompound.put(Identifiers.Data.slotDataList, pairs);
    return pCompound;
  }

  @FunctionalInterface
  public interface NBTConverter<T> {
    void put(CompoundNBT nbt, String key, T value);
  }

  @FunctionalInterface
  public interface NBTGetter<T> {
    T get (CompoundNBT nbt, String key);
  }


}

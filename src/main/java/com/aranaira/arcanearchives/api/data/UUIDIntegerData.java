package com.aranaira.arcanearchives.api.data;

import com.aranaira.arcanearchives.api.reference.Identifiers;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

import java.util.*;

public class UUIDIntegerData extends WorldSavedData {
  private final Map<UUID, Object2IntOpenHashMap<UUID>> VALUE_MAP = new HashMap<>();

  public UUIDIntegerData(String id) {
    super(id);
  }

  public int get (UUID player, UUID id) {
    return VALUE_MAP.computeIfAbsent(id, (k) -> new Object2IntOpenHashMap<>()).computeIntIfAbsent(player, (k) -> -1);
  }

  public void set (UUID player, UUID id, int slot) {
    VALUE_MAP.computeIfAbsent(id, (k) -> new Object2IntOpenHashMap<>()).put(player, slot);
  }

  @Override
  public void load(CompoundNBT tag) {
    ListNBT pairs = tag.getList(Identifiers.Data.slotDataList, Constants.NBT.TAG_COMPOUND);
    VALUE_MAP.clear();
    for (int i = 0; i < pairs.size(); i++) {
      CompoundNBT thisTag = pairs.getCompound(i);
      VALUE_MAP.computeIfAbsent(thisTag.getUUID(Identifiers.Data.slotUUID), (k) -> new Object2IntOpenHashMap<>()).put(thisTag.getUUID(Identifiers.Data.slotPlayerId), thisTag.getInt(Identifiers.Data.slotValue));
    }
  }

  @Override
  public CompoundNBT save(CompoundNBT pCompound) {
    ListNBT pairs = new ListNBT();
    for (Map.Entry<UUID, Object2IntOpenHashMap<UUID>> set : VALUE_MAP.entrySet()) {
      for (Object2IntMap.Entry<UUID> entry : set.getValue().object2IntEntrySet()) {
        CompoundNBT tag = new CompoundNBT();
        tag.putUUID(Identifiers.Data.slotUUID, entry.getKey());
        tag.putInt(Identifiers.Data.slotValue, entry.getIntValue());
        tag.putUUID(Identifiers.Data.slotPlayerId, set.getKey());
      }
    }
    pCompound.put(Identifiers.Data.slotDataList, pairs);
    return pCompound;
  }
}

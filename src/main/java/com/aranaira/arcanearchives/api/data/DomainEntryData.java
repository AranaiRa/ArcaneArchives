package com.aranaira.arcanearchives.api.data;

import com.aranaira.arcanearchives.api.domain.DomainEntry;
import com.aranaira.arcanearchives.api.reference.Identifiers;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DomainEntryData extends WorldSavedData {
  public static final String ID = "ArcaneArchives-DomainEntries";

  private final Map<UUID, DomainEntry> domainData = new HashMap<>();

  public DomainEntryData () {
    this(ID);
  }

  public DomainEntryData(String p_i2141_1_) {
    super(p_i2141_1_);
  }

  @Nullable
  public DomainEntry getEntry (UUID id) {
    return domainData.get(id);
  }

  @Override
  public void load(CompoundNBT tag) {
    domainData.clear();
    ListNBT tags = tag.getList(Identifiers.DomainEntryData.domainEntries, Constants.NBT.TAG_COMPOUND);
    for (int i = 0; i < tags.size(); i++) {
      CompoundNBT domainTag = tags.getCompound(i);
      DomainEntry result = DomainEntry.fromNBT(domainTag);
      domainData.put(result.getEntityId(), result);
    }
  }

  @Override
  public CompoundNBT save(CompoundNBT pCompound) {
    ListNBT result = new ListNBT();
    for (DomainEntry entry : domainData.values()) {
      result.add(entry.serializeNBT());
    }
    pCompound.put(Identifiers.DomainEntryData.domainEntries, result);
    return pCompound;
  }
}

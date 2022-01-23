package com.aranaira.arcanearchives.api.data;

import com.aranaira.arcanearchives.api.domain.impl.DomainEntryImpl;
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

  private final Map<UUID, DomainEntryImpl> domainData = new HashMap<>();

  public DomainEntryData () {
    this(ID);
  }

  public DomainEntryData(String p_i2141_1_) {
    super(p_i2141_1_);
  }

  // TODO: getOrCreateEntry
  @Nullable
  public DomainEntryImpl getEntry (UUID id) {
    return domainData.get(id);
  }

  @Override
  public void load(CompoundNBT tag) {
    domainData.clear();
    ListNBT tags = tag.getList(Identifiers.DomainEntryData.domainEntries, Constants.NBT.TAG_COMPOUND);
    for (int i = 0; i < tags.size(); i++) {
      CompoundNBT domainTag = tags.getCompound(i);
      DomainEntryImpl result = DomainEntryImpl.fromNBT(domainTag);
      domainData.put(result.getEntityId(), result);
    }
  }

  @Override
  public CompoundNBT save(CompoundNBT pCompound) {
    ListNBT result = new ListNBT();
    for (DomainEntryImpl entry : domainData.values()) {
      result.add(entry.serializeNBT());
    }
    pCompound.put(Identifiers.DomainEntryData.domainEntries, result);
    return pCompound;
  }
}

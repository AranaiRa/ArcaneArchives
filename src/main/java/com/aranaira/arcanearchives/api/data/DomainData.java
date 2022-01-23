package com.aranaira.arcanearchives.api.data;

import com.aranaira.arcanearchives.api.domain.impl.DomainEntryImpl;
import com.aranaira.arcanearchives.api.domain.impl.DomainImpl;
import com.aranaira.arcanearchives.api.reference.Identifiers;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DomainData extends WorldSavedData {
  public static final String ID = "ArcaneArchives-DomainData";

  private final Map<UUID, DomainImpl> domainData = new HashMap<>();

  public DomainData () {
    this(ID);
  }

  public DomainData(String p_i2141_1_) {
    super(p_i2141_1_);
  }

  @Nullable
  public DomainImpl getDomain (UUID id) {
    return domainData.get(id);
  }

  @Override
  public void load(CompoundNBT tag) {
    domainData.clear();
    ListNBT tags = tag.getList(Identifiers.DomainData.domainData, Constants.NBT.TAG_COMPOUND);
    for (int i = 0; i < tags.size(); i++) {
      CompoundNBT domainTag = tags.getCompound(i);
      DomainImpl result = DomainImpl.fromNBT(domainTag);
      domainData.put(result.getDomainId(), result);
    }
  }

  @Override
  public CompoundNBT save(CompoundNBT pCompound) {
    ListNBT result = new ListNBT();
    for (DomainImpl entry : domainData.values()) {
      result.add(entry.serializeNBT());
    }
    pCompound.put(Identifiers.DomainData.domainData, result);
    return pCompound;
  }
}
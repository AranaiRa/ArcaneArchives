package com.aranaira.arcanearchives.api.data;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.storage.WorldSavedData;

public class DomainData extends WorldSavedData {
  // Store all known domain entries, don't store enlisted or separate

  public DomainData(String p_i2141_1_) {
    super(p_i2141_1_);
  }

  @Override
  public void load(CompoundNBT p_76184_1_) {

  }

  @Override
  public CompoundNBT save(CompoundNBT pCompound) {
    return null;
  }
}

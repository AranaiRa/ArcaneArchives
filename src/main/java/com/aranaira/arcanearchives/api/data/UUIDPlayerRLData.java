package com.aranaira.arcanearchives.api.data;

import net.minecraft.util.ResourceLocation;

public class UUIDPlayerRLData extends UUIDPlayerData<ResourceLocation> {
  public UUIDPlayerRLData(String id) {
    super(id, (nbt, key, value) -> nbt.putString(key, value.toString()), (nbt, key) -> new ResourceLocation(nbt.getString(key)), null);
  }
}

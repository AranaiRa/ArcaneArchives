package com.aranaira.arcanearchives;

import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IRegistryLocation {
  @Nullable
  ResourceLocation getRegistryLocation ();

  void setRegistryLocation (@Nonnull ResourceLocation rl);
}

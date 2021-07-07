package com.aranaira.arcanearchives.api;

import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IResourceLocation {
  @Nullable
  ResourceLocation getResourceLocation ();

  void setResourceLocation (@Nonnull ResourceLocation rl);
}

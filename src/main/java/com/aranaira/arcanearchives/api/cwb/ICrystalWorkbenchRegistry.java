package com.aranaira.arcanearchives.api.cwb;

import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface ICrystalWorkbenchRegistry<V extends ICrystalWorkbenchRecipe> {
  void register(V value);

  void registerAll(@SuppressWarnings("unchecked") V... values);

  boolean containsKey(ResourceLocation key);

  boolean containsValue(V value);

  @Nullable
  V getValue(ResourceLocation key);

  @Nullable
  ResourceLocation getKey(V value);

  @Nonnull
  Set<ResourceLocation> getKeys();

  @Nonnull
  Collection<V> getValues();

  @Nonnull
  Set<Map.Entry<ResourceLocation, V>> getEntries();
}

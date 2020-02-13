package com.aranaira.arcanearchives.api.cwb;

import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ICrystalWorkbenchRegistry {
  void register(CrystalWorkbenchRecipe value);

  void registerAll(@SuppressWarnings("unchecked") CrystalWorkbenchRecipe... values);

  void registerAll(List<CrystalWorkbenchRecipe> values);

  boolean containsKey(ResourceLocation key);

  boolean containsValue(CrystalWorkbenchRecipe value);

  @Nullable
  CrystalWorkbenchRecipe getValue(ResourceLocation key);

  @Nullable
  ResourceLocation getKey(CrystalWorkbenchRecipe value);

  int getIndex (CrystalWorkbenchRecipe value);

  int getIndex (ResourceLocation key);

  @Nullable
  CrystalWorkbenchRecipe getValueByIndex (int index);

  @Nullable
  CrystalWorkbenchRecipe find (WorkbenchCrafting crafting);

  @Nonnull
  Set<ResourceLocation> getKeys();

  @Nonnull
  Collection<CrystalWorkbenchRecipe> getValues();

  @Nonnull
  Set<Map.Entry<ResourceLocation, CrystalWorkbenchRecipe>> getEntries();
}

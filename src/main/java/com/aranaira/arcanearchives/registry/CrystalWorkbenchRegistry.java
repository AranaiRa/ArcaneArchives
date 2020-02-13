package com.aranaira.arcanearchives.registry;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.api.cwb.*;
import com.google.common.collect.BiMap;
import com.google.common.collect.ForwardingMap;
import com.google.common.collect.HashBiMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Mod.EventBusSubscriber(modid = ArcaneArchives.MODID)
public class CrystalWorkbenchRegistry extends ForwardingMap<ResourceLocation, CrystalWorkbenchRecipe> implements ICrystalWorkbenchRegistry {
  private static final CrystalWorkbenchRegistry REGISTRY = new CrystalWorkbenchRegistry();
  private static final BiMap<ResourceLocation, CrystalWorkbenchRecipe> REGISTRY_MAP = HashBiMap.create();
  private static final Int2ObjectOpenHashMap<CrystalWorkbenchRecipe> INDEX_MAP = new Int2ObjectOpenHashMap<>();
  private static int index = 0;

  public CrystalWorkbenchRegistry() {
    super();
  }

  @Override
  protected Map<ResourceLocation, CrystalWorkbenchRecipe> delegate() {
    return REGISTRY_MAP;
  }

  public static CrystalWorkbenchRegistry getRegistry() {
    return REGISTRY;
  }

  @Override
  public void register(CrystalWorkbenchRecipe value) {
    if (value.getRegistryName() == null) {
      throw new NullPointerException("Registry name for " + value.toString() + " cannot be null");
    }
    CrystalWorkbenchRecipe result = REGISTRY_MAP.putIfAbsent(value.getRegistryName(), value);
    if (result != null) {
      throw new IllegalArgumentException(value.getRegistryName().toString() + " has already been registered for " + result.toString() + ", cannot register it for " + value.toString());
    }
    INDEX_MAP.put(index, value);
    value.setIndex(index);
    index++;
  }

  @Override
  public void registerAll(CrystalWorkbenchRecipe... values) {
    for (CrystalWorkbenchRecipe recipe : values) {
      register(recipe);
    }
  }

  @Override
  public void registerAll(List<CrystalWorkbenchRecipe> values) {
    for (CrystalWorkbenchRecipe recipe : values) {
      register(recipe);
    }
  }

  @Override
  public boolean containsKey(ResourceLocation key) {
    return this.containsKey((Object) key);
  }

  @Override
  public boolean containsValue(CrystalWorkbenchRecipe value) {
    return this.containsValue((Object) value);
  }

  @Nullable
  @Override
  public CrystalWorkbenchRecipe getValue(ResourceLocation key) {
    return this.get(key);
  }

  @Nullable
  @Override
  public ResourceLocation getKey(CrystalWorkbenchRecipe value) {
    return REGISTRY_MAP.inverse().get(value);
  }

  @Override
  public int getIndex(CrystalWorkbenchRecipe value) {
    return value.getIndex();
  }

  @Override
  public int getIndex(ResourceLocation key) {
    CrystalWorkbenchRecipe recipe = get(key);
    if (recipe == null) {
      return -1;
    }

    return recipe.getIndex();
  }

  @Nullable
  @Override
  public CrystalWorkbenchRecipe getValueByIndex(int index) {
    return INDEX_MAP.get(index);
  }

  @Nullable
  @Override
  public CrystalWorkbenchRecipe find(WorkbenchCrafting crafting) {
    for (CrystalWorkbenchRecipe recipe : getValues()) {
      MatchResult result = recipe.matches(crafting);
      if (result.matches()) {
        return recipe;
      }
    }

    return null;
  }

  @Nonnull
  @Override
  public Set<ResourceLocation> getKeys() {
    return this.keySet();
  }

  @Nonnull
  @Override
  public Collection<CrystalWorkbenchRecipe> getValues() {
    return this.values();
  }

  @Nonnull
  @Override
  public Set<Entry<ResourceLocation, CrystalWorkbenchRecipe>> getEntries() {
    return this.entrySet();
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public static void onRecipeRegistration(RegistryEvent.Register<IRecipe> recipes) {
    MinecraftForge.EVENT_BUS.post(new CrystalWorkbenchRegisterEvent(getRegistry()));
  }
}

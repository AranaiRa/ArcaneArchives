package com.aranaira.arcanearchives.registry;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.api.CrystalWorkbenchRecipe;
import com.aranaira.arcanearchives.api.CrystalWorkbenchRegisterEvent;
import com.aranaira.arcanearchives.api.ICrystalWorkbenchRegistry;
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
import java.util.HashMap;
import java.util.Set;

// TODO: Make int ID-backed list
@Mod.EventBusSubscriber(modid= ArcaneArchives.MODID)
public class CrystalWorkbenchRegistry extends HashMap<ResourceLocation, CrystalWorkbenchRecipe> implements ICrystalWorkbenchRegistry<CrystalWorkbenchRecipe> {
  private static final CrystalWorkbenchRegistry REGISTRY = new CrystalWorkbenchRegistry();

  public CrystalWorkbenchRegistry() {
    super();
  }

  public static CrystalWorkbenchRegistry getRegistry() {
    return REGISTRY;
  }

  @Override
  public void register(CrystalWorkbenchRecipe value) {
    if (value.getRegistryName() == null) {
      throw new NullPointerException("Registry name for " + value.toString() + " cannot be null");
    }
    CrystalWorkbenchRecipe result = putIfAbsent(value.getRegistryName(), value);
    if (result != null) {
      throw new IllegalArgumentException(value.getRegistryName().toString() + " has already been registered for " + result.toString() + ", cannot register it for " + value.toString());
    }
  }

  @Override
  public void registerAll(CrystalWorkbenchRecipe... values) {
    for (CrystalWorkbenchRecipe recipe : values) {
      register(recipe);
    }
  }

  @Override
  public boolean containsKey(ResourceLocation key) {
    return super.containsKey(key);
  }

  @Override
  public boolean containsValue(CrystalWorkbenchRecipe value) {
    return super.containsValue(value);
  }

  @Nullable
  @Override
  public CrystalWorkbenchRecipe getValue(ResourceLocation key) {
    return get(key);
  }

  @Nullable
  @Override
  public ResourceLocation getKey(CrystalWorkbenchRecipe value) {
    return getKey(value);
  }

  @Nonnull
  @Override
  public Set<ResourceLocation> getKeys() {
    return keySet();
  }

  @Nonnull
  @Override
  public Collection<CrystalWorkbenchRecipe> getValues() {
    return values();
  }

  @Nonnull
  @Override
  public Set<Entry<ResourceLocation, CrystalWorkbenchRecipe>> getEntries() {
    return entrySet();
  }

  @SubscribeEvent(priority= EventPriority.LOWEST)
  public static void onRecipeRegistration (RegistryEvent.Register<IRecipe> recipes) {
    MinecraftForge.EVENT_BUS.post(new CrystalWorkbenchRegisterEvent(getRegistry()));
  }
}

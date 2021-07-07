package com.aranaira.arcanearchives.api.crafting.processors.registry;

import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class ProcessorRegistries {
  private static final Map<ResourceLocation, ProcessorRegistry<?, ?, ?>> registries = new HashMap<>();

  @Nullable
  public static ProcessorRegistry<?, ?, ?> getRegistry(ResourceLocation location) {
    return registries.get(location);
  }

  public static void register(ProcessorRegistry<?, ?, ?> registry) {
    if (registry.getResourceLocation() == null) {
      throw new IllegalArgumentException("Invalid resource location for ProcessorRegistry");
    }
    if (registries.containsKey(registry.getResourceLocation())) {
      throw new IllegalArgumentException("ProcessorRegistry is already registered for ResourceLocation: '" + registry.getResourceLocation() + "'");
    }
    registries.put(registry.getResourceLocation(), registry);
  }
}

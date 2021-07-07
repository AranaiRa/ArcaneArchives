package com.aranaira.arcanearchives.api.crafting.processors.registry;

import com.aranaira.arcanearchives.api.IResourceLocation;
import com.aranaira.arcanearchives.api.container.IPlayerContainer;
import com.aranaira.arcanearchives.api.crafting.processors.IngredientProcessor;
import com.aranaira.arcanearchives.api.inventory.IArcaneInventory;
import com.aranaira.arcanearchives.api.tiles.IArcaneArchivesTile;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;

public class ProcessorRegistry<H extends IArcaneInventory, C extends Container & IPlayerContainer, T extends TileEntity & IArcaneArchivesTile> implements IResourceLocation {
  private ResourceLocation resourceLocation;

  private final HashMap<ResourceLocation, IngredientProcessor<H, C, T>> registry = new HashMap<>();

  public ProcessorRegistry(ResourceLocation location) {
    this.setResourceLocation(location);
  }

  public Collection<IngredientProcessor<H, C, T>> getProcessors() {
    return registry.values();
  }

  @Nullable
  public IngredientProcessor<H, C, T> getValue(ResourceLocation key) {
    return registry.get(key);
  }

  public void register(IngredientProcessor<H, C, T> processor) {
    if (processor.getResourceLocation() == null) {
      throw new IllegalArgumentException("Invalid processor for processor registry: '" + this.getResourceLocation() + "', ResourceLocation is null");
    }
    if (registry.containsKey(processor.getResourceLocation())) {
      throw new IllegalArgumentException("Processor is already registered for ResourceLocation: '" + processor.getResourceLocation() + "'");
    }
    registry.put(processor.getResourceLocation(), processor);
  }

  @Nullable
  @Override
  public ResourceLocation getResourceLocation() {
    return resourceLocation;
  }

  @Override
  public void setResourceLocation(@Nonnull ResourceLocation rl) {
    this.resourceLocation = rl;
  }
}

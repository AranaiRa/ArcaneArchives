package com.aranaira.arcanearchives.core.recipes.processors;

import com.aranaira.arcanearchives.api.crafting.processors.OutputProcessor;
import com.aranaira.arcanearchives.core.inventory.container.CrystalWorkbenchContainer;
import com.aranaira.arcanearchives.core.inventory.handlers.CrystalWorkbenchInventory;
import com.aranaira.arcanearchives.core.tiles.CrystalWorkbenchTile;
import net.minecraft.util.ResourceLocation;

public abstract class WorkbenchOutputProcessor extends OutputProcessor<CrystalWorkbenchInventory, CrystalWorkbenchContainer, CrystalWorkbenchTile> {
  public WorkbenchOutputProcessor(ResourceLocation registry, ResourceLocation resourceLocation) {
    super(registry, resourceLocation);
  }
}

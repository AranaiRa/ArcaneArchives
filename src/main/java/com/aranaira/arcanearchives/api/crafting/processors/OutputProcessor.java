package com.aranaira.arcanearchives.api.crafting.processors;

import com.aranaira.arcanearchives.api.container.IPlayerContainer;
import com.aranaira.arcanearchives.api.inventory.IArcaneInventory;
import com.aranaira.arcanearchives.api.tiles.IArcaneArchivesTile;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public abstract class OutputProcessor<H extends IArcaneInventory, C extends Container & IPlayerContainer, T extends TileEntity & IArcaneArchivesTile> extends Processor<H, C, T> implements IOutputProcessor<H, C, T> {
  public OutputProcessor(ResourceLocation registry, ResourceLocation resourceLocation) {
    super(registry, resourceLocation);
  }
}

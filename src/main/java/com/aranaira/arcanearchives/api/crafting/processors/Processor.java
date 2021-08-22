package com.aranaira.arcanearchives.api.crafting.processors;

import com.aranaira.arcanearchives.api.crafting.ArcaneCrafting;
import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class Processor<T extends ArcaneCrafting<?, ?, ?>> extends ForgeRegistryEntry<IProcessor<?>> implements IProcessor<T> {
  public Processor() {
  }
}

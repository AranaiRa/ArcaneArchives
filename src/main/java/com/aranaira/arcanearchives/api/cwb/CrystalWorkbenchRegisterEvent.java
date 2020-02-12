package com.aranaira.arcanearchives.api.cwb;

import net.minecraftforge.fml.common.eventhandler.Event;

public class CrystalWorkbenchRegisterEvent extends Event {
  private final ICrystalWorkbenchRegistry<? extends CrystalWorkbenchRecipe> registry;

  public CrystalWorkbenchRegisterEvent(ICrystalWorkbenchRegistry<? extends CrystalWorkbenchRecipe> registry) {
    this.registry = registry;
  }

  public ICrystalWorkbenchRegistry<? extends CrystalWorkbenchRecipe> getRegistry() {
    return registry;
  }
}

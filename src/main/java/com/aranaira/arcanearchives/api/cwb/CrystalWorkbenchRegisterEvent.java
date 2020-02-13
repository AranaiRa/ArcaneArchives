package com.aranaira.arcanearchives.api.cwb;

import net.minecraftforge.fml.common.eventhandler.Event;

public class CrystalWorkbenchRegisterEvent extends Event {
  private final ICrystalWorkbenchRegistry registry;

  public CrystalWorkbenchRegisterEvent(ICrystalWorkbenchRegistry registry) {
    this.registry = registry;
  }

  public ICrystalWorkbenchRegistry getRegistry() {
    return registry;
  }
}

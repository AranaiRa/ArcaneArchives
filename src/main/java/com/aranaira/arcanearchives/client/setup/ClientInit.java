package com.aranaira.arcanearchives.client.setup;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ClientInit {
  public static void init () {
    IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
    modBus.addListener(ClientSetup::setup);
  }
}

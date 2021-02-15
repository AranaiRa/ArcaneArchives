package com.aranaira.arcanearchives.setup;

import com.aranaira.arcanearchives.network.Networking;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class CommonSetup {
  public static void setup (FMLCommonSetupEvent event) {
    Networking.register();
    event.enqueueWork(() -> {

    });
  }
}

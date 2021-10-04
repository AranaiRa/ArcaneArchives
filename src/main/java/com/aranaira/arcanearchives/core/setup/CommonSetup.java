package com.aranaira.arcanearchives.core.setup;

import com.aranaira.arcanearchives.core.init.ModRecipes;
import com.aranaira.arcanearchives.core.network.Networking;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class CommonSetup {
  public static void setup (FMLCommonSetupEvent event) {
    Networking.register();
    event.enqueueWork(() -> {
      ModRecipes.Types.register();
    });
  }
}

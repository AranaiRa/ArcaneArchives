package com.aranaira.arcanearchives.client.setup;

import com.aranaira.arcanearchives.client.screen.RadiantChestScreen;
import com.aranaira.arcanearchives.core.init.ModContainers;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientSetup {
  public static void setup(FMLClientSetupEvent event) {
    event.enqueueWork(() -> {
      ScreenManager.registerFactory(ModContainers.RADIANT_CHEST.get(), RadiantChestScreen::new);
    });
  }
}

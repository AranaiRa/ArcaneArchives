package com.aranaira.arcanearchives.setup;

import com.aranaira.arcanearchives.client.RadiantChestScreen;
import com.aranaira.arcanearchives.init.ModContainers;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientSetup {
  public static void setup(FMLClientSetupEvent event) {
    event.enqueueWork(() -> {
      ScreenManager.registerFactory(ModContainers.RADIANT_CHEST.get(), RadiantChestScreen::new);
    });
  }
}

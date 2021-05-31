package com.aranaira.arcanearchives.client.setup;

import com.aranaira.arcanearchives.client.screen.CrystalWorkbenchScreen;
import com.aranaira.arcanearchives.client.screen.RadiantChestScreen;
import com.aranaira.arcanearchives.core.init.ModBlocks;
import com.aranaira.arcanearchives.core.init.ModContainers;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientSetup {
  public static void setup(FMLClientSetupEvent event) {
    event.enqueueWork(() -> {
      ScreenManager.registerFactory(ModContainers.RADIANT_CHEST.get(), RadiantChestScreen::new);
      ScreenManager.registerFactory(ModContainers.CRYSTAL_WORKBENCH.get(), CrystalWorkbenchScreen::new);
      RenderType cutout = RenderType.getCutoutMipped();
      RenderTypeLookup.setRenderLayer(ModBlocks.CRYSTAL_WORKBENCH.get(), cutout);
    });
  }
}

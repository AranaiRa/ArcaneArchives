package com.aranaira.arcanearchives.client.setup;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.client.screen.CrystalWorkbenchScreen;
import com.aranaira.arcanearchives.client.screen.RadiantChestScreen;
import com.aranaira.arcanearchives.core.init.ModBlocks;
import com.aranaira.arcanearchives.core.init.ModContainers;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid= ArcaneArchives.MODID, bus= Mod.EventBusSubscriber.Bus.MOD, value= Dist.CLIENT)
public class ClientSetup {
  @SubscribeEvent
  public static void setup(FMLClientSetupEvent event) {
    event.enqueueWork(() -> {
      ScreenManager.register(ModContainers.RADIANT_CHEST.get(), RadiantChestScreen::new);
      ScreenManager.register(ModContainers.CRYSTAL_WORKBENCH.get(), CrystalWorkbenchScreen::new);
      RenderType cutout = RenderType.cutoutMipped();
      RenderTypeLookup.setRenderLayer(ModBlocks.CRYSTAL_WORKBENCH.get(), cutout);
    });
  }
}

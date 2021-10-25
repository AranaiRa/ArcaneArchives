package com.aranaira.arcanearchives.core.setup;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.core.init.ModRecipes;
import com.aranaira.arcanearchives.core.network.Networking;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid= ArcaneArchives.MODID, bus= Mod.EventBusSubscriber.Bus.MOD)
public class CommonSetup {
  @SubscribeEvent
  public static void setup (FMLCommonSetupEvent event) {
    Networking.register();
    event.enqueueWork(() -> {
      ModRecipes.Types.register();
    });
  }
}

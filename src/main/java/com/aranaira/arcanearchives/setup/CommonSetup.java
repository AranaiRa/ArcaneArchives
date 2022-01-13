package com.aranaira.arcanearchives.setup;

import com.aranaira.arcanearchives.api.ArcaneArchivesAPI;
import com.aranaira.arcanearchives.init.ModRecipes;
import com.aranaira.arcanearchives.network.Networking;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid= ArcaneArchivesAPI.MODID, bus= Mod.EventBusSubscriber.Bus.MOD)
public class CommonSetup {
  @SubscribeEvent
  public static void setup (FMLCommonSetupEvent event) {
    Networking.register();
    event.enqueueWork(() -> {
      ModRecipes.Types.register();
    });
  }
}
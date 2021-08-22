package com.aranaira.arcanearchives.core.event;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.api.crafting.processors.IProcessor;
import com.aranaira.arcanearchives.core.init.ModRegistries;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryManager;

@Mod.EventBusSubscriber(modid = ArcaneArchives.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryEvents {
  private static <T> Class<T> c(Class<?> cls) {
    return (Class<T>) cls;
  }

  public static ResourceLocation PROCESSOR_KEY = new ResourceLocation(ArcaneArchives.MODID, "processor");

  @SubscribeEvent
  public static void onNewRegistry(RegistryEvent.NewRegistry event) {
    ArcaneArchives.LOG.info("Creating Processor registry");
    new RegistryBuilder<IProcessor<?>>()
        .setName(PROCESSOR_KEY)
        .setIDRange(1, Integer.MAX_VALUE - 1)
        .setType(c(IProcessor.class)).create();
    ModRegistries.PROCESSOR_REGISTRY = RegistryManager.ACTIVE.getRegistry(c(IProcessor.class));
  }
}

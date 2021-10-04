package com.aranaira.arcanearchives.core.event;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.api.crafting.processors.IProcessor;
import com.aranaira.arcanearchives.core.init.ModRegistries;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryManager;

@Mod.EventBusSubscriber(modid = ArcaneArchives.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryHandler {
  public static ResourceLocation PROCESSOR_KEY = new ResourceLocation(ArcaneArchives.MODID, "processor");

  @SubscribeEvent
  public static void onNewRegistry(RegistryEvent.NewRegistry event) {
    ArcaneArchives.LOG.info("Creating Processor registry");
    makeRegistry(PROCESSOR_KEY, IProcessor.class).create();
    ModRegistries.PROCESSOR_REGISTRY = RegistryManager.ACTIVE.getRegistry(IProcessor.class);
  }

  private static <T extends IForgeRegistryEntry<T>> RegistryBuilder<T> makeRegistry(ResourceLocation key, Class<T> type) {
    return new RegistryBuilder<T>()
        .setName(key)
        .setType(type)
        .disableOverrides()
        .disableSaving()
        .setMaxID(Integer.MAX_VALUE);
  }
}

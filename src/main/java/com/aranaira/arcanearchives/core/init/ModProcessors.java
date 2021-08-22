package com.aranaira.arcanearchives.core.init;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.api.crafting.processors.IProcessor;
import com.aranaira.arcanearchives.core.recipes.processors.CrystalWorkbenchContainerProcessor;
import com.aranaira.arcanearchives.core.recipes.processors.CrystalWorkbenchUUIDProcessor;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;
import java.util.List;


@Mod.EventBusSubscriber(modid = ArcaneArchives.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModProcessors {
  public static final IProcessor<?> CRYSTAL_WORKBENCH_CONTAINER_PROCESSOR = new CrystalWorkbenchContainerProcessor().setRegistryName(ArcaneArchives.MODID, "crystal_workbench_container_processor");
  public static final IProcessor<?> CRYSTAL_WORKBENCH_UUID_PROCESSOR = new CrystalWorkbenchUUIDProcessor().setRegistryName(ArcaneArchives.MODID, "crystal_workbench_uuid_processor");

  public static final List<IProcessor<?>> DEFAULT_PROCESSORS = Arrays.asList(CRYSTAL_WORKBENCH_CONTAINER_PROCESSOR, CRYSTAL_WORKBENCH_UUID_PROCESSOR);

  @SubscribeEvent
  public static void register(RegistryEvent.Register<IProcessor<?>> event) {
    event.getRegistry().registerAll(CRYSTAL_WORKBENCH_CONTAINER_PROCESSOR, CRYSTAL_WORKBENCH_UUID_PROCESSOR);
  }

  public static void load() {
  }
}

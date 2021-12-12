package com.aranaira.arcanearchives.core.init;

import com.aranaira.arcanearchives.core.recipes.processors.CrystalWorkbenchContainerProcessor;
import com.aranaira.arcanearchives.core.recipes.processors.CrystalWorkbenchUUIDProcessor;
import com.tterrag.registrate.util.entry.RegistryEntry;
import noobanidus.libs.noobutil.processor.IProcessor;

import static com.aranaira.arcanearchives.ArcaneArchives.REGISTRATE;

public class ModProcessors {
  public static final RegistryEntry<CrystalWorkbenchContainerProcessor> CRYSTAL_WORKBENCH_CONTAINER_PROCESSOR = REGISTRATE.simple("crystal_workbench_container_processor", IProcessor.class, CrystalWorkbenchContainerProcessor::new);

  public static final RegistryEntry<CrystalWorkbenchUUIDProcessor> CRYSTAL_WORKBENCH_UUID_PROCESSOR = REGISTRATE.simple("crystal_workbench_uuid_processor", IProcessor.class, CrystalWorkbenchUUIDProcessor::new);

  public static void load() {
  }
}

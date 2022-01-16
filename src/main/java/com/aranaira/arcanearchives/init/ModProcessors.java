package com.aranaira.arcanearchives.init;

import com.aranaira.arcanearchives.recipe.processors.CrystalWorkbenchContainerProcessor;
import com.aranaira.arcanearchives.recipe.processors.CrystalWorkbenchDomainProcessor;
import com.tterrag.registrate.util.entry.RegistryEntry;
import noobanidus.libs.noobutil.processor.IProcessor;

import static com.aranaira.arcanearchives.ArcaneArchives.REGISTRATE;

public class ModProcessors {
  public static final RegistryEntry<CrystalWorkbenchContainerProcessor> CRYSTAL_WORKBENCH_CONTAINER_PROCESSOR = REGISTRATE.simple("crystal_workbench_container_processor", IProcessor.class, CrystalWorkbenchContainerProcessor::new);

  public static final RegistryEntry<CrystalWorkbenchDomainProcessor> CRYSTAL_WORKBENCH_DOMAIN_PROCESSOR = REGISTRATE.simple("crystal_workbench_domain_processor", IProcessor.class, CrystalWorkbenchDomainProcessor::new);

  public static void load() {
  }
}

package com.aranaira.arcanearchives.core.init;

import com.aranaira.arcanearchives.api.reference.Identifiers;
import com.aranaira.arcanearchives.core.inventory.container.CrystalWorkbenchContainer;
import com.aranaira.arcanearchives.core.inventory.container.RadiantChestContainer;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.inventory.container.ContainerType;

import static com.aranaira.arcanearchives.ArcaneArchives.REGISTRATE;

public class ModContainers {
  public static final RegistryEntry<ContainerType<RadiantChestContainer>> RADIANT_CHEST = REGISTRATE.containerType("radiant_chest", RadiantChestContainer::new).register();

  public static final RegistryEntry<ContainerType<CrystalWorkbenchContainer>> CRYSTAL_WORKBENCH = REGISTRATE.containerType("crystal_workbench", CrystalWorkbenchContainer::new).register();

  public static void load () {
  }
}

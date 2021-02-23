package com.aranaira.arcanearchives.core.init;

import com.aranaira.arcanearchives.core.inventory.container.RadiantChestContainer;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.inventory.container.ContainerType;

import static com.aranaira.arcanearchives.ArcaneArchives.REGISTRATE;

public class ModContainers {
  public static final RegistryEntry<ContainerType<RadiantChestContainer>> RADIANT_CHEST = REGISTRATE.containerType("radiant_chest_container_type", RadiantChestContainer::new).register();

  public static void load () {
  }
}

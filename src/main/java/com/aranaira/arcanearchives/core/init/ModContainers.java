package com.aranaira.arcanearchives.core.init;

import com.aranaira.arcanearchives.client.screen.CrystalWorkbenchScreen;
import com.aranaira.arcanearchives.client.screen.RadiantChestScreen;
import com.aranaira.arcanearchives.core.inventory.container.CrystalWorkbenchContainer;
import com.aranaira.arcanearchives.core.inventory.container.RadiantChestContainer;
import com.tterrag.registrate.builders.ContainerBuilder;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.inventory.container.ContainerType;

import static com.aranaira.arcanearchives.ArcaneArchives.REGISTRATE;

public class ModContainers {
  public static final RegistryEntry<ContainerType<RadiantChestContainer>> RADIANT_CHEST = REGISTRATE.container("radiant_chest", (ContainerBuilder.ForgeContainerFactory<RadiantChestContainer>) RadiantChestContainer::new, () -> RadiantChestScreen::new).register();

  public static final RegistryEntry<ContainerType<CrystalWorkbenchContainer>> CRYSTAL_WORKBENCH = REGISTRATE.container("crystal_workbench", (ContainerBuilder.ForgeContainerFactory<CrystalWorkbenchContainer>) CrystalWorkbenchContainer::new, () -> CrystalWorkbenchScreen::new).register();

  public static void load() {
  }
}

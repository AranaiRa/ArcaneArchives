package com.aranaira.arcanearchives.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;

import javax.annotation.Nonnull;

public class ContainerFakeManifest extends Container {
  public ContainerFakeManifest(PlayerEntity playerIn) {
  }

  @Override
  public boolean canInteractWith(@Nonnull PlayerEntity playerIn) {
    return true;
  }
}

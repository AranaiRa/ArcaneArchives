package com.aranaira.arcanearchives.api.inventory.container;

import com.aranaira.arcanearchives.api.crafting.IPlayerContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;

import javax.annotation.Nullable;

public class CrystalWorkbenchContainer extends Container implements IPlayerContainer {
  protected CrystalWorkbenchContainer(@Nullable ContainerType<?> type, int id) {
    super(type, id);
  }

  @Override
  public PlayerEntity getPlayer() {
    return null;
  }

  @Override
  public boolean canInteractWith(PlayerEntity playerIn) {
    return false;
  }
}

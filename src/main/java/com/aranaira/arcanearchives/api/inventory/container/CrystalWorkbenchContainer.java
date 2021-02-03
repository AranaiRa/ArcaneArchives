package com.aranaira.arcanearchives.api.inventory.container;

import com.aranaira.arcanearchives.api.crafting.IPlayerContainer;
import com.aranaira.arcanearchives.tiles.CrystalWorkbenchTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;

import javax.annotation.Nullable;

public class CrystalWorkbenchContainer extends Container implements IPlayerContainer {
  private final PlayerInventory playerInventory;
  private final CrystalWorkbenchTile tile;

  protected CrystalWorkbenchContainer(@Nullable ContainerType<?> type, int id, PlayerInventory inventory, CrystalWorkbenchTile tile) {
    super(type, id);
    this.playerInventory = inventory;
    this.tile = tile;
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

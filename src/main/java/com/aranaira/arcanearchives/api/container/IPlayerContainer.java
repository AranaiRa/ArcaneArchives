package com.aranaira.arcanearchives.api.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Slot;

import java.util.Collections;
import java.util.List;

public interface IPlayerContainer {
  PlayerEntity getPlayer();

  // TODO
  default List<Slot> getPlayerSlots() {
    return Collections.emptyList();
  }
}

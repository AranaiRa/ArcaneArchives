package com.aranaira.arcanearchives.api.container;

import net.minecraft.inventory.container.Slot;

import java.util.List;

public interface IPartitionedPlayerContainer extends IPlayerContainer {
  List<Slot> getIngredientSlots();
}

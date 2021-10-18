package com.aranaira.arcanearchives.api.blockentities;

import com.aranaira.arcanearchives.api.inventory.IArcaneInventory;

import javax.annotation.Nullable;

public interface IInventoryBlockEntity<T extends IArcaneInventory> extends IArcaneArchivesBlockEntity {
  @Nullable
  T getBlockEntityInventory();

  T getEmptyInventory();
}

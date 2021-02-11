package com.aranaira.arcanearchives.api.data;

import com.aranaira.arcanearchives.api.inventory.IArcaneInventory;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

public class StoredInventory<I extends IArcaneInventory<I>> {
  protected final Supplier<UUID> uuidSupplier;
  protected final Function<Integer, I> builder;
  protected final int size;
  protected I inventory = null;

  public StoredInventory(Supplier<UUID> uuidSupplier, Function<Integer, I> builder, int size) {
    this.builder = builder;
    this.uuidSupplier = uuidSupplier;
    this.size = size;
  }

  @Nullable
  public I getInventory(World world) {
    if (inventory != null) {
      return inventory;
    }

    if (world == null || world.isRemote) {
      return null;
    }

    UUID id = uuidSupplier.get();
    if (id == null) {
      return null;
    }

    ArcaneInventoryData<I> inventoryData = DataStorage.getInventory(id, size, builder);
    this.inventory = inventoryData.getInventory();

    return this.inventory;
  }
}

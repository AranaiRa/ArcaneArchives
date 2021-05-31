package com.aranaira.arcanearchives.api.data;

import com.aranaira.arcanearchives.api.inventory.AbstractArcaneItemHandler;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

public class StoredInventory<I extends AbstractArcaneItemHandler> {
  protected final Supplier<UUID> uuidSupplier;
  protected final Function<Integer, I> builder;
  protected final Supplier<I> emptyBuilder;
  protected final int size;
  protected I inventory = null;
  protected I empty = null;

  public StoredInventory(Supplier<UUID> uuidSupplier, Function<Integer, I> builder, Supplier<I> empty, int size) {
    this.builder = builder;
    this.emptyBuilder = empty;
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
    this.inventory.setInventoryData(inventoryData);
    this.inventory.setChangeCallback((o) -> inventoryData.markDirty());

    return this.inventory;
  }

  public I getEmpty () {
    if (this.empty == null) {
      this.empty = this.emptyBuilder.get();
    }
    if (this.empty == null) {
      System.out.println("ARGH IT'S NULL");
    }
    return this.empty;
  }
}

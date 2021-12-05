package com.aranaira.arcanearchives.api.data;

import com.aranaira.arcanearchives.api.inventory.AbstractArcaneItemHandler;
import com.aranaira.arcanearchives.api.inventory.IArcaneInventory;
import com.aranaira.arcanearchives.api.inventory.IInventoryListener;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;

public class StoredInventory<I extends AbstractArcaneItemHandler> {
  protected final Supplier<UUID> uuidSupplier;
  protected final Function<Integer, I> builder;
  protected final IntFunction<I> emptyBuilder;
  protected final int size;
  protected I inventory = null;
  protected I empty = null;

  public StoredInventory(Supplier<UUID> uuidSupplier, Function<Integer, I> builder, IntFunction<I> empty, int size) {
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

    if (world == null || world.isClientSide) {
      return null;
    }

    UUID id = uuidSupplier.get();
    if (id == null) {
      return null;
    }

    ArcaneInventoryData<I> inventoryData = DataStorage.getInventory(id, size, builder);
    this.inventory = inventoryData.getInventory();
    this.inventory.setInventoryData(inventoryData);
    this.inventory.addListener((inventory, slot) -> inventoryData.setDirty());

    return this.inventory;
  }

  public I getEmpty () {
    if (this.empty == null) {
      this.empty = this.emptyBuilder.apply(this.size);
    }
    if (this.empty == null) {
      throw new IllegalStateException("empty inventory cannot be null at this point");
    }
    return this.empty;
  }
}

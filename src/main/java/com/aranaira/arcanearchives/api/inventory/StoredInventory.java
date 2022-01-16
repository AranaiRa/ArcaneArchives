package com.aranaira.arcanearchives.api.inventory;

import com.aranaira.arcanearchives.api.data.DataStorage;
import net.minecraft.world.World;
import noobanidus.libs.noobutil.data.server.StoredInventoryData;
import noobanidus.libs.noobutil.recipe.AbstractLargeItemHandler;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.function.IntFunction;
import java.util.function.Supplier;

public class StoredInventory<I extends AbstractLargeItemHandler> {
  protected final Supplier<UUID> uuidSupplier;
  protected final IntFunction<I> builder;
  protected final IntFunction<I> emptyBuilder;
  protected final int size;
  protected I inventory = null;
  protected I empty = null;

  public StoredInventory(Supplier<UUID> uuidSupplier, IntFunction<I> builder, IntFunction<I> empty, int size) {
    this.builder = builder;
    this.emptyBuilder = empty;
    this.uuidSupplier = uuidSupplier;
    this.size = size;
  }

  @Nullable
  public I getInventory(World level) {
    if (inventory != null) {
      return inventory;
    }

    if (level == null || level.isClientSide()) {
      return null;
    }

    UUID id = uuidSupplier.get();
    if (id == null) {
      return null;
    }

    StoredInventoryData<I> inventoryData = DataStorage.getInventory(id, size, builder);
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

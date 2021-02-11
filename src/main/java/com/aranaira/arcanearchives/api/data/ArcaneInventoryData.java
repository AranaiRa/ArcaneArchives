package com.aranaira.arcanearchives.api.data;

import com.aranaira.arcanearchives.api.inventory.IArcaneInventory;
import com.aranaira.arcanearchives.api.tiles.IArcaneArchivesTile;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.storage.WorldSavedData;

import java.util.UUID;
import java.util.function.Function;

public class ArcaneInventoryData<T extends IArcaneInventory<T>> extends WorldSavedData {
  private UUID id;
  private T inventory;
  private int size;

  public static String ID(UUID id) {
    return "ArcaneArchives-Inventory-" + id.toString();
  }

  public ArcaneInventoryData(UUID id, int size, Function<Integer, T> builder) {
    super(ID(id));
    this.id = id;
    this.size = size;
    this.inventory = builder.apply(this.size);
  }

/*  public ArcaneInventoryData(UUID id, Supplier<T> builder) {
    this(id, builder.get());
  }*/

  public T getInventory() {
    return inventory;
  }

  public UUID getId() {
    return id;
  }

  @Override
  public void read(CompoundNBT nbt) {
    this.id = nbt.getUniqueId("id");
    this.size = nbt.getInt("size");
    this.inventory.deserialize(nbt.getCompound("inventory"));
  }

  @Override
  public CompoundNBT write(CompoundNBT compound) {
    compound.putInt("size", this.size);
    compound.putUniqueId("id", this.id);
    // TODO: Handle null
    if (this.inventory != null) {
      compound.put("inventory", this.inventory.serialize());
    }
    return compound;
  }
}

package com.aranaira.arcanearchives.api.data;

import com.aranaira.arcanearchives.api.inventory.IArcaneInventory;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.storage.WorldSavedData;

import java.io.File;
import java.util.UUID;
import java.util.function.IntFunction;

public class ArcaneInventoryData<T extends IArcaneInventory> extends WorldSavedData {
  private UUID id;
  private final T inventory;
  private int size;

  public static String ID(UUID id) {
    return "ArcaneArchives-Inventory-" + id.toString();
  }

  public ArcaneInventoryData(UUID id, int size, IntFunction<T> builder) {
    super(ID(id));
    this.id = id;
    this.size = size;
    this.inventory = builder.apply(this.size);
  }

  public T getInventory() {
    return inventory;
  }

  public UUID getUUID() {
    return id;
  }

  @Override
  public void load(CompoundNBT nbt) {
    this.id = nbt.getUUID("id");
    this.size = nbt.getInt("size");
    this.inventory.deserialize(nbt.getCompound("inventory"));
  }

  @Override
  public CompoundNBT save(CompoundNBT compound) {
    compound.putInt("size", this.size);
    compound.putUUID("id", this.id);
    // TODO: Handle null
    if (this.inventory != null) {
      compound.put("inventory", this.inventory.serialize());
    }
    return compound;
  }

  @Override
  public void save(File fileIn) {
    super.save(fileIn);
  }

  @Override
  public void setDirty() {
    super.setDirty();
  }
}

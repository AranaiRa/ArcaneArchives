package com.aranaira.arcanearchives.api.data;

import com.aranaira.arcanearchives.api.inventory.IArcaneInventory;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.storage.WorldSavedData;

import java.util.UUID;
import java.util.function.Supplier;

public class ArcaneInventoryData<T extends IArcaneInventory> extends WorldSavedData {
  private UUID id;
  private T inventory;

  public static String ID(UUID id) {
    return "ArcaneArchives-Inventory-" + id.toString();
  }

  public ArcaneInventoryData(UUID id, T inventory) {
    super(ID(id));
    this.id = id;
    this.inventory = inventory;
  }

  public ArcaneInventoryData(UUID id, Supplier<T> builder) {
    this(id, builder.get());
  }

  public IArcaneInventory getInventory() {
    return inventory;
  }

  public UUID getId() {
    return id;
  }

  @Override
  public void read(CompoundNBT nbt) {
    this.id = nbt.getUniqueId("id");
    this.inventory.deserialize(nbt.getCompound("inventory"));
  }

  @Override
  public CompoundNBT write(CompoundNBT compound) {
    compound.putUniqueId("id", this.id);
    compound.put("inventory", this.inventory.serialize());
    return compound;
  }
}

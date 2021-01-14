package com.aranaira.arcanearchives.api.data;

import com.aranaira.arcanearchives.api.inventory.ArcaneItemHandler;
import com.aranaira.arcanearchives.api.inventory.IArcaneInventory;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.storage.WorldSavedData;

import java.util.UUID;

public class ArcaneInventoryData extends WorldSavedData {
  private UUID id;
  private IArcaneInventory inventory;

  public static String ID(UUID id) {
    return "ArcaneArchives-Inventory-" + id.toString();
  }

  // TODO: GENERICS????????????
  public ArcaneInventoryData(UUID id, IArcaneInventory inventory) {
    super(ID(id));
    this.id = id;
    this.inventory = inventory;
  }

  public ArcaneInventoryData(UUID id) {
    this(id, new ArcaneItemHandler());
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
    this.inventory = inventory.getBuilder().build(nbt.getCompound("inventory"));
  }

  @Override
  public CompoundNBT write(CompoundNBT compound) {
    compound.putUniqueId("id", this.id);
    compound.put("inventory", this.inventory.serialize());
    return compound;
  }
}

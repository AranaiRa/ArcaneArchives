package com.aranaira.arcanearchives.api.inventory;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.items.IItemHandlerModifiable;

public interface IArcaneInventory extends IItemHandlerModifiable {
  default int getSlots() {
    return this.size();
  }

  int size();

  long getCountInSlot(int slot);

  void enlarge(int size);

  CompoundNBT serialize();

  void deserialize(CompoundNBT result);
}

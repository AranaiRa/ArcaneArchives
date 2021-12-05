package com.aranaira.arcanearchives.api.inventory;

import com.aranaira.arcanearchives.api.data.ArcaneInventoryData;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nullable;

public interface IArcaneInventory extends IItemHandlerModifiable {
  default int getSlots() {
    return this.size();
  }

  int size();

  long getCountInSlot(int slot);

  void enlarge(int size);

  CompoundNBT serialize();

  void deserialize(CompoundNBT result);

  default void setInventoryData(ArcaneInventoryData<?> data) {
  }

  @Nullable
  default ArcaneInventoryData<?> getInventoryData() {
    return null;
  }

  default void markDirty() {
    ArcaneInventoryData<?> data = getInventoryData();
    if (data != null) {
      data.setDirty();
    }
  }

  void addListener (IInventoryListener listener);
}

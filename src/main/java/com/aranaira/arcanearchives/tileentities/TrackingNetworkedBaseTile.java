package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.inventories.ITrackingHandler;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;

public abstract class TrackingNetworkedBaseTile<T extends IItemHandler & INBTSerializable<NBTTagCompound> & ITrackingHandler> extends InventoryNetworkedBaseTile<T> {
  public Int2IntOpenHashMap getItemReference() {
    return inventory.getItemReference();
  }

  public void invalidateItemTracking() {
    inventory.invalidate();
  }

  public void manuallyRecountTracking() {
    inventory.manualRecount();
  }

}

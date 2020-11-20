/*package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.inventories.ITrackingHandler;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;

public abstract class TrackingNetworkedBaseTile<T extends IItemHandler & INBTSerializable<CompoundNBT> & ITrackingHandler> extends InventoryNetworkedBaseTile<T> {
  public Int2IntOpenHashMap getItemReference() {
    return inventory.getItemReference();
  }

  public void invalidateItemTracking() {
    inventory.invalidate();
  }

  public void manuallyRecountTracking() {
    inventory.manualRecount();
  }

  public abstract boolean isSingleItemInventory ();

  public ItemStack getSingleItemReference () {
    throw new IllegalStateException("This class does not define `getSingleItemReference`.");
  }

  public abstract String getDescriptor ();
}*/

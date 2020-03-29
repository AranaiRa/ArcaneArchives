package com.aranaira.arcanearchives.tiles;

import com.aranaira.arcanearchives.inventories.TrackingExtendedHandler;
import com.aranaira.arcanearchives.tilenetwork.Network;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public class RadiantChestTile extends NetworkedBaseTile {
  private final TrackingExtendedHandler inventory;

  private EnumFacing displayFacing = null;
  private ItemStack displayItem = ItemStack.EMPTY;
  private String customName = null;

  public RadiantChestTile() {
    this.inventory = new TrackingExtendedHandler(this, 54);
  }

  public EnumFacing getDisplayFacing() {
    return displayFacing;
  }

  public void setDisplayFacing(EnumFacing displayFacing) {
    this.displayFacing = displayFacing;
  }

  public ItemStack getDisplayItem() {
    return displayItem;
  }

  public void setDisplayItem(ItemStack displayItem) {
    this.displayItem = displayItem;
  }

  public String getCustomName() {
    return customName;
  }

  public void setCustomName(String customName) {
    this.customName = customName;
  }

  @Override
  public void onNetworkJoined(Network network) {
    // TODO: Handle brazier setup according to network settings
  }

  public TrackingExtendedHandler getInventory() {
    return inventory;
  }
}

package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.inventories.TrackingExtendedHandler;
import com.aranaira.arcanearchives.reference.Tags;
import com.aranaira.arcanearchives.tilenetwork.Network;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public class RadiantChestTile extends TrackingNetworkedBaseTile<TrackingExtendedHandler> {
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

  @Override
  public TrackingExtendedHandler getInventory() {
    return inventory;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    displayItem.deserializeNBT(compound.getCompoundTag(Tags.RadiantChest.displayItem));
    int index = compound.getInteger(Tags.RadiantChest.displayFacing);
    displayFacing = index == -1 ? null : EnumFacing.byIndex(index);
    final String temp = compound.getString(Tags.RadiantChest.customName);
    customName = temp.isEmpty() ? null : temp;
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    NBTTagCompound tag = super.writeToNBT(compound);
    tag.setTag(Tags.RadiantChest.displayItem, displayItem.serializeNBT());
    tag.setInteger(Tags.RadiantChest.displayFacing, displayFacing == null ? -1 : displayFacing.ordinal());
    tag.setString(Tags.RadiantChest.customName, customName == null ? "" : customName);
    return tag;
  }

  @Override
  public NBTTagCompound getUpdateTag() {
    // TODO: Should we do this?
    NBTTagCompound update = super.getUpdateTag();
    update.removeTag(Tags.inventory);
    return update;
  }

  @Override
  public boolean isSingleItemInventory() {
    return false;
  }

  @Override
  public String getDescriptor() {
    return "arcanearchives.tiles.tracking_descriptors.chest";
  }
}

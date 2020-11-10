package com.aranaira.arcanearchives.inventories;

/*import com.aranaira.arcanearchives.manifest.ManifestEntry;
import com.aranaira.arcanearchives.types.ManifestList;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nullable;


public class ManifestItemHandler implements IItemHandlerModifiable {
  private ManifestList manifestBase;
  private ManifestList manifestActive = null;
  private int numSlots;

  public ManifestItemHandler(ManifestList manifest) {
    this.manifestBase = manifest;
    this.numSlots = 81;
  }

  private void updateManifest() {
    if (manifestActive == null) {
      manifestActive = manifestBase.sortAndFilterMaybe();
    }
  }

  @Override
  public int getSlots() {
    return numSlots;
  }

  public void setSlots(int numSlots) {
    this.numSlots = numSlots;
  }

  @Override
  public ItemStack getStackInSlot(int slot) {
    updateManifest();
    return manifestActive.getItemStackForSlot(slot);
  }

  @Override
  public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
    return stack;
  }

  @Override
  public ItemStack extractItem(int slot, int amount, boolean simulate) {
    return ItemStack.EMPTY;
  }

  @Override
  public int getSlotLimit(int slot) {
    return 0;
  }

  @Nullable
  public ManifestEntry getManifestEntryInSlot(int slot) {
    updateManifest();
    return manifestActive.getEntryForSlot(slot);
  }

  @Override
  public void setStackInSlot(int slot, ItemStack stack) {
  }

  public String getSearchText() {
    return manifestBase.getSearchText();
  }

  public ItemStack getSearchItem() {
    return manifestBase.getSearchItem();
  }

  public void setSearchText(String s) {
    manifestBase.setSearchText(s);
    manifestActive = manifestBase.sortAndFilterMaybe();
  }

  public void setSearchItem(ItemStack s) {
    manifestBase.setSearchItem(s);
    manifestActive = manifestBase.sortAndFilterMaybe();
  }

  public void clear() {
    manifestBase.setSearchText(null);
    manifestBase.setSearchItem(ItemStack.EMPTY);
    manifestActive = manifestBase.sortAndFilterMaybe();
  }

  public void nullify() {
    manifestActive = null;
  }

  public ManifestList.SortingDirection getSortingDirection() {
    if (manifestActive != null) {
      return manifestActive.getSortingDirection();
    }
    return manifestBase.getSortingDirection();
  }

  public void setSortingDirection(ManifestList.SortingDirection sortingDirection) {
    manifestBase.setSortingDirection(sortingDirection);
    manifestActive = manifestBase.sortAndFilterMaybe();
  }

  public ManifestList.SortingType getSortingType() {
    if (manifestActive != null) {
      return manifestActive.getSortingType();
    }
    return manifestBase.getSortingType();
  }

  public void setSortingType(ManifestList.SortingType sortingType) {
    manifestBase.setSortingType(sortingType);
    manifestActive = manifestBase.sortAndFilterMaybe();
  }
}*/

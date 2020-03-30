/*package com.aranaira.arcanearchives.inventory.handlers;

import com.aranaira.arcanearchives.tileentities.RadiantTroveTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantTroveTileEntity.TroveItemHandler.Tags;
import com.aranaira.arcanearchives.types.UpgradeType;
import com.aranaira.arcanearchives.util.ItemUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TroveItemBlockItemHandler implements ITroveItemHandler, ICapabilityProvider {
  private TroveUpgradeItemHandler upgrades = null;
  private OptionalUpgradesHandler optionals = null;
  private ItemStack container;

  private static int BASE_COUNT = RadiantTroveTileEntity.BASE_COUNT;
  private int count = -1;
  private ItemStack reference = null;

  public TroveItemBlockItemHandler(ItemStack container) {
    this.container = container;
  }

  public int getUpgrades() {
    if (this.upgrades == null) {
      NBTTagCompound tag = container.getTagCompound();
      if (tag == null || !tag.hasKey(RadiantTroveTileEntity.Tags.SIZE_UPGRADES)) {
        return 0;
      }

      this.upgrades = new TroveUpgradeItemHandler();
      this.upgrades.deserializeNBT(tag.getCompoundTag(RadiantTroveTileEntity.Tags.SIZE_UPGRADES));
    }
    return upgrades.getUpgradesCount();
  }

  @Override
  public int getSlots() {
    return 2;
  }

  @Override
  public int getSlotLimit(int slot) {
    return (getUpgrades() + 1) * BASE_COUNT;
  }

  @Override
  public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
    return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
  }

  @Nullable
  @Override
  public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
    if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this);
    } else {
      return null;
    }
  }

  @Override
  public ItemStack getReference() {
    if (reference == null) {
      NBTTagCompound tag = container.getTagCompound();
      if (tag == null || !tag.hasKey(RadiantTroveTileEntity.Tags.HANDLER_ITEM)) {
        return ItemStack.EMPTY;
      }

      NBTTagCompound incoming = tag.getCompoundTag(RadiantTroveTileEntity.Tags.HANDLER_ITEM);
      this.reference = new ItemStack(incoming.getCompoundTag(Tags.REFERENCE));
    }
    return this.reference;
  }

  @Override
  public int getCount() {
    if (count == -1) {
      NBTTagCompound tag = container.getTagCompound();
      if (tag == null || !tag.hasKey(RadiantTroveTileEntity.Tags.HANDLER_ITEM)) {
        return 0;
      }

      NBTTagCompound incoming = tag.getCompoundTag(RadiantTroveTileEntity.Tags.HANDLER_ITEM);
      this.count = incoming.getInteger(Tags.COUNT);
    }
    return count;
  }

  @Override
  public int getMaxCount() {
    return (getUpgrades() + 1) * BASE_COUNT;
  }

  @Override
  public void setCount(int count) {
    this.count = count;
    saveToStack();
  }

  private OptionalUpgradesHandler getOptionals() {
    if (optionals == null) {
      NBTTagCompound tag = container.getTagCompound();
      if (tag == null || !tag.hasKey(RadiantTroveTileEntity.Tags.OPTIONAL_UPGRADES)) {
        return null;
      }
      this.optionals = new OptionalUpgradesHandler();
      this.optionals.deserializeNBT(tag.getCompoundTag(RadiantTroveTileEntity.Tags.OPTIONAL_UPGRADES));
    }
    return this.optionals;
  }

  @Override
  public boolean isVoiding() {
    OptionalUpgradesHandler handler = getOptionals();
    if (handler == null) {
      return false;
    }
    return handler.hasUpgrade(UpgradeType.VOID);
  }

  @Override
  public boolean isLocked() {
    OptionalUpgradesHandler handler = getOptionals();
    if (handler == null) {
      return false;
    }
    return handler.hasUpgrade(UpgradeType.LOCK);
  }

  @Override
  public void update() {
    saveToStack();
  }

  @Override
  public ItemStack getItem() {
    return getReference();
  }

  @Override
  public ItemStack getItemCurrent() {
    if (getCount() == 0) {
      return ItemStack.EMPTY;
    }

    return getReference();
  }

  @Override
  public void setReference(ItemStack reference) {
    this.reference = reference;
    saveToStack();
  }

  @Override
  public boolean isEmpty() {
    return getCount() == 0 && getReference().isEmpty();

  }

  public void saveToStack() {
    NBTTagCompound tag = ItemUtils.getOrCreateTagCompound(container);
    NBTTagCompound result = new NBTTagCompound();
    if (this.count != -1) {
      result.setInteger(Tags.COUNT, this.count);
    }
    if (this.reference != null) {
      result.setTag(Tags.REFERENCE, this.reference.serializeNBT());
    }
    if (this.upgrades != null) {
      result.setInteger(Tags.UPGRADES, getUpgrades());
    }
    tag.setTag(RadiantTroveTileEntity.Tags.HANDLER_ITEM, result);
  }
}*/

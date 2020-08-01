package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.reference.Tags;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public abstract class InventoryNetworkedBaseTile<I extends IItemHandler & INBTSerializable<NBTTagCompound>> extends NetworkedBaseTile {
  protected I inventory;
  protected String inventoryName;

  public I getInventory() {
    return inventory;
  }

  public String getInventoryName () {
    return inventoryName;
  }

  public void setInventoryName (String name) {
    this.inventoryName = name;
  }

  @Override
  public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
    if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return true;
    }

    return super.hasCapability(capability, facing);
  }

  @SuppressWarnings("unchecked")
  @Nullable
  @Override
  public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
    if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return capability.cast((T) getInventory());
    }

    return super.getCapability(capability, facing);
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    inventory.deserializeNBT(compound.getCompoundTag(Tags.inventory));
    if (compound.hasKey(Tags.inventoryName, Constants.NBT.TAG_STRING)) {
      inventoryName = compound.getString(Tags.inventoryName);
      if (inventoryName.isEmpty()) {
        inventoryName = null;
      }
    } else {
      inventoryName = null;
    }
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    NBTTagCompound tag = super.writeToNBT(compound);
    tag.setTag(Tags.inventory, inventory.serializeNBT());
    if (inventoryName == null) {
      tag.setString(Tags.inventoryName, "");
    } else {
      tag.setString(Tags.inventoryName, inventoryName);
    }
    return tag;
  }
}
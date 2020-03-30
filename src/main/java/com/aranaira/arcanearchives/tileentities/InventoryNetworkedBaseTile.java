package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.reference.Tags;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public abstract class InventoryNetworkedBaseTile<I extends IItemHandler & INBTSerializable<NBTTagCompound>> extends NetworkedBaseTile {
  protected I inventory;

  public I getInventory() {
    return inventory;
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
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    NBTTagCompound tag = super.writeToNBT(compound);
    tag.setTag(Tags.inventory, inventory.serializeNBT());
    return tag;
  }
}

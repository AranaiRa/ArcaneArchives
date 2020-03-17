package com.aranaira.arcanearchives.tiles;

import net.minecraft.nbt.NBTTagCompound;

import java.util.UUID;

public abstract class NetworkedBaseTile extends BaseTile {
  private UUID networkId;
  private String cachedNetworkId = null; // TODO: Transition this into the properly generated ID.

  @Override
  protected UUID generateId() {
    return null;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    return super.writeToNBT(compound);
  }
}

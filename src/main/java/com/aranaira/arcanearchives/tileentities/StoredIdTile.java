package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.reference.Tags;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import java.util.UUID;

@Deprecated
@SuppressWarnings("NullableProblems")
public class StoredIdTile extends TileEntity {
  private UUID networkId;

  public StoredIdTile() {
  }

  public UUID getNetworkId() {
    return networkId;
  }

  public void setNetworkId(UUID networkId) {
    this.networkId = networkId;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    if (compound.hasUniqueId(Tags.networkId)) {
      this.networkId = compound.getUniqueId(Tags.networkId);
    }
    super.readFromNBT(compound);
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    if (networkId != null) {
      compound.setUniqueId(Tags.networkId, networkId);
    }
    return super.writeToNBT(compound);
  }
}

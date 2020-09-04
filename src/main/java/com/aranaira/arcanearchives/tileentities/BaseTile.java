package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.reference.Tags;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public abstract class BaseTile extends TileEntity {
  protected UUID tileId = null;
  protected String cachedUUID = null;

  public UUID getTileId() {
    if (tileId == null && world != null && !world.isRemote) {
      generateTileId();
    }
    return tileId;
  }

  public String getCachedUUID() {
    if (tileId == null) {
      return "";
    }
    if (cachedUUID == null) {
      cachedUUID = tileId.toString();
    }
    return cachedUUID;
  }

  private void generateTileId() {
    if (world.isRemote) {
      ArcaneArchives.logger.error("Tried to generate tileId for " + this + " on client side.");
      return;
    }
    if (tileId == null) {
      tileId = generateId();
      markDirty();
      stateUpdate();
    }
  }

  protected UUID generateId() {
    return UUID.randomUUID();
  }

  public void forceTileId(UUID tileId) {
    if (world.isRemote) {
      ArcaneArchives.logger.error("Tried to set tileId for " + this + " on client side.");
      return;
    }
    if (tileId != null) {
      ArcaneArchives.logger.debug("BaseTile with ID " + getCachedUUID() + " changed to " + tileId.toString());
    }
    this.tileId = tileId;
    stateUpdate();
  }

  public void stateUpdate() {
    if (world == null || world.isRemote) {
      return;
    }

    IBlockState state = world.getBlockState(getPos());
    world.notifyBlockUpdate(getPos(), state, state, 8);
  }

  public int getDimension() {
    if (world == null) {
      return -999;
    }

    return world.provider.getDimension();
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    if (!compound.hasUniqueId(Tags.tileId)) {
      if (world != null && !world.isRemote) {
        generateTileId();
      }
    } else {
      tileId = compound.getUniqueId(Tags.tileId);
    }
    super.readFromNBT(compound);
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    if (tileId == null) {
      generateTileId();
    }
    if (tileId != null) {
      compound.setUniqueId(Tags.tileId, tileId);
    }
    return super.writeToNBT(compound);
  }

  @Override
  public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
    return (oldState.getBlock() != newSate.getBlock());
  }
}

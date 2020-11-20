/*package com.aranaira.arcanearchives.tilenetwork;

import com.aranaira.arcanearchives.tileentities.NetworkedBaseTile;
import com.aranaira.arcanearchives.util.WorldUtil;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

// TODO: Include Network ID?
public class NetworkEntry {
  public final Class<?> clazz;
  public final UUID uuid;
  public BlockPos position;
  public int dimension;

  public NetworkEntry(Class<?> clazz, UUID uuid, BlockPos position, int dimension) {
    this.clazz = clazz;
    this.uuid = uuid;
    this.position = position;
    this.dimension = dimension;
  }

  public <T extends NetworkedBaseTile> NetworkEntry(T tile) {
    this.clazz = tile.getClass();
    this.uuid = tile.getTileId();
    this.position = tile.getPos();
    this.dimension = tile.getWorld().provider.getDimension();
  }

  @SuppressWarnings("unchecked")
  public <T extends NetworkedBaseTile> T getTile() {
    return (T) WorldUtil.getTileEntity(this.clazz, this.dimension, this.position);
  }
}*/

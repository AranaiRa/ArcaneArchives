package com.aranaira.arcanearchives.manifest;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;

import javax.annotation.Nullable;

public class ManifestDescriptor extends IndexDescriptor {
  private final DimensionType dimension;
  private final BlockPos position;
  private final double distance;

  public static ManifestDescriptor fromEntry(BlockPos player, IndexEntry entry) {
    IndexDescriptor parent = entry.getDescriptor();
    BlockPos position = entry.getPosition();
    int dimension = entry.getDimension();
    double distance = player.distanceSq(position.getX(), position.getY(), position.getZ());
    return new ManifestDescriptor(DimensionType.getById(dimension), position, distance, parent.getType(), parent.getDescription());
  }

  protected ManifestDescriptor(DimensionType dimension, BlockPos position, double distance, IndexType type, @Nullable String description) {
    super(type, description);
    this.dimension = dimension;
    this.position = position;
    this.distance = distance;
  }

  public DimensionType getDimension() {
    return dimension;
  }

  public BlockPos getPosition() {
    return position;
  }

  public double getDistance() {
    return distance;
  }
}

package com.aranaira.arcanearchives.manifest;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ManifestDescriptor extends IndexDescriptor {
  private final RegistryKey<World> key;
  private final BlockPos position;
  private final double distance;

  public static ManifestDescriptor fromEntry(RegistryKey<World> key, BlockPos player, IndexEntry entry) {
    IndexDescriptor parent = entry.getDescriptor();
    BlockPos position = entry.getPosition();
    double distance = player.distanceSq(position); //.getX(), position.getY(), position.getZ());
    return new ManifestDescriptor(key, position, distance, parent.getType(), parent.getDescription());
  }

  protected ManifestDescriptor(RegistryKey<World> key, BlockPos position, double distance, IndexType type, @Nullable String description) {
    super(type, description);
    this.key = key;
    this.position = position;
    this.distance = distance;
  }

  public RegistryKey<World> getDimension() {
    return key;
  }

  public BlockPos getPosition() {
    return position;
  }

  public double getDistance() {
    return distance;
  }
}

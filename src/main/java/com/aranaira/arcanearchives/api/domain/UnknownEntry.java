package com.aranaira.arcanearchives.api.domain;

import com.aranaira.arcanearchives.api.block.entity.IDomainBlockEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;

public class UnknownEntry {
  private final BlockPos position;
  private final RegistryKey<World> dimension;

  public UnknownEntry(BlockPos position, RegistryKey<World> dimension) {
    this.position = position;
    this.dimension = dimension;
  }

  public BlockPos getPosition() {
    return position;
  }

  public RegistryKey<World> getDimension() {
    return dimension;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    UnknownEntry that = (UnknownEntry) o;

    if (position != null ? !position.equals(that.position) : that.position != null) return false;
    return dimension != null ? dimension.equals(that.dimension) : that.dimension == null;
  }

  @Override
  public int hashCode() {
    int result = position != null ? position.hashCode() : 0;
    result = 31 * result + (dimension != null ? dimension.hashCode() : 0);
    return result;
  }

  @Nullable
  public IDomainBlockEntity resolve (MinecraftServer server) {
    ServerWorld level = server.getLevel(dimension);
    if (level == null) {
      return null;
    }
    if (!level.isAreaLoaded(position, 1)) {
      return null;
    }
    TileEntity be = level.getBlockEntity(position);
    if (be instanceof IDomainBlockEntity) {
      return (IDomainBlockEntity) be;
    }

    return null;
  }
}

package com.aranaira.arcanearchives.api.domain;

import com.aranaira.arcanearchives.api.block.entity.IDomainBlockEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.UUID;

public class DomainEntry<T extends IDomainBlockEntity> {
  private final BlockPos position;
  private final RegistryKey<World> dimension;
  private final UUID networkId;
  private UUID entityId;
  private final Class<T> clazz;
  private int priority;

  public DomainEntry(BlockPos position, RegistryKey<World> dimension, UUID networkId, UUID entityId, Class<T> clazz) {
    this.position = position;
    this.dimension = dimension;
    this.networkId = networkId;
    this.clazz = clazz;
    this.entityId = entityId;
  }

  public BlockPos getPosition() {
    return position;
  }

  public RegistryKey<World> getDimension() {
    return dimension;
  }

  public UUID getNetworkId() {
    return networkId;
  }

  public UUID getEntityId() {
    return entityId;
  }

  public Class<T> getClazz() {
    return clazz;
  }

  public int getPriority() {
    return priority;
  }

  @Nullable
  public T getEntity (MinecraftServer server, Class<T> clazz, boolean forceLoad) {
    ServerWorld level = server.getLevel(dimension);
    if (level == null) {
      return null;
    }

    if (!forceLoad && !level.isAreaLoaded(position, 1)) {
      return null;
    }

    TileEntity te = level.getBlockEntity(position);
    if (te == null) {
      return null;
    }

    if (clazz.isAssignableFrom(te.getClass())) {
      //noinspection unchecked
      return (T) te;
    }

    return null;
  }

  @Nullable
  public T getEntity (MinecraftServer server) {
    return getEntity(server, this.clazz, false);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    DomainEntry<?> that = (DomainEntry<?>) o;

    if (priority != that.priority) return false;
    if (position != null ? !position.equals(that.position) : that.position != null) return false;
    if (dimension != null ? !dimension.equals(that.dimension) : that.dimension != null) return false;
    if (networkId != null ? !networkId.equals(that.networkId) : that.networkId != null) return false;
    if (entityId != null ? !entityId.equals(that.entityId) : that.entityId != null) return false;
    return clazz != null ? clazz.equals(that.clazz) : that.clazz == null;
  }

  @Override
  public int hashCode() {
    int result = position != null ? position.hashCode() : 0;
    result = 31 * result + (dimension != null ? dimension.hashCode() : 0);
    result = 31 * result + (networkId != null ? networkId.hashCode() : 0);
    result = 31 * result + (entityId != null ? entityId.hashCode() : 0);
    result = 31 * result + (clazz != null ? clazz.hashCode() : 0);
    result = 31 * result + priority;
    return result;
  }
}

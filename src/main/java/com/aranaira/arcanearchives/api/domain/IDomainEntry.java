package com.aranaira.arcanearchives.api.domain;

import com.aranaira.arcanearchives.api.block.entity.IDomainEntityType;
import com.aranaira.arcanearchives.api.data.UUIDNameData;
import com.aranaira.arcanearchives.api.inventory.RemoteInventory;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import noobanidus.libs.noobutil.tracking.ItemTracking;

import javax.annotation.Nullable;
import java.util.UUID;

public interface IDomainEntry {
  IDomainEntityType getType();
  int getPriority();
  void setPriority (int priority);
  long getLastUpdated ();

  UUID getDomainId();
  UUID getEntityId();
  UUIDNameData.Name getEntityName();

  BlockPos getPosition();
  RegistryKey<World> getDimension();

  default boolean hasInventory () {
    return getType().hasInventory();
  }
  boolean hasTracking();
  ItemTracking getTracking();

  @Nullable
  RemoteInventory getInventory();

  IDomainEntityType getTypeByOrdinal (int ordinal);

  IDomainEntry getEntry();

  UpdateResult updateFrom (IDomainEntry other);
}

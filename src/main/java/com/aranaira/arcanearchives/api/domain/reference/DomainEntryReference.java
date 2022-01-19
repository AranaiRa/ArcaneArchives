package com.aranaira.arcanearchives.api.domain.reference;

import com.aranaira.arcanearchives.api.block.entity.IDomainEntityType;
import com.aranaira.arcanearchives.api.data.DataStorage;
import com.aranaira.arcanearchives.api.data.UUIDNameData;
import com.aranaira.arcanearchives.api.domain.IDomainEntry;
import com.aranaira.arcanearchives.api.domain.impl.DomainEntryImpl;
import com.aranaira.arcanearchives.api.inventory.RemoteInventory;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.UUID;

public class DomainEntryReference implements IDomainEntry {
  private final IDomainEntityType type;
  private final UUID domainId;
  private final UUID entityId;

  public DomainEntryReference(IDomainEntityType type, UUID domainId, UUID entityId) {
    this.type = type;
    this.domainId = domainId;
    this.entityId = entityId;
  }

  private DomainEntryImpl validateEntry () {
    DomainEntryImpl result = DataStorage.getDomainEntries().getEntry(this.entityId);
    if (result == null) {
      // TODO:
      throw new RuntimeException("Couldn't get an entry for " + entityId);
    }
    return result;
  }

  @Override
  public IDomainEntityType getType() {
    return type;
  }

  @Override
  public int getPriority() {
    return validateEntry().getPriority();
  }

  @Override
  public void setPriority(int priority) {
    validateEntry().setPriority(priority);
  }

  @Override
  public long getLastUpdated() {
    return validateEntry().getLastUpdated();
  }

  @Override
  public UUID getDomainId() {
    return domainId;
  }

  @Override
  public UUID getEntityId() {
    return entityId;
  }

  @Override
  public UUIDNameData.Name getEntityName() {
    return validateEntry().getEntityName();
  }

  @Override
  public BlockPos getPosition() {
    return validateEntry().getPosition();
  }

  @Override
  public RegistryKey<World> getDimension() {
    return validateEntry().getDimension();
  }

  @Nullable
  @Override
  public RemoteInventory getInventory() {
    return validateEntry().getInventory();
  }

  @Override
  public IDomainEntityType getTypeByOrdinal(int ordinal) {
    return null;
  }

  @Override
  public IDomainEntry getEntry() {
    return DataStorage.getDomainEntries().getEntry(this.entityId);
  }
}

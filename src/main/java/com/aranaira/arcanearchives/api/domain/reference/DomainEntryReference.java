package com.aranaira.arcanearchives.api.domain.reference;

import com.aranaira.arcanearchives.api.block.entity.IDomainEntityType;
import com.aranaira.arcanearchives.api.data.DataStorage;
import com.aranaira.arcanearchives.api.data.UUIDNameData;
import com.aranaira.arcanearchives.api.domain.IDomainEntry;
import com.aranaira.arcanearchives.api.domain.UpdateResult;
import com.aranaira.arcanearchives.api.domain.impl.DomainEntryImpl;
import com.aranaira.arcanearchives.api.inventory.RemoteInventory;
import com.aranaira.arcanearchives.api.reference.Identifiers;
import com.aranaira.arcanearchives.block.entity.DomainEntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import noobanidus.libs.noobutil.tracking.ItemTracking;

import javax.annotation.Nullable;
import java.util.UUID;

public class DomainEntryReference implements IDomainEntry, INBTSerializable<CompoundNBT> {
  private IDomainEntityType type;
  private UUID domainId;
  private UUID entityId;
  private long lastSynchronized = -1;

  protected DomainEntryReference () {
  }

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

  @Override
  public boolean hasTracking() {
    return validateEntry().hasTracking();
  }

  @Override
  public ItemTracking getTracking() {
    return validateEntry().getTracking();
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

  @Override
  public UpdateResult updateFrom(IDomainEntry other) {
    if (other instanceof DomainEntryReference) {
      if (!other.getDomainId().equals(this.domainId) || !other.getEntityId().equals(this.entityId) || !other.getType().equals(this.type)) {
        return UpdateResult.INVALID;
      }

      return UpdateResult.NO_CHANGE;
    }
    return validateEntry().updateFrom(other);
  }

  @Override
  public CompoundNBT serializeNBT() {
    CompoundNBT result = new CompoundNBT();
    result.putUUID(Identifiers.domainId, domainId);
    result.putUUID(Identifiers.entityId, entityId);
    result.putInt(Identifiers.entityType, type.getOrdinal());
    return result;
  }

  @Override
  public void deserializeNBT(CompoundNBT nbt) {
    this.domainId = nbt.getUUID(Identifiers.domainId);
    this.entityId = nbt.getUUID(Identifiers.entityId);
    this.type = DomainEntityType.byOrdinal(nbt.getInt(Identifiers.entityType));
  }

  public static DomainEntryReference fromNBT (CompoundNBT nbt) {
    DomainEntryReference result = new DomainEntryReference();
    result.deserializeNBT(nbt);
    return result;
  }
}

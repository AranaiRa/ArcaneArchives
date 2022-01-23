package com.aranaira.arcanearchives.api.domain.impl;

import com.aranaira.arcanearchives.api.block.entity.IDomainEntityType;
import com.aranaira.arcanearchives.api.data.DataStorage;
import com.aranaira.arcanearchives.api.data.UUIDNameData;
import com.aranaira.arcanearchives.api.domain.IDomainEntry;
import com.aranaira.arcanearchives.api.domain.UpdateResult;
import com.aranaira.arcanearchives.api.domain.reference.DomainEntryReference;
import com.aranaira.arcanearchives.api.inventory.RemoteInventory;
import com.aranaira.arcanearchives.api.reference.Identifiers;
import com.aranaira.arcanearchives.block.entity.DomainEntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import noobanidus.libs.noobutil.tracking.ItemTracking;

import javax.annotation.Nullable;
import java.util.UUID;

public class DomainEntryImpl implements IDomainEntry, INBTSerializable<CompoundNBT> {
  private IDomainEntityType type;
  private int priority;
  private long lastUpdated;

  private UUID domainId;
  private UUID entityId;
  private UUIDNameData.Name entityName;

  private BlockPos position;
  private RegistryKey<World> dimension;

  private RemoteInventory inventory = null;

  protected DomainEntryImpl() {
  }

  public DomainEntryImpl(IDomainEntityType type, UUID domainId, UUID entityId, BlockPos position, RegistryKey<World> dimension) {
    this.type = type;
    this.domainId = domainId;
    this.entityId = entityId;
    this.position = position;
    this.dimension = dimension;
    if (type.hasInventory()) {
      this.inventory = new RemoteInventory(this.entityId, type.getInventorySize(), type.getInventoryBuilder());
    }
  }

  @Override
  public IDomainEntityType getType() {
    return type;
  }

  @Override
  public int getPriority() {
    return priority;
  }

  @Override
  public void setPriority(int priority) {
    this.priority = priority;
  }

  @Override
  public long getLastUpdated() {
    return lastUpdated;
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
    if (this.entityName == null) {
      this.entityName = DataStorage.getEntityName(getEntityId());
    }

    return this.entityName;
  }

  @Override
  public BlockPos getPosition() {
    return position;
  }

  @Override
  public RegistryKey<World> getDimension() {
    return dimension;
  }

  @Override
  public boolean hasTracking() {
    return inventory != null && inventory.hasTracking();
  }

  @Override
  @Nullable
  public ItemTracking getTracking() {
    if (inventory == null) {
      return null;
    }
    return inventory.getTracking();
  }

  @Nullable
  @Override
  public RemoteInventory getInventory() {
    return inventory;
  }

  @Override
  public IDomainEntityType getTypeByOrdinal(int ordinal) {
    return DomainEntityType.byOrdinal(ordinal);
  }

  @Override
  public DomainEntryImpl getEntry() {
    return this;
  }

  @Override
  public UpdateResult updateFrom(IDomainEntry other) {
    if (!other.getEntityId().equals(this.entityId) || !other.getType().equals(this.type)) {
      return UpdateResult.INVALID;
    }

    boolean changed = false;

    if (!other.getDomainId().equals(this.domainId)) {
      this.domainId = other.getDomainId();
      changed = true;
    }
    if (!other.getPosition().equals(this.position)) {
      this.position = other.getPosition();
      changed = true;
    }
    if (!other.getDimension().equals(this.dimension)) {
      this.dimension = other.getDimension();
      changed = true;
    }
    if (this.entityName == null) {
      if (other.getEntityName() != null) {
        this.entityName = other.getEntityName();
      } else {
        this.entityName = DataStorage.getEntityName(entityId);
      }
      changed = true;
    }
    if (other.getPriority() != this.priority) {
      this.priority = other.getPriority();
      changed = true;
    }

    if (changed) {
      update();
      return UpdateResult.UPDATED;
    }

    return UpdateResult.NO_CHANGE;
  }

  @Override
  public CompoundNBT serializeNBT() {
    CompoundNBT nbt = new CompoundNBT();
    nbt.putInt(Identifiers.entityType, type.getOrdinal());
    nbt.putInt(Identifiers.Data.priority, priority);
    nbt.putUUID(Identifiers.domainId, domainId);
    nbt.putUUID(Identifiers.entityId, entityId);
    nbt.put(Identifiers.entityName, getEntityName().serializeNBT());
    nbt.putString(Identifiers.Data.dimension, dimension.getRegistryName().toString());
    nbt.put(Identifiers.Data.position, NBTUtil.writeBlockPos(position));
    return nbt;
  }

  @Override
  public void deserializeNBT(CompoundNBT nbt) {
    this.type = getTypeByOrdinal(nbt.getInt(Identifiers.entityType));
    this.priority = nbt.getInt(Identifiers.Data.priority);
    this.lastUpdated = -1;
    this.domainId = nbt.getUUID(Identifiers.domainId);
    this.entityId = nbt.getUUID(Identifiers.entityId);
    this.entityName = UUIDNameData.Name.fromNBT(nbt.getCompound(Identifiers.entityName));
    this.dimension = RegistryKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(nbt.getString(Identifiers.Data.dimension)));
    this.position = NBTUtil.readBlockPos(nbt.getCompound(Identifiers.Data.position));
  }

  private void update () {
    this.lastUpdated = ServerLifecycleHooks.getCurrentServer().getLevel(World.OVERWORLD).getGameTime();
  }

  public static DomainEntryImpl fromNBT(CompoundNBT nbt) {
    DomainEntryImpl result = new DomainEntryImpl();
    result.deserializeNBT(nbt);
    return result;
  }

  public CompoundNBT asReference () {
    CompoundNBT nbt = new CompoundNBT();
    nbt.putInt(Identifiers.entityType, type.getOrdinal());
    nbt.putUUID(Identifiers.domainId, domainId);
    nbt.putUUID(Identifiers.entityId, entityId);
    return nbt;
  }
}

package com.aranaira.arcanearchives.api.domain.impl;

import com.aranaira.arcanearchives.api.block.entity.IDomainEntityType;
import com.aranaira.arcanearchives.api.data.DataStorage;
import com.aranaira.arcanearchives.api.data.UUIDNameData;
import com.aranaira.arcanearchives.api.domain.IDomainEntry;
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

  protected DomainEntryImpl () {
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
  public IDomainEntry getEntry() {
    return this;
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

  public static DomainEntryImpl fromNBT (CompoundNBT nbt) {
    DomainEntryImpl result = new DomainEntryImpl();
    result.deserializeNBT(nbt);
    return result;
  }
}

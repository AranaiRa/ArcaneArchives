package com.aranaira.arcanearchives.api.domain;

import com.aranaira.arcanearchives.api.block.entity.IDomainBlockEntity;
import com.aranaira.arcanearchives.api.inventory.RemoteInventory;
import com.aranaira.arcanearchives.api.reference.Identifiers;
import com.aranaira.arcanearchives.api.reference.InventoryInfo;
import com.aranaira.arcanearchives.block.entity.CrystalWorkbenchBlockEntity;
import com.aranaira.arcanearchives.block.entity.RadiantChestBlockEntity;
import com.aranaira.arcanearchives.block.entity.RadiantResonatorBlockEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import noobanidus.libs.noobutil.recipe.AbstractLargeItemHandler;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.IntFunction;

public class DomainEntry implements INBTSerializable<CompoundNBT> {
  // TODO: Move this somewhere useful
  private static final Map<String, Class<? extends IDomainBlockEntity>> CLASS_LOOKUP = new HashMap<>();

  static {
    CLASS_LOOKUP.put(RadiantChestBlockEntity.class.getName(), RadiantChestBlockEntity.class);
    CLASS_LOOKUP.put(CrystalWorkbenchBlockEntity.class.getName(), CrystalWorkbenchBlockEntity.class);
    CLASS_LOOKUP.put(RadiantResonatorBlockEntity.class.getName(), RadiantResonatorBlockEntity.class);
  }

  private BlockPos position;
  private RegistryKey<World> dimension;
  private UUID domainId;
  private RemoteInventory inventory;
  private UUID entityId;
  private Class<? extends IDomainBlockEntity> clazz;
  private int priority;
  private long lastUpdated = -1;

  protected DomainEntry() {
  }

  public DomainEntry(BlockPos position, RegistryKey<World> dimension, UUID domainId, UUID entityId, Class<? extends IDomainBlockEntity> clazz) {
    this.position = position;
    this.dimension = dimension;
    this.domainId = domainId;
    this.clazz = clazz;
    this.entityId = entityId;
    IntFunction<? extends AbstractLargeItemHandler> builder = InventoryInfo.inventoryBuilder(clazz);
    if (builder == null) {
      this.inventory = null;
    } else {
      this.inventory = new RemoteInventory(this.entityId, InventoryInfo.sizeFor(clazz), builder);
    }
  }

  public boolean updateFrom(DomainEntry other) {
    boolean changed = false;
    if (!this.position.equals(other.position)) {
      this.position = other.position;
      changed = true;
    }
    if (!this.dimension.equals(other.dimension)) {
      this.dimension = other.dimension;
      changed = true;
    }
    if (!this.domainId.equals(other.domainId)) {
      this.domainId = other.domainId;
      changed = true;
    }
    if (!this.clazz.equals(other.clazz)) {
      // todo: in theory this shouldn't happen
      this.clazz = other.clazz;
      changed = true;
    }
    if (!this.entityId.equals(other.entityId)) {
      this.entityId = other.entityId;
      changed = true;
    }
    if (changed) {
      if (other.inventory != null) {
        IntFunction<? extends AbstractLargeItemHandler> builder = InventoryInfo.inventoryBuilder(clazz);
        if (builder == null) {
          this.inventory = null;
        } else {
          this.inventory = new RemoteInventory(this.entityId, InventoryInfo.sizeFor(clazz), builder);
        }
      }
      //noinspection ConstantConditions
      this.lastUpdated = ServerLifecycleHooks.getCurrentServer().getLevel(World.OVERWORLD).getGameTime();
    }
    return changed;
  }

  public BlockPos getPosition() {
    return position;
  }

  public RegistryKey<World> getDimension() {
    return dimension;
  }

  public UUID getDomainId() {
    return domainId;
  }

  public UUID getEntityId() {
    return entityId;
  }

  public Class<? extends IDomainBlockEntity> getClassType() {
    return clazz;
  }

  public int getPriority() {
    return priority;
  }

  public long getLastUpdated() {
    return lastUpdated;
  }

  public void setPriority(int priority) {
    this.priority = priority;
  }

  public RemoteInventory getInventory() {
    return inventory;
  }

  public boolean hasInventory() {
    return inventory != null;
  }

  public boolean hasTracking() {
    return inventory != null && inventory.hasTracking();
  }

  @Nullable
  public <T extends IDomainBlockEntity> T getEntity(MinecraftServer server, Class<T> clazz, boolean forceLoad) {
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
      return clazz.cast(te);
    }

    return null;
  }

  @Nullable
  public <T extends IDomainBlockEntity> T getEntity(MinecraftServer server, Class<T> clazz) {
    return getEntity(server, clazz, false);
  }

  @Override
  public CompoundNBT serializeNBT() {
    CompoundNBT tag = new CompoundNBT();
    tag.put(Identifiers.Data.position, NBTUtil.writeBlockPos(position));
    tag.putString(Identifiers.Data.dimension, dimension.getRegistryName().toString());
    tag.putUUID(Identifiers.domainId, domainId);
    tag.putUUID(Identifiers.entityId, entityId);
    tag.putString(Identifiers.Data.clazz, clazz.getName());
    tag.putInt(Identifiers.Data.priority, priority);
    tag.putLong(Identifiers.Data.lastUpdated, lastUpdated);
    return tag;
  }

  @Override
  public void deserializeNBT(CompoundNBT nbt) {
    this.position = NBTUtil.readBlockPos(nbt.getCompound(Identifiers.Data.position));
    this.dimension = RegistryKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(nbt.getString(Identifiers.Data.dimension)));
    this.domainId = nbt.getUUID(Identifiers.domainId);
    this.entityId = nbt.getUUID(Identifiers.entityId);
    this.priority = nbt.getInt(Identifiers.Data.priority);
    this.lastUpdated = nbt.getLong(Identifiers.Data.lastUpdated);
    String className = nbt.getString(Identifiers.Data.clazz);
    this.clazz = CLASS_LOOKUP.get(className);
    if (this.clazz == null) {
      throw new IllegalArgumentException("invalid class found in domain info: " + className);
    }
  }

  public static DomainEntry fromNBT(CompoundNBT tag) {
    DomainEntry result = new DomainEntry();
    result.deserializeNBT(tag);
    return result;
  }
}

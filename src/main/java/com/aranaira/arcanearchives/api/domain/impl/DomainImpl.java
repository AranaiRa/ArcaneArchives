package com.aranaira.arcanearchives.api.domain.impl;

import com.aranaira.arcanearchives.api.ArcaneArchivesAPI;
import com.aranaira.arcanearchives.api.data.DataStorage;
import com.aranaira.arcanearchives.api.data.DomainEntryData;
import com.aranaira.arcanearchives.api.data.UUIDNameData;
import com.aranaira.arcanearchives.api.domain.IDomain;
import com.aranaira.arcanearchives.api.domain.IDomainEntry;
import com.aranaira.arcanearchives.api.inventory.RemoteInventory;
import com.aranaira.arcanearchives.api.reference.Identifiers;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import noobanidus.libs.noobutil.tracking.ItemTracking;

import javax.annotation.Nullable;
import java.util.*;

public class DomainImpl implements IDomain, INBTSerializable<CompoundNBT> {
  private UUID domainId;
  private UUIDNameData.Name domainName = null;
  private long lastUpdated = -1;
  private ItemTracking tracking = null;
  private final Set<UUID> enlistedIds = new HashSet<>();
  private List<DomainEntryImpl> domainEntries = null;

  protected DomainImpl () {
  }

  public DomainImpl(UUID domainId) {
    this.domainId = domainId;
  }

  @Override
  public UUID getDomainId() {
    return domainId;
  }

  @Override
  public UUIDNameData.Name getDomainName() {
    if (domainName == null) {
      domainName = DataStorage.getDomainName(domainId);
    }

    return domainName;
  }

  @Override
  public Set<UUID> getEnlisted() {
    return enlistedIds;
  }

  public List<DomainEntryImpl> getImplementedEntries() {
    if (domainEntries == null) {
      domainEntries = new ArrayList<>();
      DomainEntryData data = DataStorage.getDomainEntries();
      for (UUID enlisted : enlistedIds) {
        DomainEntryImpl impl = data.getEntry(enlisted);
        if (impl == null) {
          ArcaneArchivesAPI.LOG.error("Enlisted entry " + enlisted + " doesn't exist!");
        } else if (impl.getDomainId().equals(domainId)) {
          domainEntries.add(impl);
        } else {
          ArcaneArchivesAPI.LOG.error("Enlisted entry " + enlisted + " doesn't belong to " + domainId + " but to " + impl.getDomainId() + "!");
        }
      }
    }

    return domainEntries;
  }

  @Override
  public List<? extends IDomainEntry> getEntries() {
    return domainEntries;
  }

  private void rebuildTracking() {
    tracking = new ItemTracking();

    for (DomainEntryImpl entry : getImplementedEntries()) {
      if (!entry.hasInventory()) {
        continue;
      }

      RemoteInventory inv = entry.getInventory();
      if (inv == null) {
        ArcaneArchivesAPI.LOG.error("Somehow inventory for " + entry.getEntityId() + " is null even though it is a " + entry.getType());
      } else {
        tracking.combine(inv.getTracking());
      }
    }

    update();
  }

  @Nullable
  @Override
  public ItemTracking getTracking() {
    if (lastUpdated == -1) {
      tracking = null;
    } else {
      for (DomainEntryImpl entry : getImplementedEntries()) {
        if (entry.hasInventory() && !entry.hasTracking()) {
          tracking = null;
        }
      }
    }

    if (tracking == null) {
      rebuildTracking();
    }

    return tracking;
  }

  @Override
  public long getLastUpdated() {
    return lastUpdated;
  }

  @Override
  public boolean enlist(IDomainEntry entry) {
    return false;
  }

  @Override
  public boolean destroy(IDomainEntry entry) {
    return false;
  }

  @Override
  public void domainTick() {

  }

  @Override
  public void immanenceTick() {

  }

  private void update () {
    //noinspection ConstantConditions
    lastUpdated = ServerLifecycleHooks.getCurrentServer().getLevel(World.OVERWORLD).getGameTime();
  }

  @Override
  public CompoundNBT serializeNBT() {
    CompoundNBT nbt = new CompoundNBT();
    nbt.putUUID(Identifiers.domainId, domainId);
    nbt.put(Identifiers.domainName, getDomainName().serializeNBT());
    ListNBT entries = new ListNBT();

    return nbt;
  }

  @Override
  public void deserializeNBT(CompoundNBT nbt) {

  }

  public static DomainImpl fromNBT (CompoundNBT nbt) {
    DomainImpl domain = new DomainImpl();
    domain.deserializeNBT(nbt);
    return domain;
  }
}

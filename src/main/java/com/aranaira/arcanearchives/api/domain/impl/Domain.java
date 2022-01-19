package com.aranaira.arcanearchives.api.domain.impl;

import com.aranaira.arcanearchives.api.ArcaneArchivesAPI;
import com.aranaira.arcanearchives.api.data.DataStorage;
import com.aranaira.arcanearchives.api.data.DomainEntryData;
import com.aranaira.arcanearchives.api.data.UUIDNameData;
import com.aranaira.arcanearchives.api.domain.IDomain;
import com.aranaira.arcanearchives.api.domain.IDomainEntry;
import noobanidus.libs.noobutil.tracking.ItemTracking;

import javax.annotation.Nullable;
import java.util.*;

public class Domain implements IDomain {
  private final UUID domainId;
  private UUIDNameData.Name domainName = null;
  private long lastUpdated = -1;
  private ItemTracking tracking = null;
  private Set<UUID> enlistedIds = new HashSet<>();
  private List<DomainEntryImpl> domainEntries = null;

  public Domain(UUID domainId) {
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

  private void rebuildTracking () {

  }

  @Nullable
  @Override
  public ItemTracking getTracking() {
    return null;
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
}

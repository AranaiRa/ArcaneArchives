package com.aranaira.arcanearchives.api.domain.reference;

import com.aranaira.arcanearchives.api.data.UUIDNameData;
import com.aranaira.arcanearchives.api.domain.IDomain;
import com.aranaira.arcanearchives.api.domain.IDomainEntry;
import com.aranaira.arcanearchives.api.domain.impl.DomainImpl;
import noobanidus.libs.noobutil.tracking.ItemTracking;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class DomainReference implements IDomain {
  private UUID domainId;
  private Set<UUID> enlistedIds;

  @Override
  public UUID getDomainId() {
    return domainId;
  }

  private DomainImpl validateDomain () {
  }

  @Override
  public UUIDNameData.Name getDomainName() {
    return null;
  }

  @Override
  public Set<UUID> getEnlisted() {
    return null;
  }

  @Override
  public List<? extends IDomainEntry> getEntries() {
    return null;
  }

  @Nullable
  @Override
  public ItemTracking getTracking() {
    return null;
  }

  @Override
  public long getLastUpdated() {
    return 0;
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

package com.aranaira.arcanearchives.api.domain;

import com.aranaira.arcanearchives.api.data.UUIDNameData;
import noobanidus.libs.noobutil.tracking.ItemTracking;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface IDomain {
  UUID getDomainId();

  UUIDNameData.Name getDomainName();

  Set<UUID> getEnlisted();

  List<? extends IDomainEntry> getEntries();

  @Nullable
  ItemTracking getTracking();

  long getLastUpdated();

  boolean enlist(IDomainEntry entry);
  boolean destroy(IDomainEntry entry);

  void domainTick ();
  void immanenceTick ();
}

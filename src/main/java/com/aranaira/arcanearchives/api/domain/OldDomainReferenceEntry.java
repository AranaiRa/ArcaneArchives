/*
package com.aranaira.arcanearchives.api.domain;

import com.aranaira.arcanearchives.api.data.DataStorage;

import java.util.UUID;

public class OldDomainReferenceEntry {
  private final UUID domainId;
  private final UUID entryId;

  public OldDomainReferenceEntry(UUID domainId, UUID entryId) {
    this.domainId = domainId;
    this.entryId = entryId;
  }

  public UUID getDomainId() {
    return domainId;
  }

  public UUID getEntryId() {
    return entryId;
  }

  public OldDomainEntry getEntry() {
    return DataStorage.getDomainEntries().getEntry(entryId);
  }

  public boolean valid() {
    return getEntry() != null;
  }

  public boolean valid (UUID domainId) {
    OldDomainEntry entry = getEntry();
    if (entry == null) {
      return false;
    }

    return entry.getDomainId().equals(domainId);
  }
}
*/

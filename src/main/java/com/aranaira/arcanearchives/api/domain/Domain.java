package com.aranaira.arcanearchives.api.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class Domain implements Iterable<DomainEntry<?>> {
  private final UUID domainId;
  private long lastUpdated = -1;

  private final List<DomainEntry<?>> entries = new ArrayList<>();

  public Domain(UUID domainId) {
    this.domainId = domainId;
  }

  public void clearEntries() {
    entries.clear();
  }

  @Override
  public Iterator<DomainEntry<?>> iterator() {
    return entries.iterator();
  }

  public List<DomainEntry<?>> getEntries() {
    return entries;
  }
}

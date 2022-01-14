package com.aranaira.arcanearchives.api.domain;

import java.util.List;
import java.util.UUID;

public class Domain {
  private UUID domainId;
  private long lastUpdated = -1;

  private List<DomainEntry> enlisted;
  private List<DomainEntry> knownEntries;
}

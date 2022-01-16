package com.aranaira.arcanearchives.api.block.entity;

import com.aranaira.arcanearchives.api.data.UUIDNameData;

import javax.annotation.Nullable;
import java.util.UUID;

public interface IDomainBlockEntity extends IIdentifiedBlockEntity {
  @Nullable
  UUID getDomainId();

  @Nullable
  UUIDNameData.Name getDomainName();

  boolean isDomainUnknown();
}

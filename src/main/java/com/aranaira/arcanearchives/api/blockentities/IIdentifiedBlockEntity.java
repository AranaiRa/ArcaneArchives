package com.aranaira.arcanearchives.api.blockentities;

import com.aranaira.arcanearchives.api.data.UUIDNameData;

import javax.annotation.Nullable;
import java.util.UUID;

public interface IIdentifiedBlockEntity extends IArcaneArchivesBlockEntity {
  @Nullable
  UUID getEntityId();

  UUIDNameData.Name getEntityName();
}

package com.aranaira.arcanearchives.api.blockentities;

import com.aranaira.arcanearchives.api.data.UUIDNameData;

import javax.annotation.Nullable;
import java.util.UUID;

public interface IIdentifiedBlockEntity extends IArcaneArchivesBlockEntity {
  UUID UNKNOWN = UUID.fromString("981dd3f2-f0f7-43cf-98bb-b14d8726057b");

  @Nullable
  UUID getEntityId();

  UUIDNameData.Name getEntityName();

  boolean isBlockUnknown ();

}

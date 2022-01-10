package com.aranaira.arcanearchives.api.block.entity;

import com.aranaira.arcanearchives.api.data.UUIDNameData;
import noobanidus.libs.noobutil.block.entities.IReferentialBlockEntity;

import javax.annotation.Nullable;
import java.util.UUID;

public interface IIdentifiedBlockEntity extends IReferentialBlockEntity {
  UUID UNKNOWN = UUID.fromString("981dd3f2-f0f7-43cf-98bb-b14d8726057b");

  @Nullable
  UUID getEntityId();

  UUIDNameData.Name getEntityName();

  boolean isBlockUnknown ();

}

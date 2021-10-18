package com.aranaira.arcanearchives.api.blockentities;

import javax.annotation.Nullable;
import java.util.UUID;

public interface IArcaneArchivesBlockEntity {
  // TODO: Move this out of IArcaneArchivesTile
  @Nullable
  UUID getTileId ();
}

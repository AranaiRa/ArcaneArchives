package com.aranaira.arcanearchives.api.tiles;

import javax.annotation.Nullable;
import java.util.UUID;

public interface IArcaneArchivesTile {
  // TODO: Move this out of IArcaneArchivesTile
  @Nullable
  UUID getTileId ();
}

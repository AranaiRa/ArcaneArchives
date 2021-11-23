package com.aranaira.arcanearchives.api.blockentities;

import com.aranaira.arcanearchives.api.data.UUIDNameData;

import javax.annotation.Nullable;
import java.util.UUID;

public interface INetworkedBlockEntity extends IIdentifiedBlockEntity {
  @Nullable
  UUID getNetworkId ();

  @Nullable
  UUIDNameData.Name getNetworkName ();
}

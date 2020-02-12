package com.aranaira.arcanearchives.api.immanence;

import javax.annotation.Nullable;

public interface IImmanenceGenerator extends IImmanenceSubscriber {
  @Nullable
  IImmanenceSource generateImmanence();
}

package com.aranaira.arcanearchives.tileentities.interfaces;

import com.aranaira.arcanearchives.inventory.handlers.OptionalUpgradesHandler;
import com.aranaira.arcanearchives.inventory.handlers.SizeUpgradeItemHandler;

public interface IUpgradeableStorage {
  SizeUpgradeItemHandler getSizeUpgradesHandler();

  OptionalUpgradesHandler getOptionalUpgradesHandler();

  int getModifiedCapacity();
}

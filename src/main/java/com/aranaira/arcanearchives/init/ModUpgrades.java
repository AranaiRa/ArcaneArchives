package com.aranaira.arcanearchives.init;

import com.aranaira.arcanearchives.items.upgrades.UpgradeInfo;
import com.aranaira.arcanearchives.types.enums.UpgradeType;

public class ModUpgrades {
  public static UpgradeInfo MATRIX_BRACE = UpgradeInfo.Builder.create(UpgradeType.SIZE).slot(0).size(2)/*.classes(RadiantTankTileEntity.class, RadiantTroveTileEntity.class)*/.build();
  public static UpgradeInfo CONTAINMENT_FIELD = UpgradeInfo.Builder.create(UpgradeType.SIZE).slot(1).size(3)/*.classes(RadiantTankTileEntity.class)*/.build();
  public static UpgradeInfo MATERIAL_INTERFACE = UpgradeInfo.Builder.create(UpgradeType.SIZE).slot(1).size(3)/*.classes(RadiantTroveTileEntity.class)*/.build();
  public static UpgradeInfo RADIANT_KEY = UpgradeInfo.Builder.create(UpgradeType.LOCK).slot(-1).size(-1)/*.classes(RadiantTroveTileEntity.class)*/.build();
}

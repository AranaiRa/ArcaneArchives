package com.aranaira.arcanearchives.init;

import com.aranaira.arcanearchives.items.upgrades.UpgradeInfo;
import com.aranaira.arcanearchives.types.enums.UpgradeType;

public class ModUpgrades {
 public static UpgradeInfo MATRIX_BRACE = new UpgradeInfo.Builder(UpgradeType.SIZE).slot(0).size(2)/*.classes(RadiantTankTileEntity.class, RadiantTroveTileEntity.class)*/.build();
 public static UpgradeInfo CONTAINMENT_FIELD = new UpgradeInfo.Builder(UpgradeType.SIZE).slot(1).size(3)/*.classes(RadiantTankTileEntity.class)*/.build();
 public static UpgradeInfo MATERIAL_INTERFACE = new UpgradeInfo.Builder(UpgradeType.SIZE).slot(1).size(3)/*.classes(RadiantTroveTileEntity.class)*/.build();
}

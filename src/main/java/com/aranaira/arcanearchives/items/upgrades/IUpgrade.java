package com.aranaira.arcanearchives.items.upgrades;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import java.util.List;

public interface IUpgrade {
  default int getUpgradeSlot() {
    return getUpgradeSlot(ItemStack.EMPTY);
  }

  int getUpgradeSlot(ItemStack stack);

  default int getUpgradeSzie() {
    return getUpgradeSize(ItemStack.EMPTY);
  }

  int getUpgradeSize(ItemStack stack);

  List<Class<? extends TileEntity>> getUpgradeClasses(ItemStack stack);

  default List<Class<? extends TileEntity>> getUpgradeClasses() {
    return getUpgradeClasses(ItemStack.EMPTY);
  }

  default boolean upgradeFor(TileEntity te, ItemStack stack) {
    List<Class<? extends TileEntity>> classes = getUpgradeClasses(stack);
    return classes.contains(te.getClass());
  }

  default boolean upgradeFor(TileEntity te) {
    return upgradeFor(te, ItemStack.EMPTY);
  }

  default boolean isUpgrade() {
    return true;
  }
}

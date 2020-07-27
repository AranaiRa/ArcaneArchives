package com.aranaira.arcanearchives.items.upgrades;

import com.aranaira.arcanearchives.types.UpgradeType;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import java.util.List;

public interface IUpgrade {
  int NO_SIZE = -99;
  int NO_SLOT = -99;

  default int getUpgradeSlot() {
    return getUpgradeSlot(ItemStack.EMPTY);
  }

  int getUpgradeSlot(ItemStack stack);

  default int getUpgradeSize() {
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

  default boolean isUpgrade(ItemStack stack) {
    return true;
  }

  default boolean isUpgrade () {
    return isUpgrade(ItemStack.EMPTY);
  }

  UpgradeType getType(ItemStack stack);
}

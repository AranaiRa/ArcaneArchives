package com.aranaira.arcanearchives.inventories;

import net.minecraft.item.Items;
import net.minecraft.item.Item;

public class TroveUpgradeItemHandler extends SizeUpgradeItemHandler {
  @Override
  public Item getUpgradeForSlot(int slot) {
/*		switch (slot) {
			case 0:
				return ItemRegistry.COMPONENT_MATRIXBRACE;
			case 1:
				return ItemRegistry.COMPONENT_MATERIALINTERFACE;
			case 2:
				return BlockRegistry.STORAGE_SHAPED_QUARTZ.getItemBlock();
		}*/
    return Items.AIR;
  }
}

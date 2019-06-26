package com.aranaira.arcanearchives.inventory.handlers;

import com.aranaira.arcanearchives.init.BlockRegistry;
import com.aranaira.arcanearchives.init.ItemRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class TankUpgradeItemHandler extends SizeUpgradeItemHandler {
	@Override
	public Item getUpgradeForSlot (int slot) {
		switch (slot) {
			case 0:
				return ItemRegistry.COMPONENT_MATRIXBRACE;
			case 1:
				return ItemRegistry.COMPONENT_CONTAINMENTFIELD;
			case 2:
				return BlockRegistry.STORAGE_SHAPED_QUARTZ.getItemBlock();
		}
		return Items.AIR;
	}
}

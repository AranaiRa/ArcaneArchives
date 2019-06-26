package com.aranaira.arcanearchives.items;

import com.aranaira.arcanearchives.blocks.templates.BlockTemplate;
import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import com.aranaira.arcanearchives.util.types.UpgradeType;
import net.minecraft.item.ItemStack;

import java.util.List;

public interface IUpgradeItem {
	UpgradeType getUpgradeType (ItemStack stack);

	// Returns -1 if not a size upgrade
	// Otherwise returns the size of **this upgrade**
	int getUpgradeSize (ItemStack stack);

	// Returns -1 if any slot
	int getSlotIsUpgradeFor (ItemStack stack);

	List<Class<?>> upgradeFor ();

	default boolean isUpgradeFor (ImmanenceTileEntity entity) {
		return upgradeFor().contains(entity.getClass());
	}

	default boolean isUpgradeFor (Class<?> clazz) {
		return upgradeFor().contains(clazz);
	}

	default boolean isUpgradeFor (BlockTemplate block) {
		return upgradeFor().contains(block.getEntityClass());
	}
}

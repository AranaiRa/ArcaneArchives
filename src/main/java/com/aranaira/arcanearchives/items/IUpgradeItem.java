/*package com.aranaira.arcanearchives.items;

import com.aranaira.arcanearchives.blocks.templates.TemplateBlock;
import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import com.aranaira.arcanearchives.types.enums.UpgradeType;
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

	default boolean isUpgradeFor (TemplateBlock block) {
		return upgradeFor().contains(block.getEntityClass());
	}
}*/

package com.aranaira.arcanearchives.tileentities;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public interface IUpgradeableStorage {
	IItemHandler getSizeUpgradesHandler ();

	IItemHandler getOptionalUpgradesHandler ();

	int getMultiplierForUpgrade (ItemStack stack);

	boolean canModifierBeApplied (ItemStack stack);

	int getModifiedCapacity ();
}

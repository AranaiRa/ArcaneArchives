package com.aranaira.arcanearchives.blocks;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public interface IUpgradeableStorage {
	IItemHandler getSizeUpgradesHandler ();
	IItemHandler getOptionalUpgradesHandler ();

	int getMultiplierForUpgrade (ItemStack stack);
}

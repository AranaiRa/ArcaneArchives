package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import java.util.function.BiFunction;

public interface IUpgradeableStorage {
	IItemHandler getSizeUpgradesHandler ();
	IItemHandler getOptionalUpgradesHandler ();

	int getMultiplierForUpgrade (Item stack);
	boolean canModifierBeApplied (Item stack);
	int getCapacityModified ();

	void registerSpecialEffect (Item stack, SpecialEffect effect);

	@FunctionalInterface
	interface SpecialEffect {
		void apply (ImmanenceTileEntity tile, ItemStack stack);
	}
}

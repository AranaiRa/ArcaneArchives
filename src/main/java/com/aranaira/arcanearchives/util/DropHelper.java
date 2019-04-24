package com.aranaira.arcanearchives.util;

import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public class DropHelper
{
	public static void dropInventoryItems(World world, BlockPos pos, @Nullable IItemHandler inventory) {
		if(inventory == null) return;

		for(int i = 0; i < inventory.getSlots(); i++) {
			ItemStack toDrop = inventory.extractItem(i, 64, false);
			if(toDrop.isEmpty()) continue;

			InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), toDrop);
		}
	}
}

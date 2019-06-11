package com.aranaira.arcanearchives.util;

import com.aranaira.arcanearchives.tileentities.RadiantChestTileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public class ItemUtilities {
	public static boolean areStacksEqualIgnoreSize (ItemStack stackA, ItemStack stackB) {
		return ItemStack.areItemsEqual(stackA, stackB) && ItemStack.areItemStackTagsEqual(stackA, stackB);
	}

	public static int calculateRedstoneFromTileEntity (@Nullable TileEntity te) {
		if (te instanceof RadiantChestTileEntity) {
			return calculateRedstoneFromItemHandler(te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null));
		}
		return 0;
	}

	public static int calculateRedstoneFromItemHandler (@Nullable IItemHandler handler) {
		if (handler == null) {
			return 0;
		} else {
			int i = 0;
			float f = 0.0F;

			for (int j = 0; j < handler.getSlots(); ++j) {
				ItemStack itemstack = handler.getStackInSlot(j);

				if (!itemstack.isEmpty()) {
					f += (float) itemstack.getCount() / (float) Math.min(handler.getSlotLimit(j), itemstack.getMaxStackSize());
					++i;
				}
			}

			f = f / (float) handler.getSlots();
			return MathHelper.floor(f * 14.0F) + (i > 0 ? 1 : 0);
		}
	}
}

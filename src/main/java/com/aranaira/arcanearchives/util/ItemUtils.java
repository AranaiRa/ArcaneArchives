package com.aranaira.arcanearchives.util;

import com.aranaira.arcanearchives.tileentities.RadiantChestTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantTankTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantTroveTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantTroveTileEntity.TroveItemHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public class ItemUtils {
	public static boolean areStacksEqualIgnoreSize (ItemStack stackA, ItemStack stackB) {
		/*if (stackA.getItem() instanceof ItemBlock && stackB.getItem() instanceof ItemBlock) {
			return ItemStack.areItemsEqual(stackA, stackB);
		}*/
		return ItemStack.areItemsEqual(stackA, stackB) && ItemStack.areItemStackTagsEqual(stackA, stackB);
	}

	public static int calculateRedstoneFromTileEntity (@Nullable TileEntity te) {
		if (te instanceof RadiantChestTileEntity) {
			return calculateRedstoneFromItemHandler(te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null));
		} else if (te instanceof RadiantTroveTileEntity) {
			RadiantTroveTileEntity trove = (RadiantTroveTileEntity) te;
			TroveItemHandler handler = trove.getInventory();
			float f = (float) handler.getCount() / handler.getMaxCount();
			return MathHelper.floor(f * 14.0F) + (handler.getCount() > 0 ? 1 : 0);
		} else if (te instanceof RadiantTankTileEntity) {
			RadiantTankTileEntity tank = (RadiantTankTileEntity) te;
			int amount = tank.getInventory().getFluidAmount();
			float f = (float) amount / tank.getCapacity();
			return MathHelper.floor(f * 14.0F) + (amount > 0 ? 1 : 0);
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
					f += (float) itemstack.getCount() / (float) handler.getSlotLimit(i);//(float) Math.min(handler.getSlotLimit(j), itemstack.getMaxStackSize());
					++i;
				}
			}

			f = f / (float) handler.getSlots();

			return MathHelper.floor(f * 14.0F) + (i > 0 ? 1 : 0);
		}
	}

	public static NBTTagCompound getOrCreateTagCompound (ItemStack stack) {
		NBTTagCompound tagCompound = stack.getTagCompound();
		if (tagCompound == null) {
			tagCompound = new NBTTagCompound();
			stack.setTagCompound(tagCompound);
		}
		return tagCompound;
	}

	public static boolean ingredientsMatch (Ingredient ingredient1, Ingredient ingredient2) {
		if (ingredient1.isSimple() != ingredient2.isSimple()) {
			return false;
		}

		for (ItemStack stack : ingredient2.getMatchingStacks()) {
			if (!ingredient1.apply(stack)) {
				return false;
			}
		}

		for (ItemStack stack : ingredient1.getMatchingStacks()) {
			if (!ingredient2.apply(stack)) {
				return false;
			}
		}

		return true;
	}
}

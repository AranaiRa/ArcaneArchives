package com.aranaira.arcanearchives.inventory.handlers;

import com.aranaira.arcanearchives.items.gems.ArcaneGemItem;
import com.aranaira.arcanearchives.items.gems.GemUtil;
import com.aranaira.arcanearchives.items.gems.pendeloque.ParchtearItem;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

//Shamelessly pillaged from Roots' QuiverHandler
public class DevouringCharmHandler implements INBTSerializable<NBTTagCompound> {
	private ItemStack socket;
	private ItemStackHandler handler = new ItemStackHandler(7) {
		@Override
		public boolean isItemValid (int slot, @Nonnull ItemStack stack) {
			if(slot == 0) {
				if(stack.getItem() instanceof ParchtearItem) {
					GemUtil.restoreCharge(stack, -1);
					return true;
				}
				boolean validItem = false;
				validItem |= stack.getItem() == Items.WATER_BUCKET;
				validItem |= stack.getItem() == Items.LAVA_BUCKET;
				validItem |= stack.getItem() == Items.MILK_BUCKET;
				validItem |= stack.getItem() instanceof UniversalBucket;
				return validItem;
			}
			else
				return true;
		}

		@Override
		protected void onContentsChanged (int slot) {
			Item item = getStackInSlot(slot).getItem();
			boolean shouldSwap = false;
			shouldSwap |= item == Items.WATER_BUCKET;
			shouldSwap |= item == Items.LAVA_BUCKET;
			shouldSwap |= item == Items.MILK_BUCKET;
			shouldSwap |= item instanceof UniversalBucket;

			if(shouldSwap) {
				setStackInSlot(slot, new ItemStack(Items.BUCKET));
			}
			//TODO: Dump fluid contents of things with a fluid capability
			super.onContentsChanged(slot);
		}
	};

	public DevouringCharmHandler(ItemStack socket) {
		this.socket = socket;
	}

	@Override
	public NBTTagCompound serializeNBT () {
		return handler.serializeNBT();
	}

	@Override
	public void deserializeNBT (NBTTagCompound nbt) {
		handler.deserializeNBT(nbt);
	}

	public ItemStackHandler getInventory () {
		return handler;
	}

	public static DevouringCharmHandler getHandler (ItemStack stack) {
		DevouringCharmHandler handler = new DevouringCharmHandler(stack);

		return handler;
	}
}

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
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
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

				return stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
			}
			else {
				return true;
			}
		}

		@Override
		protected void onContentsChanged (int slot) {
			ItemStack stack = getStackInSlot(slot);
			IFluidHandlerItem cap = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
			if (cap != null) {
				IFluidTankProperties[] props = cap.getTankProperties();
				if (props.length >= 1) {
					FluidStack drain = props[0].getContents();
					cap.drain(drain, true);
				}
			}
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

/*package com.aranaira.arcanearchives.inventory.handlers;

import com.aranaira.arcanearchives.init.ItemRegistry;
import com.aranaira.arcanearchives.items.gems.GemUtil;
import com.aranaira.arcanearchives.items.gems.pendeloque.ParchtearItem;
import com.aranaira.arcanearchives.util.ItemUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

//Shamelessly pillaged from Roots' QuiverHandler
public class DevouringCharmHandler {
	private ItemStack socket;
	private ItemStackHandler autovoidHandler = new ItemStackHandler(6) {
		@Nonnull
		@Override
		public ItemStack insertItem (int slot, @Nonnull ItemStack stack, boolean simulate) {
			if (stack.isEmpty()) {
				super.setStackInSlot(slot, stack);
			} else {
				ItemStack copy = stack.copy();
				copy.setCount(1);
				super.setStackInSlot(slot, copy);
			}
			saveToStack();
			return stack;
		}

		@Nonnull
		@Override
		public ItemStack extractItem (int slot, int amount, boolean simulate) {
			super.setStackInSlot(slot, ItemStack.EMPTY);
			saveToStack();
			return ItemStack.EMPTY;
		}

		@Override
		public void setStackInSlot (int slot, @Nonnull ItemStack stack) {
			throw new IllegalArgumentException("setStackInSlot is unusable");
		}

		@Override
		protected int getStackLimit (int slot, @Nonnull ItemStack stack) {
			return 1;
		}
	};

	private ItemStackHandler handler = new ItemStackHandler(7) {
		@Override
		public boolean isItemValid (int slot, @Nonnull ItemStack stack) {
			if (slot == 0) {
				return stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null) || stack.getItem() == ItemRegistry.PARCHTEAR;
			} else {
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

			if (slot == 0 && stack.getItem() == ItemRegistry.PARCHTEAR) {
				if (stack.getItem() instanceof ParchtearItem) {
					GemUtil.manuallyRestoreCharge(stack, -1);
				}
			}

			super.onContentsChanged(slot);
		}
	};

	public DevouringCharmHandler (ItemStack socket) {
		this.socket = socket;
		NBTTagCompound tag = ItemUtils.getOrCreateTagCompound(socket);
		if (tag.hasKey(Tags.AUTOVOID_HANDLER)) {
			this.autovoidHandler.deserializeNBT(tag.getCompoundTag(Tags.AUTOVOID_HANDLER));
		}
	}

	public void saveToStack () {
		NBTTagCompound tag = ItemUtils.getOrCreateTagCompound(socket);
		tag.setTag(Tags.AUTOVOID_HANDLER, autovoidHandler.serializeNBT());
	}

	public ItemStackHandler getInventory () {
		return handler;
	}

	public ItemStackHandler getAutovoidInventory () {
		return autovoidHandler;
	}

	public List<ItemStack> getItemsToVoid () {
		List<ItemStack> items = new ArrayList<>();
		for (int i = 0; i < 6; i++) {
			ItemStack item = autovoidHandler.getStackInSlot(i);
			if (!item.isEmpty()) {
				items.add(item);
			}
		}

		return items;
	}

	public boolean shouldVoidItem (ItemStack stack) {
		for (ItemStack other : getItemsToVoid()) {
			if (ItemUtils.areStacksEqualIgnoreSize(stack, other)) {
				return true;
			}
		}

		return false;
	}

	public static DevouringCharmHandler getHandler (ItemStack stack) {
		DevouringCharmHandler handler = new DevouringCharmHandler(stack);

		return handler;
	}

	public static class Tags {
		public static final String AUTOVOID_HANDLER = "autovoid_handler";

		public Tags () {
		}
	}
}*/

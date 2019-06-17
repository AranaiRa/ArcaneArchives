package com.aranaira.arcanearchives.inventory.handlers;

import com.aranaira.arcanearchives.items.gems.ArcaneGemItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

//Shamelessly pillaged from Roots' QuiverHandler
public class GemSocketHandler implements INBTSerializable<NBTTagCompound> {
	private ItemStack socket;
	private ItemStackHandler handler = new ItemStackHandler(1) {
		@Override
		public boolean isItemValid (int slot, @Nonnull ItemStack stack) {
			return stack.getItem() instanceof ArcaneGemItem;
		}

		@Override
		protected void onContentsChanged (int slot) {
			super.onContentsChanged(slot);

			GemSocketHandler.this.saveToStack();
		}
	};

	public GemSocketHandler (ItemStack socket) {
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

	public ItemStack getGem () {
		return handler.getStackInSlot(0);
	}

	public ItemStackHandler getInventory () {
		return handler;
	}

	public static GemSocketHandler getHandler (ItemStack stack) {
		GemSocketHandler handler = new GemSocketHandler(stack);
		if (stack.hasTagCompound()) {
			if (stack.getTagCompound().hasKey("gem")) {
				handler.deserializeNBT(stack.getTagCompound().getCompoundTag("gem"));
			}
		}

		return handler;
	}

	public void saveToStack () {
		NBTTagCompound nbt = socket.getTagCompound();
		if (nbt == null) {
			nbt = new NBTTagCompound();
			socket.setTagCompound(nbt);
		}

		nbt.setTag("gem", serializeNBT());
	}
}

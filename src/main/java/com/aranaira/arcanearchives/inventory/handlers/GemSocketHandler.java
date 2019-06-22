package com.aranaira.arcanearchives.inventory.handlers;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import com.aranaira.arcanearchives.init.ItemRegistry;
import com.aranaira.arcanearchives.items.gems.ArcaneGemItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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

	public static ItemStack findSocket (EntityPlayer player) {
		if (player.getHeldItemMainhand().getItem() == ItemRegistry.BAUBLE_GEMSOCKET) {
			return player.getHeldItemMainhand();
		}

		IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
		for (int i : BaubleType.BODY.getValidSlots()) {
			ItemStack stack = handler.getStackInSlot(i);
			if (stack.getItem() == ItemRegistry.BAUBLE_GEMSOCKET) return stack;
		}

		IItemHandler playerInventory = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		for (int i = 0; i < playerInventory.getSlots(); i++) {
			ItemStack stack = playerInventory.getStackInSlot(i);
			if (stack.getItem() == ItemRegistry.BAUBLE_GEMSOCKET) return stack;
		}

		return ItemStack.EMPTY;
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

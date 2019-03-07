package com.aranaira.arcanearchives.inventory.handlers;

import com.aranaira.arcanearchives.tileentities.GemCuttersTableTileEntity;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.*;
import net.minecraftforge.items.wrapper.*;

import javax.annotation.Nonnull;

public class InventoryCraftingGCT extends InventoryCraftingItemHandler<GemCuttersTableTileEntity, CombinedInvWrapper>
{
	private ItemStackHandler tileInventory;
	private IInventory playerInventory;
	private CombinedInvWrapper wrappedInventory;

	private InventoryCraftingGCT(Container eventHandler, GemCuttersTableTileEntity tile, CombinedInvWrapper inventory, ItemStackHandler parent, IInventory playerInventory, int width, int height)
	{
		super(eventHandler, inventory, tile, width, height);

		this.wrappedInventory = inventory;
		this.playerInventory = playerInventory;
		this.tileInventory = parent;
	}

	public static InventoryCraftingGCT build(Container eventHandler, GemCuttersTableTileEntity tile, ItemStackHandler tileInventory, IInventory playerInventory)
	{
		IItemHandlerModifiable wrappedPlayerInventory = new RangedWrapper(new InvWrapper(playerInventory), 0, 36);
		CombinedInvWrapper wrappedInventories = new CombinedInvWrapper(wrappedPlayerInventory, tileInventory);

		return new InventoryCraftingGCT(eventHandler, tile, wrappedInventories, tileInventory, playerInventory, 9, 6);
	}

	@Deprecated
	public static class PlayerInvWrapper extends InvWrapper
	{

		public PlayerInvWrapper(IInventory inv)
		{
			super(inv);
		}

		@Override
		public int getSlots()
		{
			return 36;
		}

		@Nonnull
		@Override
		public ItemStack getStackInSlot(int slot)
		{
			if(slot >= 36) return ItemStack.EMPTY;

			return super.getStackInSlot(slot);
		}

		@Nonnull
		@Override
		public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
		{
			if(slot >= 36) return stack;

			return super.insertItem(slot, stack, simulate);
		}

		@Nonnull
		@Override
		public ItemStack extractItem(int slot, int amount, boolean simulate)
		{
			if(slot >= 36) return ItemStack.EMPTY;

			return super.extractItem(slot, amount, simulate);
		}

		@Override
		public void setStackInSlot(int slot, @Nonnull ItemStack stack)
		{
			if(slot >= 36) return;

			super.setStackInSlot(slot, stack);
		}

		@Override
		public int getSlotLimit(int slot)
		{
			return super.getSlotLimit(slot);
		}

		@Override
		public boolean isItemValid(int slot, @Nonnull ItemStack stack)
		{
			if(slot >= 36) return false;

			return super.isItemValid(slot, stack);
		}
	}
}

package com.aranaira.arcanearchives.inventory.handlers;

import com.aranaira.arcanearchives.tileentities.GemCuttersTableTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantCraftingTableTileEntity;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public abstract class InventoryCraftingItemHandler<T extends TileEntity, V extends IItemHandlerModifiable> extends InventoryCrafting
{
	private final int length;
	private final Container eventHandler;
	private final V parent;
	private final T tile;
	private boolean doNotCallUpdates;

	public InventoryCraftingItemHandler(Container eventHandler, V parent, T tile, int width, int height)
	{
		super(eventHandler, width, height);

		this.tile = tile;
		this.parent = parent;
		this.length = width * height;
		this.eventHandler = eventHandler;
		this.doNotCallUpdates = false;
	}

	@Override
	public int getSizeInventory()
	{
		return length;
	}

	@Override
	public boolean isEmpty()
	{
		for(int i = 0; i < this.parent.getSlots(); i++)
		{
			ItemStack slot = this.parent.getStackInSlot(i);
			if(!slot.isEmpty()) return false;
		}
		return true;
	}

	@Nonnull
	@Override
	public ItemStack getStackInSlot(int index)
	{
		return index >= this.getSizeInventory() ? ItemStack.EMPTY : this.parent.getStackInSlot(index);
	}

	public String getCommandSenderName()
	{
		return "container.crafting";
	}

	@Override
	public boolean hasCustomName()
	{
		return false;
	}

	@Nonnull
	public ItemStack getStackInSlotOnClosing(int index)
	{
		return ItemStack.EMPTY;
	}

	@Nonnull
	@Override
	public ItemStack decrStackSize(int index, int count)
	{
		if(!this.getStackInSlot(index).isEmpty())
		{
			ItemStack itemstack;

			if(this.getStackInSlot(index).getCount() <= count)
			{
				itemstack = this.getStackInSlot(index);
				this.setInventorySlotContents(index, ItemStack.EMPTY);
				return itemstack;
			} else
			{
				itemstack = this.getStackInSlot(index).splitStack(count);

				if(this.getStackInSlot(index).getCount() == 0)
				{
					this.setInventorySlotContents(index, ItemStack.EMPTY);
				}

				onCraftMatrixChanged();
				return itemstack;
			}
		} else
		{
			return ItemStack.EMPTY;
		}
	}

	@Override
	public void setInventorySlotContents(int index, @Nonnull ItemStack stack)
	{
		this.parent.setStackInSlot(index, stack);
		onCraftMatrixChanged();
	}

	@Override
	public void markDirty()
	{
		this.tile.markDirty();
	}

	@Override
	public void clear() {
	}

	public void setDoNotCallUpdates(boolean doNotCallUpdates)
	{
		this.doNotCallUpdates = doNotCallUpdates;
	}

	public void onCraftMatrixChanged()
	{
		if(!doNotCallUpdates)
		{
			this.eventHandler.onCraftMatrixChanged(this);
		}
	}
}

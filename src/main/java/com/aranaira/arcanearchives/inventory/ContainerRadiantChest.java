package com.aranaira.arcanearchives.inventory;

import com.aranaira.arcanearchives.tileentities.RadiantChestTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class ContainerRadiantChest extends Container
{
	private RadiantChestTileEntity tile;

	public ContainerRadiantChest(RadiantChestTileEntity tile, IInventory playerInventory)
	{
		this.tile = tile;

		IItemHandler handler = tile.getInventory();

		for(int y = 5; y > -1; y--)
		{
			for(int x = 8; x > -1; x--)
			{
				this.addSlotToContainer(new SlotItemHandler(handler, 9 * y + x, x * 18 + 16, y * 18 + 16));
			}
		}

		//Creates the slots for the players inventory.
		int i = 35;
		//inventory.
		for(int y = 2; y > -1; y--)
		{
			for(int x = 8; x > -1; x--)
			{
				this.addSlotToContainer(new Slot(playerInventory, i, 16 + (18 * x), 142 + (18 * y)));

				i--;
			}
		}
		//Hotbar.
		for(int x = 8; x > -1; x--)
		{
			this.addSlotToContainer(new Slot(playerInventory, i, 16 + (18 * x), 200));
			i--;
		}
	}

	public String getName()
	{
		return tile.getChestName();
	}

	public void setName(String name)
	{
		tile.setChestName(name);
	}

	@Override
	public boolean canInteractWith(@Nonnull EntityPlayer playerIn)
	{
		return true;
	}

	@Override
	@Nonnull
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		ItemStack stack = ItemStack.EMPTY;
		final Slot slot = inventorySlots.get(index);

		if(slot != null && slot.getHasStack())
		{
			final ItemStack slotStack = slot.getStack();
			stack = slotStack.copy();

			//Chest inventory
			if(index < 54)
			{
				if(!mergeItemStack(slotStack, 54, 90, true)) return ItemStack.EMPTY;
			}
			//Players inventory
			else
			{
				if(!mergeItemStack(slotStack, 0, 54, true)) return ItemStack.EMPTY;
			}

			if(slotStack.isEmpty())
			{
				slot.putStack(ItemStack.EMPTY);
			} else
			{
				slot.onSlotChanged();
			}
		}

		return stack;
	}
}

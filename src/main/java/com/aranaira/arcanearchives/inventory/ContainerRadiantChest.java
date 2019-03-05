package com.aranaira.arcanearchives.inventory;

import com.aranaira.arcanearchives.tileentities.RadiantChestTileEntity;
import invtweaks.api.container.ChestContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

@ChestContainer(isLargeChest = true)
public class ContainerRadiantChest extends Container
{
	private RadiantChestTileEntity tile;

	public ContainerRadiantChest(RadiantChestTileEntity tile, IInventory playerInventory)
	{
		this.tile = tile;

		IItemHandler handler = tile.getInventory();

		for(int j = 0; j < 6; ++j)
		{
			for(int k = 0; k < 9; ++k)
			{
				this.addSlotToContainer(new SlotItemHandler(handler, k + j * 9, 16 + k * 18, 16 + j * 18));
			}
		}

		for(int l = 0; l < 3; ++l)
		{
			for(int j1 = 0; j1 < 9; ++j1)
			{
				this.addSlotToContainer(new Slot(playerInventory, j1 + l * 9 + 9, 16 + j1 * 18, 142 + l * 18));
			}
		}

		for(int i1 = 0; i1 < 9; ++i1)
		{
			this.addSlotToContainer(new Slot(playerInventory, i1, 16 + i1 * 18, 200));
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

package com.aranaira.arcanearchives.inventory;

import com.aranaira.arcanearchives.inventory.slots.SlotRecipeHandler;
import com.aranaira.arcanearchives.registry.crafting.GemCuttersTableRecipe;
import com.aranaira.arcanearchives.tileentities.GemCuttersTableTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class ContainerGemCuttersTable extends Container
{
	private GemCuttersTableTileEntity mTileEntity;
	private boolean isServer;
	private IInventory playerInventory;

	public ContainerGemCuttersTable(GemCuttersTableTileEntity GCTTE, IInventory playerInventory, boolean serverSide)
	{
		mTileEntity = GCTTE;
		isServer = serverSide;
		this.playerInventory = playerInventory;

		//player inventory
		int i = 35;
		//Inventory
		for(int y = 2; y > -1; y--)
		{
			for(int x = 8; x > -1; x--)
			{
				this.addSlotToContainer(new Slot(playerInventory, i, 23 + (18 * x), 166 + (18 * y)));
				i--;
			}
		}
		//hotbar
		for(int x = 8; x > -1; x--)
		{
			this.addSlotToContainer(new Slot(playerInventory, i, 23 + (18 * x), 224));
			i--;
		}

		IItemHandler GCTTE_IH = GCTTE.getInventory();

		//crafting output - 0
		this.addSlotToContainer(new SlotItemHandler(GCTTE_IH, 18, 95, 18));

		//selector - 1 - 8
		{
			int y = 0;

			for(int x = 6; x > -1; x--)
			{
				this.addSlotToContainer(new SlotRecipeHandler(x, x * 18 + 41, y * 18 + 70, GCTTE));
			}
		}

		i = 17;
		for(int y = 1; y > -1; y--)
		{
			for(int x = 8; x > -1; x--)
			{
				this.addSlotToContainer(new SlotItemHandler(GCTTE_IH, i, x * 18 + 23, y * 18 + 105));
				i--;
			}
		}
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
			if(index < 36)
			{
				if(!mergeItemStack(slotStack, 45, 62, true)) return ItemStack.EMPTY;
			}
			//Players inventory
			else
			{
				if(!mergeItemStack(slotStack, 0, 36, true)) return ItemStack.EMPTY;
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

	@Override
	protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection)
	{
		boolean temp = super.mergeItemStack(stack, startIndex, endIndex, reverseDirection);
		this.getTile().updateOutput();
		return temp;
	}

	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
	}

	@Override
	@Nonnull
	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player)
	{
		if(!player.world.isRemote)
		{
			if(slotId <= 43 && slotId >= 37)
			{
				this.getTile().setRecipe(getSlot(slotId).getStack());
				this.getTile().updateOutput();
				return ItemStack.EMPTY;
			}
		}

		return super.slotClick(slotId, dragType, clickTypeIn, player);
	}

	public GemCuttersTableTileEntity getTile()
	{
		return mTileEntity;
	}

	public boolean getRecipeStatus()
	{
		GemCuttersTableRecipe recipe = getTile().getRecipe();
		if(recipe == null) return false;

		NonNullList<ItemStack> raw = NonNullList.create();
		ItemStackHandler inventory = getTile().getInventory();
		for(int i = 0; i < inventory.getSlots(); i++)
		{
			raw.add(inventory.getStackInSlot(i));
		}

		for(int i = 0; i < playerInventory.getSizeInventory(); i++)
		{
			raw.add(playerInventory.getStackInSlot(i));
		}

		return recipe.matchesRecipe(raw);
	}
}

package com.aranaira.arcanearchives.common;

import java.util.ArrayList;
import java.util.Collections;

import com.aranaira.arcanearchives.ArcaneArchives;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

public class GCTItemHandler extends ItemStackHandler 
{
	NonNullList<ItemStack> GCTInventory;
	GemCuttersTableRecipe mRecipe = null;
	boolean mIsRecipeMet = false;
	
	public GCTItemHandler(int slotCount)
	{
		super(slotCount);
		GCTInventory = NonNullList.withSize(18, ItemStack.EMPTY);
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) 
	{
		if (slot >= 18)
		{
			return stack;
		}
		ItemStack s = super.insertItem(slot, stack, simulate);
		updateOutput();
		return s;
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) 
	{
		if (slot >= 18 && slot != 25)
		{
			return ItemStack.EMPTY;
		}
		if (slot == 25)
		{
			if (mRecipe == null || !mIsRecipeMet)
				return ItemStack.EMPTY;
			NonNullList<ItemStack> tempList = NonNullList.create();
			for (int i = 0; i < 18; i++)
			{
				tempList.add(getStackInSlot(i));
			}
			mRecipe.consumeInput(GCTInventory);
			updateOutput();
			return super.extractItem(slot, amount, simulate);
		}
		ItemStack s = super.extractItem(slot, amount, simulate);
		updateOutput();
		return s;
	}
	
	@Override
	public ItemStack getStackInSlot(int slot) {
		if (slot >= 18 && slot != 25)
		{
			if (slot - 17 <= GemCuttersTableRecipe.RecipeList.size())
				return Collections.list(GemCuttersTableRecipe.RecipeList.keys()).get(slot - 18);
			return ItemStack.EMPTY;
		}
		return super.getStackInSlot(slot);
	}
	
	private void updateOutput()
	{
		if (mRecipe != null)
		{
			ArcaneArchives.logger.info(mRecipe.mOutput.getDisplayName());
			NonNullList<ItemStack> tempList = NonNullList.create();
			for (int i = 0; i < 18; i++)
			{
				tempList.add(getStackInSlot(i));
			}
			if (mRecipe.matchesRecipe(tempList))
			{
				this.stacks.set(25, mRecipe.mOutput.copy());
				mIsRecipeMet = true;
			}
			else
			{
				this.stacks.set(25, ItemStack.EMPTY);
				mIsRecipeMet = false;
			}
		}
	}
}

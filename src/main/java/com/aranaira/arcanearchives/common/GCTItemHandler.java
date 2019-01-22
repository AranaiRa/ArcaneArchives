package com.aranaira.arcanearchives.common;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;

public class GCTItemHandler extends ItemStackHandler
{
	NonNullList<ItemStack> GCTInventory;
	public GemCuttersTableRecipe mRecipe = null;
	boolean mIsRecipeMet = false;
	int mPageNumber = 0;
	public boolean isServer = false;


	public GCTItemHandler(int slotCount)
	{
		super(slotCount);
		GCTInventory = NonNullList.withSize(18, ItemStack.EMPTY);
	}

	public boolean getRecipeStatus()
	{
		return mIsRecipeMet;
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
	{
		if(slot >= 18)
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
		if(slot >= 18 && slot != 25)
		{
			return ItemStack.EMPTY;
		}
		if(slot == 25)
		{
			if(mRecipe == null || !mIsRecipeMet) return ItemStack.EMPTY;
			NonNullList<ItemStack> tempList = NonNullList.create();
			for(int i = 0; i < 18; i++)
			{
				tempList.add(getStackInSlot(i));
			}
			if(!simulate) mRecipe.consumeInput(tempList);
			ItemStack s = super.extractItem(slot, amount, simulate);
			updateOutput();
			return s;
		}
		ItemStack s = super.extractItem(slot, amount, simulate);
		updateOutput();
		return s;
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		if(slot >= 18 && slot != 25)
		{
			if(slot - 17 + mPageNumber * 7 <= GemCuttersTableRecipe.RecipeList.size())
				return (ItemStack) (new ArrayList(GemCuttersTableRecipe.RecipeList.keySet())).get(slot - 18 + mPageNumber * 7);
			return ItemStack.EMPTY;
		}
		return super.getStackInSlot(slot);
	}

	public void nextPage()
	{
		if(GemCuttersTableRecipe.RecipeList.size() > (mPageNumber + 1) * 7) mPageNumber++;
	}

	public void prevPage()
	{
		if(mPageNumber > 0) mPageNumber--;
	}

	void updateOutput()
	{
		if(mRecipe != null)
		{
			NonNullList<ItemStack> tempList = NonNullList.create();
			for(int i = 0; i < 18; i++)
			{
				tempList.add(getStackInSlot(i));
			}
			if(mRecipe.matchesRecipe(tempList))
			{
				this.stacks.set(25, mRecipe.getOutput());
				mIsRecipeMet = true;
			} else
			{
				this.stacks.set(25, ItemStack.EMPTY);
				mIsRecipeMet = false;
			}
		}
	}

	public int getPage()
	{
		return mPageNumber;
	}

	public void setPage(int page)
	{
		mPageNumber = page;
	}

	public void setRecipe(ItemStack itemStack)
	{
		mRecipe = GemCuttersTableRecipe.GetRecipe(itemStack);
		updateOutput();
	}
}

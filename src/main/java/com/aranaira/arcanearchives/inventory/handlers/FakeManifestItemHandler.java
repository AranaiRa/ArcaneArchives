package com.aranaira.arcanearchives.inventory.handlers;

import com.aranaira.arcanearchives.util.types.ManifestEntry;
import com.aranaira.arcanearchives.util.types.ManifestList;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class FakeManifestItemHandler extends ManifestItemHandler
{
	public FakeManifestItemHandler()
	{
		super(new ManifestList());
	}

	@Override
	public int getSlots()
	{
		return 81;
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return ItemStack.EMPTY;
	}

	@Nullable
	public ManifestEntry getManifestEntryInSlot(int slot)
	{
		return null;
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
	{
		return stack;
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate)
	{
		return ItemStack.EMPTY;
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return 0;
	}

	@Override
	public void setStackInSlot(int slot, ItemStack stack)
	{
	}

	@Override
	public void setSearchText(String s)
	{
	}

	@Override
	public String getSearchText()
	{
		return "";
	}

	@Override
	public void clear()
	{
	}
}

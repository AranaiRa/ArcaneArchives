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
public class ManifestItemHandler implements IItemHandlerModifiable
{
	private ManifestList manifestBase;
	private ManifestList manifestActive = null;

	public ManifestItemHandler(ManifestList manifest)
	{
		this.manifestBase = manifest;
	}

	@Override
	public int getSlots()
	{
		return 81;
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		if(manifestActive == null) manifestActive = manifestBase.filtered();
		return manifestActive.getItemStackForSlot(slot);
	}

	@Nullable
	public ManifestEntry getManifestEntryInSlot(int slot)
	{
		if(manifestActive == null) manifestActive = manifestBase.filtered();
		return manifestActive.getEntryForSlot(slot);
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

	public String getSearchText()
	{
		return manifestBase.getSearchText();
	}

	public void setSearchText(String s)
	{
		manifestBase.setSearchText(s);
		manifestActive = manifestBase.filtered();
	}

	public void clear()
	{
		manifestBase.setSearchText(null);
		manifestActive = manifestBase.filtered();
	}

	public void nullify ()
	{
		manifestActive = null;
	}

}

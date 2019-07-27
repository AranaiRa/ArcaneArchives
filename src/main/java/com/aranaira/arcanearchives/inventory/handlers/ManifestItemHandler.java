package com.aranaira.arcanearchives.inventory.handlers;

import com.aranaira.arcanearchives.types.lists.ManifestList;
import com.aranaira.arcanearchives.types.lists.ManifestList.SortingDirection;
import com.aranaira.arcanearchives.types.lists.ManifestList.SortingType;
import com.aranaira.arcanearchives.util.ManifestUtils.CollatedEntry;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ManifestItemHandler implements IItemHandlerModifiable {
	private ManifestList manifestBase;
	private ManifestList manifestActive = null;
	private int numSlots;

	public ManifestItemHandler (ManifestList manifest) {
		this.manifestBase = manifest;
		this.numSlots = 81;
	}

	private void updateManifest () {
		if (manifestActive == null) {
			manifestActive = manifestBase.sorted().filtered();
		}
	}

	@Override
	public int getSlots () {
		return numSlots;
	}

	public void setSlots (int numSlots) {
		this.numSlots = numSlots;
	}

	@Override
	public ItemStack getStackInSlot (int slot) {
		updateManifest();
		return manifestActive.getItemStackForSlot(slot);
	}

	@Override
	public ItemStack insertItem (int slot, ItemStack stack, boolean simulate) {
		return stack;
	}

	@Override
	public ItemStack extractItem (int slot, int amount, boolean simulate) {
		return ItemStack.EMPTY;
	}

	@Override
	public int getSlotLimit (int slot) {
		return 0;
	}

	@Nullable
	public CollatedEntry getManifestEntryInSlot (int slot) {
		updateManifest();
		return manifestActive.getEntryForSlot(slot);
	}

	@Override
	public void setStackInSlot (int slot, ItemStack stack) {
	}

	public String getSearchText () {
		return manifestBase.getSearchText();
	}

	public ItemStack getSearchItem () {
		return manifestBase.getSearchItem();
	}

	public void setSearchText (String s) {
		manifestBase.setSearchText(s);
		manifestActive = manifestBase.sorted().filtered();
	}

	public void setSearchItem (ItemStack s) {
		manifestBase.setSearchItem(s);
		manifestActive = manifestBase.sorted().filtered();
	}

	public void clear () {
		manifestBase.setSearchText(null);
		manifestBase.setSearchItem(ItemStack.EMPTY);
		manifestActive = manifestBase.sorted().filtered();
	}

	public void nullify () {
		manifestActive = null;
	}

	public SortingDirection getSortingDirection () {
		if (manifestActive != null) {
			return manifestActive.getSortingDirection();
		}
		return manifestBase.getSortingDirection();
	}

	public void setSortingDirection (SortingDirection sortingDirection) {
		manifestBase.setSortingDirection(sortingDirection);
		manifestActive = manifestBase.sorted().filtered();
	}

	public SortingType getSortingType () {
		if (manifestActive != null) {
			return manifestActive.getSortingType();
		}
		return manifestBase.getSortingType();
	}

	public void setSortingType (SortingType sortingType) {
		manifestBase.setSortingType(sortingType);
		manifestActive = manifestBase.sorted().filtered();
	}
}

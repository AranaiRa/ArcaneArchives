package com.aranaira.arcanearchives.common;

import com.aranaira.arcanearchives.util.ItemComparison;
import com.aranaira.arcanearchives.util.ItemStackConsolidator;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import scala.xml.dtd.EMPTY;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

//Referenced Immersive Engineering
//https://github.com/BluSunrize/ImmersiveEngineering/blob/e3e8cf65dadb2762cb343cc5f31d9bf9a29f2188/src/main/java/blusunrize/immersiveengineering/api/crafting/BlueprintCraftingRecipe.java#L3
public class GemCuttersTableRecipe
{
	public static final GemCuttersTableRecipe EMPTY = new GemCuttersTableRecipe(new ArrayList<>(), ItemStack.EMPTY);

	private ItemStack mOutput;
	private List<ItemStack> mInput;

	// Use ItemStack.EMPTY over null.
	public GemCuttersTableRecipe(@Nonnull List<ItemStack> input, @Nonnull ItemStack output)
	{
		// Make sure we're keeping copies of things
		// Also, set this up so we can sanitize it later if we need to stet values to one
		// although I believe a recipe can contain more than one of an item?
		mInput = input.stream().map(ItemStack::copy).collect(Collectors.toList());
		mOutput = output.copy();
	}

	public boolean isEmpty () {
		return this == EMPTY;
	}

	public boolean matchesRecipe(NonNullList<ItemStack> raw)
	{
		if (isEmpty()) return false;

		List<ItemStack> available = ItemStackConsolidator.ConsolidatedItems(raw);
		List<ItemStack> requirements = getInput();

		Set<Integer> usedIndexes = new HashSet<>();

		int foundItems = 0;

		// Praise be to the lord that these requirements are always of single counts
		for(ItemStack requirement : requirements)
		{
			for(int i = 0; i < available.size(); i++)
			{
				ItemStack avail = available.get(i);
				if(usedIndexes.contains(i)) continue;
				if(ItemComparison.AreItemsEqual(requirement, avail))
				{
					if(requirement.getCount() == 1)
					{
						usedIndexes.add(i);
					} else
					{
						requirement.setCount(requirement.getCount() - 1);
					}
					usedIndexes.add(i);
					foundItems++;
				}
			}
		}
		return foundItems == requirements.size();
	}
	@Nonnull
	public ItemStack getOutput()
	{
		if (mOutput == null || mOutput.isEmpty()) return ItemStack.EMPTY;

		return mOutput.copy();
	}

	public List<ItemStack> getInput()
	{
		return new ArrayList<>(mInput);
	}

	// Clarify
	// This could use code from the recipe list
	public void consumeInput(NonNullList<ItemStack> query)
	{
		List<ItemStack> temp = new ArrayList<>();
		for(ItemStack s : mInput)
		{
			temp.add(s.copy());
		}
		for(ItemStack i : query)
		{
			for(ItemStack is : temp)
			{
				if(is.isEmpty()) continue;
				if(ItemComparison.AreItemsEqual(i, is))
				{
					if(i.getCount() >= is.getCount())
					{
						i.shrink(is.getCount());
						is.shrink(is.getCount());
					} else
					{
						int x = i.getCount();
						i.shrink(x);
						is.shrink(x);
					}
				}
			}
		}
	}
}

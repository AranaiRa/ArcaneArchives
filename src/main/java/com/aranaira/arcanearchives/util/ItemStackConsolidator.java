package com.aranaira.arcanearchives.util;

import com.aranaira.arcanearchives.util.types.ManifestEntry;
import com.aranaira.arcanearchives.util.types.Turple;
import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ItemStackConsolidator
{
	@Deprecated
	public static NonNullList<ItemStack> ConsolidateItems(NonNullList<ItemStack> list)
	{
		NonNullList<ItemStack> tempList = NonNullList.create();

		for(ItemStack s : list)
		{
			boolean added = false;
			for(ItemStack s2 : tempList)
			{
				if(ItemComparison.AreItemsEqual(s, s2))
				{
					s2.setCount(s2.getCount() + s.getCount());
					added = true;
				}
			}
			if(added) continue;
			tempList.add(s.copy());
		}

		return tempList;
	}

	public static List<ItemStack> ConsolidatedItems(NonNullList<ItemStack> list)
	{
		List<ItemStack> input = new ArrayList<>(list);
		List<ItemStack> output = new ArrayList<>();

		if(input.size() == 0) return output;

		while(input.size() != 0)
		{
			ItemStack next = input.remove(0).copy();
			final ItemStack copy = next.copy();

			List<ItemStack> matches = input.stream().filter((i) -> ItemComparison.AreItemsEqual(copy, i)).collect(Collectors.toList());

			if(matches.size() == 0)
			{
				output.add(next.copy());
				continue;
			}

			input.removeAll(matches);

			for(ItemStack match : matches)
			{
				if((next.getCount() + match.getCount()) > next.getMaxStackSize())
				{
					output.add(next.copy());
					next = match.copy();
					continue;
				}

				next.setCount(next.getCount() + match.getCount());
			}

			output.add(next);
		}

		return output;
	}

	public static List<ManifestEntry> ConsolidateManifest(List<Turple<ItemStack, Integer, BlockPos>> input)
	{
		// It doesns't matter if we modify the original list
		List<ManifestEntry> output = new ArrayList<>();

		if(input.size() == 0) return output;

		while(input.size() != 0)
		{
			Turple<ItemStack, Integer, BlockPos> tup = input.remove(0);
			ManifestEntry next = new ManifestEntry(tup.val1, tup.val2, Lists.newArrayList(tup.val3));
			final ItemStack copy = tup.val1.copy();
			final int copy2 = tup.val2;

			List<Turple<ItemStack, Integer, BlockPos>> matches = input.stream().filter((i) -> ItemComparison.AreItemsEqual(i.val1, copy) && i.val2 == copy2).collect(Collectors.toList());

			input.removeAll(matches);

			for(Turple<ItemStack, Integer, BlockPos> match : matches)
			{
				next.val1.setCount(next.val1.getCount() + match.val1.getCount());
				next.val3.add(match.val3);
			}

			output.add(next);
		}

		return output;
	}
}

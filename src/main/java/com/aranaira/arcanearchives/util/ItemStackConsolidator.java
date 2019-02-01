package com.aranaira.arcanearchives.util;

import com.aranaira.arcanearchives.util.types.Tuple;
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

	public static List<ItemStack> ConsolidatedItems (NonNullList<ItemStack> list) {
		List<ItemStack> input = new ArrayList<>(list);
		List<ItemStack> output = new ArrayList<>();

		if (input.size() == 0) return output;

		while (input.size() != 0) {
			ItemStack next = input.remove(0).copy();
			final ItemStack copy = next.copy();

			List<ItemStack> matches = input.stream().filter((i) -> ItemComparison.AreItemsEqual(copy, i)).collect(Collectors.toList());

			if (matches.size() == 0) {
				output.add(next.copy());
				continue;
			}

			input.removeAll(matches);

			for (ItemStack match : matches) {
				if ((next.getCount()+match.getCount()) > next.getMaxStackSize()) {
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

		public static List<Tuple<ItemStack, List<BlockPos>>> TupleConsolidatedItems (List<Tuple<ItemStack, BlockPos>> list) {
		List<Tuple<ItemStack, BlockPos>> input = new ArrayList<>(list);
		List<Tuple<ItemStack, List<BlockPos>>> output = new ArrayList<>();

		if (input.size() == 0) return output;

		while (input.size() != 0) {
			Tuple<ItemStack, BlockPos> tup = input.remove(0);
			Tuple<ItemStack, List<BlockPos>> next = new Tuple<>(tup.val1, Lists.newArrayList(tup.val2));
			final ItemStack copy = tup.val1.copy();

			List<Tuple<ItemStack, BlockPos>> matches = input.stream().filter((i) -> ItemComparison.AreItemsEqual(copy, i.val1)).collect(Collectors.toList());

			if (matches.size() == 0) {
				output.add(next);
				continue;
			}

			input.removeAll(matches);

			for (Tuple<ItemStack, BlockPos> match : matches) {
				if ((next.val1.getCount()+match.val1.getCount()) > next.val1.getMaxStackSize()) {
					output.add(next);
					next = new Tuple<>(match.val1, Lists.newArrayList(match.val2));
					continue;
				}

				next.val1.setCount(next.val1.getCount() + match.val1.getCount());
				next.val2.add(match.val2);
			}

			output.add(next);
		}

		return output;
	}
}

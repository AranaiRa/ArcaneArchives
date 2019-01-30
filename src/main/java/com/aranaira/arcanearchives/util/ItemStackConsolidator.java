package com.aranaira.arcanearchives.util;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ItemStackConsolidator
{
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

	public static class Workaround<T>
	{
		T term;
		BiFunction<T, T, Boolean> comparator = null;

		Workaround (T term, BiFunction<T, T, Boolean> comparator) {
			this.term = term;
			this.comparator = comparator;
		}

		Workaround(T term)
		{
			this.term = term;
		}

		public boolean matches(T otherTerm)
		{
			if (this.comparator != null) {
				return comparator.apply(this.term, otherTerm);
			}
			return this.term == otherTerm;
		}

		public void update (T newTerm)
		{
			this.term = newTerm;
		}

		public T getTerm () {
			return this.term;
		}
	}
}

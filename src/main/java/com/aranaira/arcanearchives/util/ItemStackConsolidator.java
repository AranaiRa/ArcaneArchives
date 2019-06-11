package com.aranaira.arcanearchives.util;

import com.aranaira.arcanearchives.util.types.ManifestEntry;
import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.aranaira.arcanearchives.util.types.ManifestItemEntry;
import scala.collection.immutable.Stream.Cons;

public class ItemStackConsolidator {
	@Deprecated
	public static NonNullList<ItemStack> ConsolidateItems (NonNullList<ItemStack> list) {
		NonNullList<ItemStack> tempList = NonNullList.create();

		for (ItemStack s : list) {
			boolean added = false;
			for (ItemStack s2 : tempList) {
				if (ItemComparison.areStacksEqualIgnoreSize(s, s2)) {
					s2.setCount(s2.getCount() + s.getCount());
					added = true;
				}
			}
			if (added) {
				continue;
			}
			tempList.add(s.copy());
		}

		return tempList;
	}

	public static List<ItemStack> ConsolidatedItems (NonNullList<ItemStack> list) {
		List<ItemStack> input = new ArrayList<>(list);
		List<ItemStack> output = new ArrayList<>();

		if (input.size() == 0) {
			return output;
		}

		while (input.size() != 0) {
			ItemStack next = input.remove(0).copy();
			final ItemStack copy = next.copy();

			List<ItemStack> matches = input.stream().filter((i) -> ItemComparison.areStacksEqualIgnoreSize(copy, i)).collect(Collectors.toList());

			if (matches.size() == 0) {
				output.add(next.copy());
				continue;
			}

			input.removeAll(matches);

			for (ItemStack match : matches) {
				if ((next.getCount() + match.getCount()) > next.getMaxStackSize()) {
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

	public static List<ManifestEntry> ConsolidateManifest (List<ManifestItemEntry> input) {
		return ConsolidateManifest(input, -1);
	}

	public static List<ManifestEntry> ConsolidateManifest (List<ManifestItemEntry> input, int maxDistance) {
		List<ManifestEntry> output = new ArrayList<>();

		if (input.size() == 0) {
			return output;
		}

		while (input.size() != 0) {
			ManifestItemEntry tup = input.remove(0);
			ManifestEntry next = new ManifestEntry(tup.stack, tup.dim, Lists.newArrayList(tup.entry), tup.outOfRange);
			final ItemStack copy = tup.stack.copy();
			final int copy2 = tup.dim;

			List<ManifestItemEntry> matches = input.stream().filter((i) -> ItemComparison.areStacksEqualIgnoreSize(i.stack, copy) && i.dim == copy2).collect(Collectors.toList());

			input.removeAll(matches);

			for (ManifestItemEntry match : matches) {
				next.stack.setCount(next.stack.getCount() + match.stack.getCount());
				next.itemEntries.add(match.entry);
			}

			output.add(next);
		}

		return output;
	}
}

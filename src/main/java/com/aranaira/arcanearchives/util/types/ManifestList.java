package com.aranaira.arcanearchives.util.types;

import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static com.aranaira.arcanearchives.common.ManifestItemHandler.ManifestEntry;

public class ManifestList extends ReferenceList<ManifestEntry>
{
	String mFilterText;

	public ManifestList()
	{
		super(new ArrayList<>());
	}

	public ManifestList(List<ManifestEntry> reference)
	{
		super(reference);
		this.mFilterText = null;
	}

	public ManifestList(List<ManifestEntry> reference, String filterText)
	{
		super(reference);
		this.mFilterText = filterText;
	}

	public ManifestList filtered()
	{
		if (mFilterText == null) return this;

		return reference().stream().filter((entry) -> entry != null && entry.getStack().getDisplayName().compareToIgnoreCase(mFilterText) != 0).collect(Collectors.toCollection(ManifestList::new));

	}

	@Nullable
	public ManifestEntry getEntryForSlot (int slot) {
		if (slot < reference().size() && slot > 0) return reference().get(slot);
		return null;
	}

	public ItemStack getItemStackForSlot (int slot) {
		if (slot < reference().size() && slot > 0) return reference().get(slot).getStack();
		return ItemStack.EMPTY;
	}

	public void setSearchText(String searchTerm)
	{
		this.mFilterText = searchTerm;
	}

	@Override
	public ManifestListIterable iterable()
	{
		return new ManifestListIterable(new ManifestIterator(iterator()));
	}

	public ManifestList sorted(Comparator<ManifestEntry> c)
	{
		ManifestList copy = new ManifestList(new ArrayList<>(), null);
		copy.addAll(this.reference());
		copy.sort(c);
		return copy;
	}

	public class ManifestListIterable extends ReferenceListIterable<ManifestEntry>
	{
		ManifestListIterable(ManifestIterator iter)
		{
			super(iter);
		}

		public int getSlot()
		{
			return ((ManifestIterator) iter).getSlot();
		}
	}

	public class ManifestIterator implements Iterator<ManifestEntry>
	{
		private int slot = 0;
		private Iterator<ManifestEntry> iter;

		public ManifestIterator(Iterator<ManifestEntry> iter)
		{
			this.iter = iter;
		}

		public int getSlot()
		{
			return slot;
		}

		@Override
		public boolean hasNext()
		{
			return iter.hasNext();
		}

		@Override
		public ManifestEntry next()
		{
			slot++;
			return iter.next();
		}
	}
}

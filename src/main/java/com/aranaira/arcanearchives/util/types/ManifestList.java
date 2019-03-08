package com.aranaira.arcanearchives.util.types;

import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class ManifestList extends ReferenceList<ManifestEntry>
{
	private String mFilterText;

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
		if(mFilterText == null) return this;

		String filter = mFilterText.toLowerCase();

		return stream().filter((entry) ->
		{
			if(entry == null) return false;

			ItemStack stack = entry.getStack();
			String display = stack.getDisplayName().toLowerCase();
			String resource = stack.getItem().getRegistryName().toString().toLowerCase();

			return display.contains(filter) || resource.contains(filter);
		}).collect(Collectors.toCollection(ManifestList::new));

	}

	@Nullable
	public ManifestEntry getEntryForSlot(int slot)
	{
		if(slot < size() && slot >= 0) return get(slot);
		return null;
	}

	public ItemStack getItemStackForSlot(int slot)
	{
		if(slot < size() && slot >= 0) return get(slot).getStack();
		return ItemStack.EMPTY;
	}

	public String getSearchText()
	{
		return this.mFilterText;
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
		copy.addAll(this);
		copy.sort(c);
		return copy;
	}

	@Override
	public void clear()
	{
		super.clear();
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

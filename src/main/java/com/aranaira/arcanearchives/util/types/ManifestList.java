package com.aranaira.arcanearchives.util.types;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import static com.aranaira.arcanearchives.common.ManifestItemHandler.ManifestEntry;

public class ManifestList extends ReferenceList<ManifestEntry>
{
	String mFilterText;

	public ManifestList (List<ManifestEntry> reference) {
		super(reference);
		this.mFilterText = null;
	}

	public ManifestList(List<ManifestEntry> reference, String filterText)
	{
		super(reference);
		this.mFilterText = filterText;
	}

	public ManifestListIterable filtered ()
	{
		return new ManifestListIterable(new FilterIterator(mFilterText));
	}

	public ManifestListIterable setSearchText (String searchTerm) {
		this.mFilterText = searchTerm;
		if (searchTerm != null)
		{
			return filtered();
		}
		else
		{
			return iterable();
		}
	}

	@Override
	public ManifestListIterable iterable ()
	{
		return new ManifestListIterable(iterator());
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
		ManifestListIterable(Iterator<ManifestEntry> iter)
		{
			super(iter);
		}

		ManifestListIterable(FilterIterator iter)
		{
			super(iter);
		}

		public int getSlot()
		{
			if(iter instanceof FilterIterator)
			{
				return ((FilterIterator) iter).getSlot();
			} else
			{
				return -1;
			}
		}

		public ManifestEntry translate(int slot)
		{
			if(iter instanceof FilterIterator)
			{
				return ((FilterIterator) iter).translateFiltered(slot);
			}
			else
			{
				return reference().get(slot);
			}
		}
	}

	public class FilterIterator implements ListIterator<ManifestEntry>
	{
		private int slot = 0;
		private int filteredSlot = 0;
		private String filterString;
		private BiFunction<Integer, ManifestEntry, Boolean> comparatorString = (slot, entry) -> (filterString != null && entry.getStack().getDisplayName().compareToIgnoreCase(filterString) != 0);

		public FilterIterator(String filterString)
		{
			this.filterString = filterString;
		}

		public int getSlot()
		{
			return filteredSlot;
		}

		public int getActualSlot()
		{
			return slot;
		}

		public ManifestEntry translateFiltered(int slot)
		{
			return everything(this::_next, this::_hasNext, (curSlot, entry) ->
			{
				if(curSlot == slot) return true;
				return false;
			}, false);
		}

		@Override
		public boolean hasNext()
		{
			return everything(this::_next, this::_hasNext, comparatorString, false) != null;
		}

		public boolean _hasNext()
		{
			return slot < reference().size();
		}

		@Override
		public ManifestEntry next()
		{
			filteredSlot++;
			return everything(this::_next, this::_hasNext, comparatorString, true);
		}

		public ManifestEntry _next()
		{
			return reference().get(slot++);
		}

		@Override
		public boolean hasPrevious()
		{
			return everything(this::_previous, this::_hasPrevious, comparatorString, false) != null;
		}

		public ManifestEntry everything(Supplier<ManifestEntry> supplier, Supplier<Boolean> iter, BiFunction<Integer, ManifestEntry, Boolean> compare, boolean keepSlot)
		{
			int last_slot = slot;
			while(iter.get())
			{
				ManifestEntry entry = supplier.get();
				if(compare.apply(slot, entry))
				{
					if(!keepSlot) slot = last_slot;
					return entry;
				}
			}
			slot = last_slot;
			return null;
		}

		public boolean _hasPrevious()
		{
			return slot != 0;
		}

		@Override
		public ManifestEntry previous()
		{
			filteredSlot--;
			return everything(this::_previous, this::_hasPrevious, comparatorString, true);
		}

		public ManifestEntry _previous()
		{
			return reference().get(slot--);
		}

		@Override
		public int nextIndex()
		{
			if(hasNext()) return filteredSlot + 1;
			else return filteredSlot;
		}

		@Override
		public int previousIndex()
		{
			if(hasPrevious()) return filteredSlot - 1;
			else return 0;
		}

		@Override
		public void remove()
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public void set(ManifestEntry manifestEntry)
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public void add(ManifestEntry manifestEntry)
		{
			throw new UnsupportedOperationException();
		}
	}
}

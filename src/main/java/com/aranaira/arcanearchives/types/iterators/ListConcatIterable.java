package com.aranaira.arcanearchives.types.iterators;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

// This remains as a testament to my inability to function properly
// Yes, addAll is a thing.
public class ListConcatIterable<T> implements Iterable<T> {
	private List<List<T>> lists;
	private Iterator<List<T>> listIterator;
	private List<T> entry = null;
	private Iterator<T> iterator = null;

	public ListConcatIterable (Collection<List<T>> lists) {
		this.lists = lists.stream().filter(t -> !t.isEmpty()).collect(Collectors.toList());
		this.listIterator = this.lists.iterator();
		if (this.listIterator.hasNext()) {
			this.entry = this.listIterator.next();
			this.iterator = this.entry.iterator();
		}
	}

	@Override
	public Iterator<T> iterator () {
		return new ListConcatIterator();
	}

	private void nextIterator () {
		if (this.listIterator.hasNext()) {
			this.entry = this.listIterator.next();
			this.iterator = this.entry.iterator();
		} else {
			this.entry = null;
			this.iterator = null;
		}
	}

	public class ListConcatIterator implements Iterator<T> {

		@Override
		public boolean hasNext () {
			if (entry == null) {
				return false;
			} else {
				if (iterator != null && !iterator.hasNext()) {
					return listIterator.hasNext();
				} else if (iterator != null) {
					return iterator.hasNext();
				} else {
					return false;
				}
			}
		}

		@Override
		public T next () {
			if (iterator == null || !iterator.hasNext()) {
				nextIterator();
			}

			if (iterator == null || !iterator.hasNext()) throw new NoSuchElementException();

			return iterator.next();
		}
	}
}

package com.aranaira.arcanearchives.types.lists;

import com.google.common.collect.ForwardingList;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

public class ReferenceList<T> extends ForwardingList<T> {
	private List<T> reference;

	public ReferenceList () {
		this.reference = new ArrayList<>();
	}

	public ReferenceList (List<T> reference) {
		this.reference = reference;
	}

	public ReferenceList (Supplier<List<T>> supplier) {
		this.reference = supplier.get();
	}

	@Override
	protected List<T> delegate () {
		return reference;
	}

	public Iterable<T> iterable () {
		return new ReferenceListIterable<>(iterator());
	}

	public static class ReferenceListIterable<U> implements Iterable<U> {
		protected Iterator<U> iter;

		public ReferenceListIterable () {
		}

		public ReferenceListIterable (Iterator<U> iter) {
			this.iter = iter;
		}

		@Override
		@Nonnull
		public Iterator<U> iterator () {
			return this.iter;
		}

		public void setIterator (Iterator<U> iterator) {
			this.iter = iterator;
		}
	}
}

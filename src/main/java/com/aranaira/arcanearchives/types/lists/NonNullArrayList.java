package com.aranaira.arcanearchives.types.lists;

import java.util.ArrayList;
import java.util.function.Supplier;

public class NonNullArrayList<T> extends ArrayList<T> {
	private Supplier<T> emptySupplier;

	public NonNullArrayList (Supplier<T> emptySupplier, int initialCapacity) {
		super(initialCapacity);
		this.emptySupplier = emptySupplier;
		ensureCapacity(initialCapacity);
	}

	public NonNullArrayList (Supplier<T> emptySupplier) {
		super(0);
		this.emptySupplier = emptySupplier;
	}

	@Override
	public void ensureCapacity (int minCapacity) {
		super.ensureCapacity(minCapacity);
		int originalSize = size();
		for (int i = originalSize; i < originalSize + minCapacity; i++) {
			this.add(emptySupplier.get());
		}
	}
}

package com.aranaira.arcanearchives.util.types;

import java.util.function.BiFunction;

public class Filter<T>
{
	T term = null;
	BiFunction<T, T, Boolean> comparator = null;

	Filter(T term, BiFunction<T, T, Boolean> comparator) {
		this.term = term;
		this.comparator = comparator;
	}

	Filter(BiFunction <T, T, Boolean> comparator) {
		this.comparator = comparator;
	}

	Filter(T term)
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

	public void updateTerm (T newTerm)
	{
		this.term = newTerm;
	}

	public T getTerm () {
		return this.term;
	}

	public BiFunction<T, T, Boolean> getComparator()
	{
		return comparator;
	}

	public void updateComparator(BiFunction<T, T, Boolean> comparator)
	{
		this.comparator = comparator;
	}
}

package com.aranaira.arcanearchives.util.types;

import java.util.function.BiPredicate;

public class Filter<T>
{
	private T term = null;
	private BiPredicate<T, T> comparator = null;

	Filter(T term, BiPredicate<T, T> comparator)
	{
		this.term = term;
		this.comparator = comparator;
	}

	Filter(BiPredicate<T, T> comparator)
	{
		this.comparator = comparator;
	}

	Filter(T term)
	{
		this.term = term;
	}

	public boolean matches(T otherTerm)
	{
		if(this.comparator != null)
		{
			return comparator.test(this.term, otherTerm);
		}
		return this.term == otherTerm;
	}

	public void updateTerm(T newTerm)
	{
		this.term = newTerm;
	}

	public T getTerm()
	{
		return this.term;
	}

	public BiPredicate<T, T> getComparator()
	{
		return comparator;
	}

	public void updateComparator(BiPredicate<T, T> comparator)
	{
		this.comparator = comparator;
	}
}

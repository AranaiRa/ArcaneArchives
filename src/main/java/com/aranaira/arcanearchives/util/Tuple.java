package com.aranaira.arcanearchives.util;

import javax.annotation.Nonnull;
import java.io.Serializable;

public class Tuple<T, U> implements Serializable
{
	public final T val1;
	public final U val2;

	public Tuple(@Nonnull T val1, @Nonnull U val2)
	{
		this.val1 = val1;
		this.val2 = val2;
	}

	@Override
	public String toString()
	{
		return String.format("%s, %s", val1, val2);
	}

	@Override
	public int hashCode()
	{
		return val1.hashCode() * 13 + val2.hashCode();
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o instanceof Tuple)
		{
			Tuple t = (Tuple) o;
			if(!val1.equals(t.val1)) return false;
			if(!val2.equals(t.val2)) return false;
		}
		return false;
	}
}

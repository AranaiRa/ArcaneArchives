package com.aranaira.arcanearchives.util.types;

import javax.annotation.Nonnull;
import java.io.Serializable;

@SuppressWarnings("WeakerAccess")
public class Turple<T, U, V> implements Serializable
{
	public final T val1;
	public final U val2;
	public final V val3;

	public Turple(@Nonnull T val1, @Nonnull U val2, @Nonnull V val3)
	{
		this.val1 = val1;
		this.val2 = val2;
		this.val3 = val3;
	}

	@Override
	public String toString()
	{
		return String.format("%s, %s, %s", val1, val2, val3);
	}

	@Override
	public int hashCode()
	{
		return val1.hashCode() * val2.hashCode() * val3.hashCode();
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o instanceof Turple)
		{
			Turple t = (Turple) o;
			if(!val1.equals(t.val1)) return false;
			if(!val2.equals(t.val2)) return false;
			return val3.equals(t.val3);
		}
		return false;
	}
}

package com.aranaira.arcanearchives.util;

import com.aranaira.arcanearchives.tileentities.AATileEntity;
import com.google.common.collect.Iterators;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class TileList<T extends AATileEntity> implements Iterable<T>, Collection<T>
{
	private Collection<T> reference;

	public TileList(Collection<T> reference)
	{
		this.reference = reference;
	}

	public Iterator<T> filterDimension(int dimension)
	{
		return Iterators.filter(iterator(), (f) -> f != null && f.getWorld().provider.getDimension() == dimension);
	}

	public Iterator<T> filterDimension(World world)
	{
		return filterDimension(world.provider.getDimension());
	}

	public Iterator<T> filterDimension(Entity entity)
	{
		return filterDimension(entity.dimension);
	}

	public Iterator<T> filterLoaded()
	{
		return Iterators.filter(iterator(), (f) -> f != null && f.getWorld().isBlockLoaded(f.getPos()));
	}

	@Override
	public int size()
	{
		return this.reference.size();
	}

	@Override
	public boolean isEmpty()
	{
		return this.reference.isEmpty();
	}

	@Override
	public boolean contains(Object o)
	{
		return this.reference.contains(o);
	}

	@Override
	public Object[] toArray()
	{
		return this.reference.toArray();
	}

	@Override // TODO: Cheeck
	public <T1> T1[] toArray(T1[] a)
	{
		return this.reference.toArray(a);
	}

	@Override
	public boolean add(T t)
	{
		return this.reference.add(t);
	}

	@Override
	public boolean remove(Object o)
	{
		return this.reference.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c)
	{
		return this.reference.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends T> c)
	{
		return this.reference.addAll(c);
	}

	@Override
	public boolean removeAll(Collection<?> c)
	{
		return this.reference.removeAll(c);
	}

	@Override
	public boolean removeIf(Predicate<? super T> filter)
	{
		return this.reference.removeIf(filter);
	}

	@Override
	public boolean retainAll(Collection<?> c)
	{
		return this.reference.retainAll(c);
	}

	@Override
	public void clear()
	{
		this.reference.clear();
	}

	@Override
	public Iterator<T> iterator()
	{
		return reference.iterator();
	}

	@Override
	public void forEach(Consumer<? super T> action)
	{
		reference.forEach(action);
	}

	@Override
	public Spliterator<T> spliterator()
	{
		return this.reference.spliterator();
	}

	@Override
	public Stream<T> stream()
	{
		return this.reference.stream();
	}

	@Override
	public Stream<T> parallelStream()
	{
		return this.reference.parallelStream();
	}
}

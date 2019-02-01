package com.aranaira.arcanearchives.util;

import com.aranaira.arcanearchives.tileentities.AATileEntity;
import com.google.common.collect.Iterators;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class TileList<T extends AATileEntity> implements Iterable<T>, List<T>
{
	private List<T> reference;

	public TileList () {
		this.reference = new ArrayList<>();
	}

	public TileList(List<T> reference)
	{
		this.reference = reference;
	}

	public TileList(Supplier<List<T>> supplier) {
		this.reference = supplier.get();
	}

	public TileListIterable<T> filterDimension(int dimension)
	{
		return new TileListIterable<>(Iterators.filter(iterator(), (f) -> f != null && f.getWorld().provider.getDimension() == dimension));
	}

	public TileListIterable<T> filterDimension(World world)
	{
		return filterDimension(world.provider.getDimension());
	}

	public TileListIterable<T> filterDimension(Entity entity)
	{
		return filterDimension(entity.dimension);
	}

	public TileListIterable<T> filterLoaded()
	{
		return new TileListIterable<>(Iterators.filter(iterator(), (f) -> f != null && f.getWorld().isBlockLoaded(f.getPos())));
	}

	public TileListIterable<T> filterActive() {
		return new TileListIterable<>(Iterators.filter(iterator(), (f) -> f != null && f.isActive()));
	}

	public TileListIterable<T> filterClass (Class<? extends AATileEntity> clazz) {
		return new TileListIterable<>(Iterators.filter(iterator(), (f) -> f != null && f.getClass().equals(clazz)));
	}

	public TileList<T> cleanInvalid () {
		this.reference.removeIf((f) -> f != null && f.isInvalid());
		return this;
	}

	public TileList<T> sorted (Comparator<? super T> c, Supplier<? extends List<T>> supplier) {
		TileList<T> copy = new TileList<>(supplier.get());
		copy.addAll(this.reference);
		copy.sort(c);
		return copy;
	}

	@Override
	public Iterator<T> iterator()
	{
		return reference.iterator();
	}

	public TileListIterable<T> iterable () {
		return new TileListIterable<>(iterator());
	}

	// Overrides -> Overrides -> OVERRIDES!

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
	public boolean addAll(int index, Collection<? extends T> c)
	{
		return this.reference.addAll(index, c);
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
	public void replaceAll(UnaryOperator<T> operator)
	{
		this.reference.replaceAll(operator);
	}

	@Override
	public void sort(Comparator<? super T> c)
	{
		this.reference.sort(c);
	}

	@Override
	public void clear()
	{
		this.reference.clear();
	}

	@Override
	public T get(int index)
	{
		return this.reference.get(index);
	}

	@Override
	public T set(int index, T element)
	{
		return this.reference.set(index, element);
	}

	@Override
	public void add(int index, T element)
	{
		this.reference.add(index, element);
	}

	@Override
	public T remove(int index)
	{
		return this.reference.remove(index);
	}

	@Override
	public int indexOf(Object o)
	{
		return this.reference.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o)
	{
		return this.reference.lastIndexOf(o);
	}

	@Override
	public ListIterator<T> listIterator()
	{
		return this.reference.listIterator();
	}

	@Override
	public ListIterator<T> listIterator(int index)
	{
		return this.reference.listIterator(index);
	}

	@Override
	public TileList<T> subList(int fromIndex, int toIndex)
	{
		return new TileList<>(this.reference.subList(fromIndex, toIndex));
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

	public static class TileListIterable<T> implements Iterable<T> {

		Iterator<T> iter;

		TileListIterable (Iterator<T> iter) {
			this.iter = iter;
		}

		@Override
		@Nonnull
		public Iterator<T> iterator()
		{
			return this.iter;
		}
	}
}

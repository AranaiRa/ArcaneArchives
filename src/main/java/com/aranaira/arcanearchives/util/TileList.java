package com.aranaira.arcanearchives.util;

import com.aranaira.arcanearchives.tileentities.AATileEntity;
import com.google.common.collect.Iterators;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.Iterator;

public class TileList<T extends AATileEntity> implements Iterable<T>
{
	Collection<T> reference;

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
	public Iterator<T> iterator()
	{
		return reference.iterator();
	}
}

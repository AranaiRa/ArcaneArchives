package com.aranaira.arcanearchives.util.types;

import com.aranaira.arcanearchives.tileentities.AATileEntity;
import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import com.google.common.collect.Iterators;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class TileList extends ReferenceList<ImmanenceTileEntity>
{
	public TileList()
	{
		super(new ArrayList<>());
	}

	public TileList(List<ImmanenceTileEntity> reference)
	{
		super(reference);
	}

	public TileListIterable filterDimension(int dimension)
	{
		return new TileListIterable(Iterators.filter(iterator(), (f) -> f != null && f.getWorld().provider.getDimension() == dimension));
	}

	public TileListIterable filterDimension(World world)
	{
		return filterDimension(world.provider.getDimension());
	}

	public TileListIterable filterDimension(Entity entity)
	{
		return filterDimension(entity.dimension);
	}

	public TileListIterable filterLoaded()
	{
		return new TileListIterable(Iterators.filter(iterator(), (f) -> f != null && f.getWorld().isBlockLoaded(f.getPos())));
	}

	public TileListIterable filterValid()
	{
		return new TileListIterable(Iterators.filter(iterator(), (f) -> f != null && !f.isInvalid()));
	}

	public TileListIterable filterActive()
	{
		return new TileListIterable(Iterators.filter(iterator(), (f) -> f != null && f.isActive()));
	}

	public TileListIterable filterClass(Class<? extends AATileEntity> clazz)
	{
		return new TileListIterable(Iterators.filter(iterator(), (f) -> f != null && f.getClass().equals(clazz)));
	}

	public TileList cleanInvalid()
	{
		this.removeIf((f) -> f != null && f.isInvalid());
		return this;
	}

	public TileList sorted(Comparator<ImmanenceTileEntity> c)
	{
		TileList copy = new TileList(new ArrayList<ImmanenceTileEntity>());
		copy.addAll(this);
		copy.sort(c);
		return copy;
	}

	@Override
	public TileListIterable iterable()
	{
		return new TileListIterable(iterator());
	}

	public class TileListIterable extends ReferenceListIterable<ImmanenceTileEntity>
	{
		TileListIterable(Iterator<ImmanenceTileEntity> iter)
		{
			super(iter);
		}
	}
}

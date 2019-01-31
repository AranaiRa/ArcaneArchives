package com.aranaira.arcanearchives.data;

import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import com.aranaira.arcanearchives.util.AACollectors;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collection;

public class TileList<T extends ImmanenceTileEntity> extends ArrayList<T>
{
	public TileList () {
		super();
	}

	public TileList(Collection<T> c)
	{
		super(c);
	}

	public TileList<T> filterDimension (int dimension) {
		return this.stream().filter((f) -> f.getWorld().provider.getDimension() == dimension).collect(AACollectors.toTileList());
	}

	public TileList<T> filterDimension (World world) {
		return filterDimension(world.provider.getDimension());
	}

	public TileList<T> filterDimension (Entity entity)
	{
		return filterDimension(entity.dimension);
	}
}

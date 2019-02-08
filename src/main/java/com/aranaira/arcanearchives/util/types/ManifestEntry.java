package com.aranaira.arcanearchives.util.types;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ManifestEntry
{
	public ItemStack stack;
	public int dimension;
	public List<BlockPos> positions;
	public List<String> names;
	public Map<BlockPos, String> lookup;

	private boolean tracking = false;

	public ManifestEntry(@Nonnull ItemStack val1, @Nonnull Integer val2, @Nonnull List<BlockPos> val3, @Nonnull List<String> val4)
	{
		this.stack = val1;
		this.dimension = val2;
		this.positions = val3;
		this.names = val4;

		this.lookup = new HashMap<>();

		assert positions.size() == names.size(); // even if the name is empty

		for (int i = 0; i < positions.size(); i++) {
			BlockPos pos = positions.get(i);
			String name = names.get(i);
			if (lookup.containsKey(pos) && lookup.get(pos).equals(name)) continue;

			lookup.put(pos, name);
		}
	}

	public boolean isTracked () {
		return tracking;
	}

	public void track () {
		this.tracking = true;
	}

	public void untrack () {
		this.tracking = false;
	}

	public ItemStack getStack()
	{
		return stack;
	}

	public int getDimension()
	{
		return dimension;
	}

	public List<BlockPos> getPositions()
	{
		return positions;
	}

	public List<Vec3d> getVecPositions()
	{
		return getPositions().stream().map((pos) -> new Vec3d(pos.getX(), pos.getY(), pos.getZ())).collect(Collectors.toList());
	}

	public List<String> getNames () {
		return names;
	}

	public String getPositionNames (BlockPos pos) {
		if (!lookup.containsKey(pos)) return "";
		return lookup.get(pos);
	}
}

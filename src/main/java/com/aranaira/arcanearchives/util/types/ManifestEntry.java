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
	public Map<BlockPos, String> lookup = new HashMap<>();

	public ManifestEntry(@Nonnull ItemStack val1, @Nonnull Integer val2, @Nonnull List<BlockPos> val3, @Nonnull List<String> val4)
	{
		this.stack = val1;
		this.dimension = val2;
		this.positions = val3;
		this.names = val4;

		assert positions.size() == names.size(); // even if the name is empty

		this.lookup = IntStream.range(0, positions.size()).boxed().collect(Collectors.toMap(positions::get, names::get));
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

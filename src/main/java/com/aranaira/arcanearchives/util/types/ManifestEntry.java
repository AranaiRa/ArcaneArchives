package com.aranaira.arcanearchives.util.types;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

public class ManifestEntry extends Turple<ItemStack, Integer, List<BlockPos>>
{

	public ManifestEntry(@Nonnull ItemStack val1, @Nonnull Integer val2, @Nonnull List<BlockPos> val3)
	{
		super(val1, val2, val3);
	}

	public ItemStack getStack()
	{
		return val1;
	}

	public int getDimension()
	{
		return val2;
	}

	public List<BlockPos> getPositions()
	{
		return val3;
	}

	public List<Vec3d> getVecPositions()
	{
		return getPositions().stream().map((pos) -> new Vec3d(pos.getX(), pos.getY(), pos.getZ())).collect(Collectors.toList());
	}
}

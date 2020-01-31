package com.aranaira.arcanearchives.types;

import net.minecraft.util.math.BlockPos;

import java.util.Objects;

/**
 * Specifically pairing a block position with a dimension in order to
 * track the unique whereabouts of a tile or other block without running
 * into cross-dimensional conflicts.
 */
public class BlockPosDimension {
	public BlockPos pos;
	public int dimension;

	public BlockPosDimension (BlockPos pos, int dimension) {
		this.pos = pos;
		this.dimension = dimension;
	}

	public int getX () {
		return pos.getX();
	}

	public int getY () {
		return pos.getY();
	}

	public int getZ () {
		return pos.getZ();
	}

	@Override
	public boolean equals (Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		BlockPosDimension that = (BlockPosDimension) o;
		return dimension == that.dimension && Objects.equals(pos, that.pos);
	}

	@Override
	public int hashCode () {
		return Objects.hash(pos, dimension);
	}

/*	public static BlockPosDimension fromITE (ImmanenceTileEntity ite) {
		return new BlockPosDimension(ite.getPos(), ite.dimension);
	}*/
}

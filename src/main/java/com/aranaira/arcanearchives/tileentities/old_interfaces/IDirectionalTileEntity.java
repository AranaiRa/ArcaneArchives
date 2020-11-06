package com.aranaira.arcanearchives.tileentities.interfaces;

import com.aranaira.arcanearchives.blocks.templates.TemplateBlock;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IDirectionalTileEntity {
	*
 * @return The current facing of the block associated with this TileEntity .
 * Theoretically there are issues with the presumption that all BlockTemplates
 * are also BlockDirectionalTemplates; few are.
 * <p>
 * The default value for this function is Direction.WEST; this may change in
 * the future.

	default Direction getFacing () {
		BlockState state = getWorld().getBlockState(getPos());
		if (state.getBlock() instanceof TemplateBlock) {
			return ((TemplateBlock) state.getBlock()).getFacing(getWorld(), getPos());
		}

		return Direction.WEST;
	}

	World getWorld ();

	BlockPos getPos ();
}

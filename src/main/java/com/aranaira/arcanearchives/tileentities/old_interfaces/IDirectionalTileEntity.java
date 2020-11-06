package com.aranaira.arcanearchives.tileentities.interfaces;

import com.aranaira.arcanearchives.blocks.templates.TemplateBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IDirectionalTileEntity {
	*
 * @return The current facing of the block associated with this TileEntity .
 * Theoretically there are issues with the presumption that all BlockTemplates
 * are also BlockDirectionalTemplates; few are.
 * <p>
 * The default value for this function is EnumFacing.WEST; this may change in
 * the future.

	default EnumFacing getFacing () {
		IBlockState state = getWorld().getBlockState(getPos());
		if (state.getBlock() instanceof TemplateBlock) {
			return ((TemplateBlock) state.getBlock()).getFacing(getWorld(), getPos());
		}

		return EnumFacing.WEST;
	}

	World getWorld ();

	BlockPos getPos ();
}

/*package com.aranaira.arcanearchives.tileentities.old_interfaces;

import com.aranaira.arcanearchives.blocks.templates.TemplateBlock;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IDirectionalTileEntity {
	default Direction getFacing () {
		BlockState state = getWorld().getBlockState(getPos());
		if (state.getBlock() instanceof TemplateBlock) {
			return ((TemplateBlock) state.getBlock()).getFacing(getWorld(), getPos());
		}

		return Direction.WEST;
	}

	World getWorld ();

	BlockPos getPos ();
}*/

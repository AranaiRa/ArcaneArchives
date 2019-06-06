package com.aranaira.arcanearchives.entity;

import com.aranaira.arcanearchives.init.BlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIOcelotSit;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AIResonatorSit extends EntityAIOcelotSit {
	public AIResonatorSit (EntityOcelot ocelotIn, double p_i45315_2_) {
		super(ocelotIn, p_i45315_2_);
	}

	@Override
	protected boolean shouldMoveTo (World worldIn, BlockPos pos) {
		if (!worldIn.isAirBlock(pos.up())) {
			return false;
		} else {
			IBlockState iblockstate = worldIn.getBlockState(pos);
			Block block = iblockstate.getBlock();

			return block == BlockRegistry.RADIANT_RESONATOR;
		}
	}
}

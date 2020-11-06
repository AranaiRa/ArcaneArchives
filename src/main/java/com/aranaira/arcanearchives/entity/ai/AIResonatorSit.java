package com.aranaira.arcanearchives.entity.ai;

import com.aranaira.arcanearchives.init.BlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.EntityAIOcelotSit;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AIResonatorSit extends EntityAIOcelotSit {
	public AIResonatorSit (OcelotEntity ocelotIn, double p_i45315_2_) {
		super(ocelotIn, p_i45315_2_);
	}

	@Override
	protected boolean shouldMoveTo (World worldIn, BlockPos pos) {
		if (!worldIn.isAirBlock(pos.up())) {
			return false;
		} else {
			BlockState iblockstate = worldIn.getBlockState(pos);
			Block block = iblockstate.getBlock();

			return block == BlockRegistry.RADIANT_RESONATOR;
		}
	}
}

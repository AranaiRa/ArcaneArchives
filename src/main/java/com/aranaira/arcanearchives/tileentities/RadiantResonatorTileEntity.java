package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.blocks.RadiantResonator;
import com.aranaira.arcanearchives.blocks.RawQuartz;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RadiantResonatorTileEntity extends ImmanenceTileEntity 
{
	public int TicksUntilCrystalGrowth = 200;
	public int BonusTicks; //How many bonus ticks the Resonator simulates based on supplied Immanence
	
	@Override
	public void update() {
		//ArcaneArchives.logger.info("TEST");
		
		
		if (TicksUntilCrystalGrowth > 0)
		{
			TicksUntilCrystalGrowth -= (1 + BonusTicks);
		}
		else
		{
			if (Block.getIdFromBlock(world.getBlockState(pos.add(0, 1, 0)).getBlock()) == 0)
			{
				TicksUntilCrystalGrowth = 200;
				if (!world.isRemote)
				{
					world.setBlockState(pos.add(0, 1, 0), Block.getStateById(Block.getIdFromBlock(ArcaneArchives.rawQuartz)), 3);
				}
			}
		}
		
		//ArcaneArchives.logger.info(NetworkID);
		
		super.update();
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
	{
	    return (oldState.getBlock() != newSate.getBlock());
	}
}

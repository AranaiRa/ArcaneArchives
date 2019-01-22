package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.init.BlockLibrary;
import com.aranaira.arcanearchives.util.handlers.ConfigHandler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

public class RadiantResonatorTileEntity extends ImmanenceTileEntity
{
	public int TicksUntilCrystalGrowth = ConfigHandler.values.iRadiantResonatorTickTime;
	public int BonusTicks; //How many bonus ticks the Resonator simulates based on supplied Immanence

	public RadiantResonatorTileEntity()
	{
		super("radiant_resonator_tile_entity");
		ImmanenceDrain = ConfigHandler.values.iRadiantResonatorDrain;
		BonusTicks = ConfigHandler.values.iRadiantResonatorBonusTicks;
	}

	@Override
	public void update()
	{
		if(Block.getIdFromBlock(world.getBlockState(pos.add(0, 1, 0)).getBlock()) == 0)
		{
			if(TicksUntilCrystalGrowth > 0)
			{
				TicksUntilCrystalGrowth--;
				if(IsDrainPaid) TicksUntilCrystalGrowth -= BonusTicks;
			} else
			{

				TicksUntilCrystalGrowth = ConfigHandler.values.iRadiantResonatorTickTime;
				if(!world.isRemote)
				{
					world.setBlockState(pos.add(0, 1, 0), Block.getStateById(Block.getIdFromBlock(BlockLibrary.RAW_QUARTZ)), 3);
				}
			}
		}


		super.update();
	}

	@Override
	@ParametersAreNonnullByDefault
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
	{
		return (oldState.getBlock() != newSate.getBlock());
	}

}

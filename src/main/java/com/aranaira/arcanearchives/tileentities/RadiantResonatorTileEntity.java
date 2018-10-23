package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.blocks.RadiantResonator;
import com.aranaira.arcanearchives.blocks.RawQuartz;
import com.aranaira.arcanearchives.init.BlockLibrary;
import com.aranaira.arcanearchives.util.handlers.ConfigHandler;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RadiantResonatorTileEntity extends ImmanenceTileEntity 
{
	public int TicksUntilCrystalGrowth = 200;
	public int BonusTicks; //How many bonus ticks the Resonator simulates based on supplied Immanence
	
	public RadiantResonatorTileEntity()
	{
		super("radiant_resonator_tile_entity");
		ImmanenceDrain = ConfigHandler.values.iRadiantResonatorDrain;
		BonusTicks = ConfigHandler.values.iRadiantResonatorBonusTicks;
	}
	
	@Override
	public void update() {
		if (TicksUntilCrystalGrowth > 0)
		{
			TicksUntilCrystalGrowth--;
			if (IsDrainPaid)
				TicksUntilCrystalGrowth -= BonusTicks;
		}
		else
		{
			if (Block.getIdFromBlock(world.getBlockState(pos.add(0, 1, 0)).getBlock()) == 0)
			{
				TicksUntilCrystalGrowth = 200;
				if (!world.isRemote)
				{
					world.setBlockState(pos.add(0, 1, 0), Block.getStateById(Block.getIdFromBlock(BlockLibrary.RAW_QUARTZ)), 3);
				}
			}
		}
		
		
		super.update();
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
	{
	    return (oldState.getBlock() != newState.getBlock());
	}
	
}

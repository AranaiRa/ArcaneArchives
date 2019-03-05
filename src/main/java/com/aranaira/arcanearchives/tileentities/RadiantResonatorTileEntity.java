package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.config.ConfigHandler;
import com.aranaira.arcanearchives.data.NetworkHelper;
import com.aranaira.arcanearchives.init.BlockRegistry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class RadiantResonatorTileEntity extends ImmanenceTileEntity
{
	private int growth = 0;
	private int ticks = 0;
	private int bonusTicks = ConfigHandler.values.iRadiantResonatorBonusTicks;
	private boolean canTick = false;

	public RadiantResonatorTileEntity()
	{
		super("radiant_resonator_tile_entity");
		immanenceDrain = ConfigHandler.values.iRadiantResonatorDrain;
	}

	@Override
	public void update()
	{
		super.update();

		ticks++;

		// Only tick on the client side
		if(world.isRemote || networkID == null || networkID.equals(NetworkHelper.INVALID)) return;

		// This will have to be updated to hive networks TODO
		EntityPlayer player = world.getPlayerEntityByUUID(networkID);

		// Don't tick if the player isn't online
		if(player == null)
		{
			canTick = false;
			return;
		} else
		{
			canTick = true;
		}

		int ticksRequired = ConfigHandler.values.iRadiantResonatorTickTime;

		if(world.isAirBlock(pos.up()))
		{
			if(growth < ticksRequired)
			{
				growth++;
				if(isDrainPaid) growth += bonusTicks;
			} else
			{
				growth = 0;
				world.setBlockState(pos.up(), BlockRegistry.RAW_QUARTZ.getDefaultState(), 3);
			}
		}

		if(ticks % 50 == 0)
		{
			this.defaultServerSideUpdate();
		}
	}

	public int getPercentageComplete()
	{
		return (int) Math.floor(growth / (double) ConfigHandler.values.iRadiantResonatorTickTime * 100D);
	}

	@Nonnull
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);

		compound.setInteger(Tags.CURRENT_TICK, growth);
		compound.setBoolean(Tags.CAN_TICK, canTick);

		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);

		if(compound.hasKey(Tags.CURRENT_TICK))
		{
			growth = compound.getInteger(Tags.CURRENT_TICK);
		}
		if(compound.hasKey(Tags.CAN_TICK))
		{
			canTick = compound.getBoolean(Tags.CAN_TICK);
		}
	}

	@Override
	public NBTTagCompound getUpdateTag()
	{
		return writeToNBT(new NBTTagCompound());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		readFromNBT(pkt.getNbtCompound());
		super.onDataPacket(net, pkt);
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
	{
		return (oldState.getBlock() != newSate.getBlock());
	}

	@Override
	@Nonnull
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		NBTTagCompound compound = writeToNBT(new NBTTagCompound());

		return new SPacketUpdateTileEntity(pos, 0, compound);
	}

	public boolean canTick()
	{
		return this.canTick;
	}

	public static class Tags
	{
		public static final String CURRENT_TICK = "current_tick";
		public static final String CAN_TICK = "can_tick";
	}
}

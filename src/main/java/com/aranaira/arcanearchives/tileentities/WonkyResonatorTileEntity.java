package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.config.ConfigHandler;
import com.aranaira.arcanearchives.config.ServerSideConfig;
import com.aranaira.arcanearchives.init.BlockRegistry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Random;

public class WonkyResonatorTileEntity extends AATileEntity implements ITickable {
	public static Random rand = new Random();

	private int growth = 0;
	private int ticks = 0;
	private boolean canTick = false;
	private boolean breaking = false;

	public WonkyResonatorTileEntity () {
		setName("wonky_resonator_tile_entity");
	}

	@Override
	public void update () {
		ticks++;

		if (world.isRemote) {
			return;
		}

		int ticksRequired = ServerSideConfig.ResonatorTickTime;

		markDirty();

		if (world.isAirBlock(pos.up())) {
			if (growth < ticksRequired) {
				growth++;
			} else {
				growth = 0;


				// TODO: EXPLODE
			}
		}
	}

	@Nonnull
	@Override
	public NBTTagCompound writeToNBT (NBTTagCompound compound) {
		super.writeToNBT(compound);

		compound.setInteger(Tags.CURRENT_TICK, growth);

		return compound;
	}

	@Override
	public void readFromNBT (NBTTagCompound compound) {
		super.readFromNBT(compound);

		if (compound.hasKey(Tags.CURRENT_TICK)) {
			growth = compound.getInteger(Tags.CURRENT_TICK);
		}
	}

	public int getPercentageComplete () {
		return (int) Math.floor(growth / (double) ServerSideConfig.ResonatorTickTime * 100D);
	}

	@Override
	@Nonnull
	public SPacketUpdateTileEntity getUpdatePacket () {
		NBTTagCompound compound = writeToNBT(new NBTTagCompound());

		return new SPacketUpdateTileEntity(pos, 0, compound);
	}

	@Override
	public NBTTagCompound getUpdateTag () {
		return writeToNBT(new NBTTagCompound());
	}

	@Override
	public void onDataPacket (NetworkManager net, SPacketUpdateTileEntity pkt) {
		readFromNBT(pkt.getNbtCompound());
		super.onDataPacket(net, pkt);
	}

	@Override
	public boolean shouldRefresh (World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
		return (oldState.getBlock() != newSate.getBlock());
	}

	public static class Tags {
		public static final String CURRENT_TICK = "current_tick";
	}
}

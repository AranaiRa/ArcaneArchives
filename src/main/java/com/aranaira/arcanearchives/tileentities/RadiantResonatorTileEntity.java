package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.blocks.RawQuartzCluster;
import com.aranaira.arcanearchives.config.ConfigHandler;
import com.aranaira.arcanearchives.data.DataHelper;
import com.aranaira.arcanearchives.data.ServerNetwork;
import com.aranaira.arcanearchives.init.BlockRegistry;
import com.aranaira.arcanearchives.init.SoundRegistry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class RadiantResonatorTileEntity extends ImmanenceTileEntity {
	public static Random rand = new Random();

	private int growth = 0;
	private int ticks = 0;
	private int bonusTicks = 0;
	private boolean canTick = false;

	public RadiantResonatorTileEntity () {
		super("radiant_resonator_tile_entity");
	}

	@Override
	public void update () {
		super.update();

		ticks++;

		// Only tick on the client side
		if (world.isRemote || networkId == null || networkId.equals(DataHelper.INVALID)) {
			return;
		}

		// This will have to be updated to hive networks TODO
		EntityPlayer player = world.getPlayerEntityByUUID(networkId);

		// Don't tick if the player isn't online
		if (player == null) {
			canTick = false;
			return;
		} else {
			canTick = true;
		}

		int ticksRequired = ConfigHandler.serverSideConfig.ResonatorTickTime;

		markDirty();

		if (world.isAirBlock(pos.up())) {
			if (growth < ticksRequired) {
				growth++;
			} else {
				growth = 0;
				world.setBlockState(pos.up(), BlockRegistry.RAW_QUARTZ.getStateFromMeta(2));

				BlockPos up = pos.up();
				List<EntityOcelot> entities = world.getEntitiesWithinAABB(EntityOcelot.class, new AxisAlignedBB(up.getX() - 0.9, up.getY() - 0.9, up.getZ() - 0.9, up.getX() + 0.9, up.getY() + 0.9, up.getZ() + 0.9));
				for (EntityOcelot ocelot : entities) {
					ocelot.motionY += rand.nextFloat() * 5F;
					ocelot.motionX += (rand.nextFloat() - 0.5f) * 3F;
					ocelot.motionZ += (rand.nextFloat() - 0.5f) * 3F;
				}

				world.playSound(null, pos, SoundRegistry.RESONATOR_COMPLETE, SoundCategory.BLOCKS, 1f, 1f);
			}
		}

		if (ticks % 50 == 0) {
			this.defaultServerSideUpdate();
		}
	}

	@Nonnull
	@Override
	public NBTTagCompound writeToNBT (NBTTagCompound compound) {
		super.writeToNBT(compound);

		compound.setInteger(Tags.CURRENT_TICK, growth);
		compound.setBoolean(Tags.CAN_TICK, canTick);

		return compound;
	}

	@Override
	public void readFromNBT (NBTTagCompound compound) {
		super.readFromNBT(compound);

		if (compound.hasKey(Tags.CURRENT_TICK)) {
			growth = compound.getInteger(Tags.CURRENT_TICK);
		}
		if (compound.hasKey(Tags.CAN_TICK)) {
			canTick = compound.getBoolean(Tags.CAN_TICK);
		}
	}

	public int getPercentageComplete () {
		return (int) Math.floor(growth / (double) ConfigHandler.serverSideConfig.ResonatorTickTime * 100D);
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

	@Override
	public void breakBlock (@Nullable IBlockState state, boolean harvest) {
		super.breakBlock(state, harvest);

		if (world.isRemote) {
			return;
		}

		ServerNetwork network = DataHelper.getServerNetwork(networkId, world);
		if (network != null) {
			network.removeTile(this);
		}
	}

	public TickResult canTick () {
		if (world.isAirBlock(pos.up())) {
			if (!canTick) {
				return TickResult.OFFLINE;
			} else {
				return TickResult.TICKING;
			}
		} else {
			IBlockState up = world.getBlockState(pos.up());
			if (up.getBlock() instanceof RawQuartzCluster) {
				return TickResult.HARVEST_WAITING;
			} else {
				return TickResult.OBSTRUCTION;
			}
		}
	}

	public enum TickResult {
		OBSTRUCTION("obstruction", TextFormatting.RED), HARVEST_WAITING("harvestable", TextFormatting.GOLD), OFFLINE("offline", TextFormatting.DARK_RED), TICKING("resonating", TextFormatting.GREEN);

		private String key;
		private TextFormatting format;

		TickResult (String key, TextFormatting format) {
			this.key = key;
			this.format = format;
		}

		public String getKey () {
			return "arcanearchives.data.tooltip.resonator_status." + key;
		}

		public TextFormatting getFormat () {
			return format;
		}
	}

	public static class Tags {
		public static final String CURRENT_TICK = "current_tick";
		public static final String CAN_TICK = "can_tick";
	}
}

package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.blocks.MultiblockSize;
import com.aranaira.arcanearchives.config.ConfigHandler;
import com.aranaira.arcanearchives.data.DataHelper;
import com.aranaira.arcanearchives.data.ServerNetwork;
import com.aranaira.arcanearchives.events.ServerTickHandler;
import com.aranaira.arcanearchives.types.MachineSound;
import com.aranaira.arcanearchives.util.ManifestTrackingUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;


public class ImmanenceTileEntity extends AATileEntity implements ITickable {
	public UUID uuid = null;
	public UUID networkId = null; //UUID of network owner
	public boolean hasBeenAddedToNetwork = false;
	public int dimension;
	public MultiblockSize size;
	private BlockPos lastPosition;
	private ServerNetwork network;
	private int ticks = 0;
	private boolean fake = false;
	@SideOnly(Side.CLIENT)
	protected MachineSound sound;

	public ImmanenceTileEntity (String name) {
		setName(name);
	}

	public ImmanenceTileEntity (String name, boolean fake) {
		this(name);
		this.fake = fake;
	}

	// TODO: Fix these up at some point to be less cancerous
	public void tick () {
		ticks++;
	}

	public int ticks () {
		return ticks;
	}

	@Override
	public boolean isActive () {
		// TODO: in a later version, functionality for initial registration delay
		return true;
	}

	public UUID getNetworkId () {
		return networkId;
	}

	public UUID getUuid () {
		return uuid;
	}

	public void setNetworkId (UUID newId) {
		this.networkId = newId;
	}

	@Override
	public void onLoad () {
		if (world != null && !world.isRemote && !fake) {
			ServerTickHandler.incomingITE(this);
			ArcaneArchives.logger.debug(String.format("Loaded a tile entity with the class %s into the queue.", this.getClass().getName()));
		} else if (world == null) {
			// TODO: Include more information
			ArcaneArchives.logger.debug("TileEntity loaded in with a null world. WTF?");
		}
		super.onLoad();
	}

	public void joinedNetwork (ServerNetwork network) {
	}

	public void firstJoinedNetwork (ServerNetwork network) {
	}

	@Nullable
	public ServerNetwork getServerNetwork () {
		if (network == null && networkId != null) {
			network = DataHelper.getServerNetwork(networkId, this.world);
		}

		return network;
	}

	public void tryGenerateUUID () {
		if (this.world != null && !this.world.isRemote && this.networkId != null && this.uuid == null && !fake) {
			ServerNetwork network = getServerNetwork();
			if (network != null) {
				this.uuid = network.generateTileUuid();
				firstJoinedNetwork(getServerNetwork());
				markDirty();
				defaultServerSideUpdate();
			}
		}
	}

	public boolean handleManipulationInterface (EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		return false;
	}

	@Override
	public void readFromNBT (NBTTagCompound compound) {
		if (compound.hasKey(Tags.PLAYER_ID)) {
			networkId = UUID.fromString(compound.getString(Tags.PLAYER_ID));
		} else {
			ArcaneArchives.logger.debug(String.format("Tile entity of class %s didn't have a network ID", this.getClass().getName()));
		}
		if (compound.hasKey(Tags.TILE_ID)) {
			UUID newId = UUID.fromString(compound.getString(Tags.TILE_ID));
			if (uuid != null && !uuid.equals(newId)) {
				if (!this.world.isRemote) {
					ServerNetwork network = getServerNetwork();
					if (network != null) {
						network.handleTileIdChange(uuid, newId);
					}
				}
			}
			uuid = newId;
		}

		if (uuid == null) {
			this.tryGenerateUUID();
		}
		dimension = compound.getInteger(Tags.DIM);
		super.readFromNBT(compound);
	}

	@Override
	@Nonnull
	public NBTTagCompound writeToNBT (NBTTagCompound compound) {
		if (networkId != null) {
			compound.setString(Tags.PLAYER_ID, networkId.toString());
		}
		if (uuid != null) {
			compound.setString(Tags.TILE_ID, uuid.toString());
		}
		compound.setInteger(Tags.DIM, dimension);
		return super.writeToNBT(compound);
	}

	public void tileMoved (BlockPos oldPosition, BlockPos newPosition) {
		ServerNetwork network = getServerNetwork();
		network.tileEntityMoved(this.uuid, newPosition);
	}

	@Override
	public void update () {
		if (lastPosition == null) {
			lastPosition = getPos();
		}

		if (!getPos().equals(lastPosition)) {
			tileMoved(getPos(), lastPosition);
		}

		if (world.isRemote) {
			updateSound();
		}
	}

	@Override
	public void invalidate () {
		super.invalidate();
		if (world.isRemote) {
			updateSound();
		}
	}

	@SideOnly(Side.CLIENT)
	public boolean isBeingTracked () {
		return ManifestTrackingUtils.get(dimension, getPos()) != null;
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

	protected boolean shouldPlaySound () {
		return hasSound() && !isInvalid();
	}

	protected boolean hasSound () {
		return false;
	}

	protected ResourceLocation getSound () {
		return null;
	}

	protected float getVolume () {
		return 1f;
	}

	protected float getPitch () {
		return 1f;
	}

	public int getNetworkPriority () {
		return -1;
	}

	@SideOnly(Side.CLIENT)
	private void updateSound () {
		if (ConfigHandler.soundConfig.useSounds) {
			final ResourceLocation soundRL = getSound();
			if (shouldPlaySound() && soundRL != null) {
				if (sound == null) {
					FMLClientHandler.instance().getClient().getSoundHandler().playSound(sound = new MachineSound(soundRL, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, getVolume(), getPitch()));
				}
			} else if (sound != null) {
				sound.endPlaying();
				sound = null;
			}
		}
	}

	public static class Tags {
		public static final String PLAYER_ID = "playerId";
		public static final String DIM = "dim";
		public static final String TILE_ID = "tileId";
	}
}

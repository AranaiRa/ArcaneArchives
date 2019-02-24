package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.data.ClientNetwork;
import com.aranaira.arcanearchives.data.NetworkHelper;
import com.aranaira.arcanearchives.data.ServerNetwork;
import com.aranaira.arcanearchives.util.handlers.AAServerTickHandler;
import com.aranaira.arcanearchives.util.types.Size;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;

import javax.annotation.Nonnull;
import java.util.UUID;


public class ImmanenceTileEntity extends AATileEntity implements ITickable
{
	public UUID tileID = null;
	public UUID networkID = null; //UUID of network owner
	public int immanenceDrain = 0; //Immanence cost to operate the device
	public int immanenceGeneration = 0; //Immanence that is given to the network with this device
	public int networkPriority = 0; //What order the device's Immanence is paid for
	public boolean isDrainPaid = false; //Whether the device's Immanence needs have been covered
	public boolean isProtected = false; //Whether the device is currently indestructable
	public boolean hasBeenAddedToNetwork = false;
	public int dimension;
	public Size size;
	private ServerNetwork network;
	private ClientNetwork cNetwork;
	private int ticks = 0;

	public ImmanenceTileEntity(String name)
	{
		setName(name);
	}

	public void generateTileId () {
		if (!this.world.isRemote && this.networkID != null && this.tileID == null) {
			ServerNetwork network = getNetwork();
			this.tileID = network.generateTileId();
		}
	}

	// Functions specifically for dealing with Rannuncarpus. >:0!
	public void tick()
	{
		ticks++;
	}

	public int ticks()
	{
		return ticks;
	}

	@Override
	public void update()
	{
	}

	@Override
	public boolean isActive()
	{
		// TODO: in a later version, functionality for initial registration delay
		return true;
	}

	public UUID GetNetworkID()
	{
		return networkID;
	}

	public void SetNetworkID(UUID newId)
	{
		this.networkID = newId;
	}

	@Override
	@Nonnull
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		if(networkID != null)
		{
			compound.setString(Tags.PLAYER_ID, networkID.toString());
		}
		if(tileID != null)
		{
			compound.setString(Tags.TILE_ID, tileID.toString());
		}
		compound.setInteger(Tags.DIM, dimension);
		return super.writeToNBT(compound);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		if(compound.hasKey(Tags.PLAYER_ID))
		{
			networkID = UUID.fromString(compound.getString(Tags.PLAYER_ID));
		}
		if(compound.hasKey(Tags.TILE_ID))
		{
			UUID newId = UUID.fromString(compound.getString(Tags.TILE_ID));
			if (tileID != null && !tileID.equals(newId)) {
				if (!this.world.isRemote)
				{
					getNetwork().handleTileIdChange(tileID, newId);
				}
			}
			tileID = newId;
		} else if (tileID == null) {
			this.generateTileId();
		}
		dimension = compound.getInteger(Tags.DIM);
		super.readFromNBT(compound);
	}

	public int GetNetImmanence()
	{
		return immanenceGeneration - immanenceDrain;
	}

	public ClientNetwork getClientNetwork()
	{
		if(cNetwork == null)
		{
			cNetwork = NetworkHelper.getClientNetwork(networkID);
		}

		return cNetwork;
	}

	public ServerNetwork getNetwork()
	{
		if(network == null && networkID != null)
		{
			network = NetworkHelper.getServerNetwork(networkID, this.world);
		}

		return network;
	}

	@Override
	public void invalidate()
	{
		super.invalidate();

		if(world.isRemote) return;

		if(getNetwork() == null) return;

		network.RemoveTileFromNetwork(this);
	}

	@Override
	public void onChunkUnload()
	{
		super.onChunkUnload();

		if(world.isRemote) return;

		if(getNetwork() == null) return;

		network.RemoveTileFromNetwork(this);
	}

	@Override
	public void onLoad()
	{
		if(world != null && !world.isRemote)
		{
			AAServerTickHandler.incomingITE(this);
		} else if(world == null)
		{
			// TODO: Include more information
			ArcaneArchives.logger.info("TileEntity loaded in with a null world. WTF?");
		}
		super.onLoad();
	}

	public static class Tags
	{
		public static final String PLAYER_ID = "playerId";
		public static final String DIM = "dim";
		public static final String TILE_ID = "tileId";
	}
}

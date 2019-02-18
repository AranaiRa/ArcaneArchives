package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.data.ClientNetwork;
import com.aranaira.arcanearchives.data.ServerNetwork;
import com.aranaira.arcanearchives.data.NetworkHelper;
import com.aranaira.arcanearchives.util.handlers.AAServerTickHandler;
import com.aranaira.arcanearchives.util.types.Size;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;

import javax.annotation.Nonnull;
import java.util.UUID;


public class ImmanenceTileEntity extends AATileEntity implements ITickable
{
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

	/*public int GetTotalItems()
	{
		int tmp = 0;
		for(ItemStack item : inventory)
		{
			tmp += item.getCount();
		}
		return tmp;
	}

	public ItemStack InsertItem(ItemStack item, boolean simulate)
	{
		//Creates a copy of the item that can safely be edited.
		ItemStack temp = item.copy();

		//Returns the itemstack if this tile entity cannot have items inserted.
		if(item.isEmpty() || !isDrainPaid || (GetTotalItems() >= maxItems)) return temp;

		//Sets the amount of free space in the network.
		int maxCanAdd = maxItems - GetTotalItems();

		//If the amount of free space is greater than the itemstack item count, then it brings it down to that amount.
		if(maxCanAdd > temp.getCount()) maxCanAdd = temp.getCount();

		//Tries to find the same item in the network, so that it will consolidate the itemstack.
		for(ItemStack itemStack : inventory)
		{
			if(ItemComparison.AreItemsEqual(temp, itemStack))
			{
				//Adds the item count to the one in the network, and removes the remainder from the one that will be returned.
				if(!simulate) itemStack.setCount(itemStack.getCount() + maxCanAdd);
				temp.setCount(temp.getCount() - maxCanAdd);
				return temp;
			}
		}

		//If the item is not found, create a copy that will be added to the network.
		ItemStack temp_add = temp.copy();

		//Sets the itemstack count for the proper amount to be added to the inventory then adds the itemstack to the inventory.
		temp_add.setCount(maxCanAdd);
		if(!simulate) inventory.add(temp_add);

		//Reduces the returned stack to the remainder of items. Then returns that itemstack.
		temp.setCount(temp.getCount() - maxCanAdd);

		return temp;
	}

	//Returns true if it was successful at removing the item, false if there is no such item in inventory.
	public ItemStack RemoveItem(ItemStack item)
	{
		if(!isDrainPaid) return null;
		for(ItemStack itemStack : inventory)
		{
			if(ItemComparison.AreItemsEqual(itemStack, item))
			{
				if(itemStack.getCount() > itemStack.getMaxStackSize())
				{
					ItemStack temp = itemStack.copy();
					temp.setCount(item.getMaxStackSize());
					itemStack.setCount(itemStack.getCount() - item.getMaxStackSize());

					return temp;
				} else
				{
					if(inventory.remove(itemStack)) return itemStack;
				}
			}
		}
		return null;
	}

	public ItemStack RemoveItemCount(ItemStack item, int count_needed, boolean simulate)
	{
		if(!isDrainPaid) return ItemStack.EMPTY;
		for(ItemStack itemStack : inventory)
		{
			if(ItemComparison.AreItemsEqual(itemStack, item))
			{
				if(itemStack.getCount() > count_needed)
				{
					ItemStack temp = itemStack.copy();
					temp.setCount(count_needed);
					if(!simulate) itemStack.setCount(itemStack.getCount() - count_needed);

					return temp;
				} else
				{
					if(!simulate)
					{
						if(inventory.remove(itemStack)) return itemStack.copy();
					} else
					{
						return itemStack.copy();
					}
				}
			}
		}
		return ItemStack.EMPTY;
	}*/

	@Override
	@Nonnull
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		if (networkID != null) {
			compound.setString(Tags.PLAYER_ID, networkID.toString());
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

		private Tags()
		{
		}
	}
}

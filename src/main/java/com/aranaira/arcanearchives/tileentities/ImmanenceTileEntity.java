package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.data.ArcaneArchivesNetwork;
import com.aranaira.arcanearchives.util.TileHelper;
import com.aranaira.arcanearchives.util.Tuple;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;

import java.util.Iterator;
import java.util.UUID;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.init.BlockLibrary;
import com.aranaira.arcanearchives.util.ItemComparison;
import com.aranaira.arcanearchives.util.NetworkHelper;
import net.minecraft.world.World;

public class ImmanenceTileEntity extends TileEntity implements ITickable
{
	public UUID NetworkID = null; //UUID of network owner
	public int ImmanenceDrain; //Immanence cost to operate the device
	public int ImmanenceGeneration; //Immanence that is given to the network with this device
	public int NetworkPriority; //What order the device's Immanence is paid for
	public boolean IsDrainPaid; //Whether the device's Immanence needs have been covered
	public boolean IsProtected; //Whether the device is currently indestructable
	public String name;
	public boolean hasBeenAddedToNetwork = false;
	public int Dimension;
	public NonNullList<ItemStack> Inventory;
	public boolean IsInventory = false;
	public int MaxItems;
	public int Width;
	public int Height;
	private byte facing;
	
	public ImmanenceTileEntity(String name)
	{
		this.name = name;
		BlockLibrary.TILE_ENTITIES.put(name, this);
		Inventory = NonNullList.create();
	}
	
	@Override
	public void update() {
		if (!world.isRemote && GetNetworkID() == null && getPos() != BlockPos.ORIGIN) {
			UUID newId = TileHelper.getNetworkIdForPos(getPos());
			if (newId == null) {
				// Handle that
				return;
			}

			EntityPlayer owner = world.getPlayerEntityByUUID(newId);
			if (owner == null) {
				// Handle that

			} else {
				SetNetworkID(newId);
				Dimension = owner.dimension;
				ArcaneArchivesNetwork network = NetworkHelper.getArcaneArchivesNetwork(newId);

				// Any custom handling of name (like the matrix core) should be done here
				network.AddTileToNetwork(this);
			}
		}
	}

	public void setWidthAndHeight (Tuple<Integer, Integer> values)
	{
		this.Width = values.val1;
		this.Height = values.val2;
	}

	public void setWidthAndHeight (int width, int height)
	{
		this.Width = width;
		this.Height = height;
	}

	public boolean hasAccessors () {
		return this.Width != 1 && this.Height != 1;
	}

	public UUID GetNetworkID ()
	{
		return NetworkID;
	}

	private void SetNetworkID (UUID newId)
	{
		this.NetworkID = newId;
	}
	
	public int GetTotalItems()
	{
		int tmp = 0;
		for (ItemStack item : Inventory)
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
		if (item.isEmpty() || !IsDrainPaid || (GetTotalItems() >= MaxItems))
			return temp;
		
		//Sets the amount of free space in the network.
		int maxCanAdd = MaxItems - GetTotalItems();
		
		//If the amount of free space is greater than the itemstack item count, then it brings it down to that amount.
		if (maxCanAdd > temp.getCount())
			maxCanAdd = temp.getCount();
		
		//Tries to find the same item in the network, so that it will consolidate the itemstack.
		for (ItemStack itemStack : Inventory)
		{
			if (ItemComparison.AreItemsEqual(temp, itemStack))
			{
				//Adds the item count to the one in the network, and removes the remainder from the one that will be returned.
				if (!simulate)
					itemStack.setCount(itemStack.getCount() + maxCanAdd);
				temp.setCount(temp.getCount() - maxCanAdd);
				return temp;
			}
		}
		
		//If the item is not found, create a copy that will be added to the network.
		ItemStack temp_add = temp.copy();
		
		//Sets the itemstack count for the proper amount to be added to the inventory then adds the itemstack to the inventory.
		temp_add.setCount(maxCanAdd);
		if (!simulate)
			Inventory.add(temp_add);
		
		//Reduces the returned stack to the remainder of items. Then returns that itemstack.
		temp.setCount(temp.getCount() - maxCanAdd);
		
		return temp;
	}
	
	//Returns true if it was successful at removing the item, false if there is no such item in inventory.
	public ItemStack RemoveItem(ItemStack item)
	{
		if (!IsDrainPaid)
			return null;
		for (ItemStack itemStack : Inventory)
		{
			if (ItemComparison.AreItemsEqual(itemStack, item))
			{
				if (itemStack.getCount() > itemStack.getMaxStackSize())
				{
					ItemStack temp = itemStack.copy();
					temp.setCount(item.getMaxStackSize());
					itemStack.setCount(itemStack.getCount() - item.getMaxStackSize());
					
					return temp;
				}
				else
				{
					if (Inventory.remove(itemStack))
						return itemStack;
				}
			}
		}
		return null;
	}

	public ItemStack RemoveItemCount(ItemStack item, int count_needed, boolean simulate) {
		if (!IsDrainPaid)
			return ItemStack.EMPTY;
		for (ItemStack itemStack : Inventory)
		{
			if (ItemComparison.AreItemsEqual(itemStack, item))
			{
				if (itemStack.getCount() > count_needed)
				{
					ItemStack temp = itemStack.copy();
					temp.setCount(count_needed);
					if (!simulate)
						itemStack.setCount(itemStack.getCount() - count_needed);
					
					return temp;
				}
				else
				{
					if (!simulate)
					{
						if (Inventory.remove(itemStack))
							return itemStack.copy();
					}
					else
					{
						return itemStack.copy();
					}
				}
			}
		}
		return ItemStack.EMPTY;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setUniqueId("playerId", NetworkID);
		compound.setLong("blockpos", getPos().toLong());
		compound.setInteger("dim", Dimension);
		NBTTagList tags = new NBTTagList();
		for (ItemStack item : Inventory) 
		{
			NBTTagCompound data = new NBTTagCompound();
			item.writeToNBT(data);
			tags.appendTag(data);
		}
		compound.setTag("inventory", tags);
		compound.setInteger("invsize", MaxItems);
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		NetworkID = compound.getUniqueId("playerId");
		Dimension = compound.getInteger("dim");
		MaxItems = compound.getInteger("invsize");
		NBTTagList tags = compound.getTagList("inventory", 10);
		Iterator itr = tags.iterator();
		while(itr.hasNext())
		{
			NBTTagCompound data = (NBTTagCompound)itr.next();
			ItemStack temp = new ItemStack(data);
			if (!temp.isEmpty())
				Inventory.add(temp);
		}
		NetworkHelper.getArcaneArchivesNetwork(NetworkID).AddTileToNetwork(this);
		super.readFromNBT(compound);
	}
	
	public int GetNetImmanence()
	{
		return ImmanenceGeneration - ImmanenceDrain;
	}
}

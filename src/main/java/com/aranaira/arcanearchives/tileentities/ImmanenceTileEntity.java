package com.aranaira.arcanearchives.tileentities;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.init.BlockLibrary;
import com.aranaira.arcanearchives.util.ItemComparison;
import com.aranaira.arcanearchives.util.NetworkHelper;

public class ImmanenceTileEntity extends TileEntity implements ITickable
{
	public UUID NetworkID; //UUID of network owner
	public int ImmanenceDrain; //Immanence cost to operate the device
	public int ImmanenceGeneration; //Immanence that is given to the network with this device
	public int NetworkPriority; //What order the device's Immanence is paid for
	public boolean IsDrainPaid; //Whether the device's Immanence needs have been covered
	public boolean IsProtected; //Whether the device is currently indestructable
	public String name;
	public BlockPos blockpos;
	public boolean hasBeenAddedToNetwork = false;
	public int Dimension;
	//BLOOD MAGIC uses NonNullList<ItemStack>
	public NonNullList<ItemStack> Inventory;
	public boolean IsInventory = false;
	public int MaxItems;
	
	public ImmanenceTileEntity(String name)
	{
		this.name = name;
		BlockLibrary.TILE_ENTITIES.put(name, this);
		Inventory = NonNullList.create();
	}
	
	@Override
	public void update() {
		// TODO Auto-generated method stub
		
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
	
	//Returns true if item was successful at adding to inventory, false if full.
	public boolean AddItem(ItemStack item)
	{
		if (item.isEmpty() || !IsDrainPaid || (GetTotalItems() + item.getCount() > MaxItems))
			return false;
		
		for (ItemStack itemStack : Inventory)
		{
			//TODO : Check for too many items in the block.
			if (itemStack.getUnlocalizedName().compareTo(item.getUnlocalizedName()) == 0)
			{
				itemStack.setCount(itemStack.getCount() + item.getCount());
				return true;
			}
		}
		
		return Inventory.add(item);
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

	public ItemStack RemoveItemCount(ItemStack item, int count_needed) {
		if (!IsDrainPaid)
			return null;
		for (ItemStack itemStack : Inventory)
		{
			if (ItemComparison.AreItemsEqual(itemStack, item))
			{
				if (itemStack.getCount() > count_needed)
				{
					ItemStack temp = itemStack.copy();
					temp.setCount(count_needed);
					itemStack.setCount(itemStack.getCount() - count_needed);
					
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
	
	public ItemStack RemoveHalfItem(ItemStack item)
	{
		if (!IsDrainPaid)
			return null;
		for (ItemStack itemStack : Inventory)
		{
			if (itemStack.getUnlocalizedName().compareTo(item.getUnlocalizedName()) == 0)
			{
				if (itemStack.getCount() > itemStack.getMaxStackSize())
				{
					ItemStack temp = itemStack.copy();
					temp.setCount(itemStack.getMaxStackSize()/2);
					itemStack.setCount(itemStack.getCount() - item.getMaxStackSize()/2);
					return temp;
				}
			}
		}
		Inventory.remove(item);
		return item;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setUniqueId("playerId", NetworkID);
		compound.setString("name", name);
		compound.setLong("blockpos", blockpos.toLong());
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
		name = compound.getString("name");
		blockpos = BlockPos.fromLong(compound.getLong("blockpos"));
		Dimension = compound.getInteger("dim");
		MaxItems = compound.getInteger("invsize");
		NBTTagList tags = compound.getTagList("inventory", 10);
		for (int i = 0; i < tags.tagCount(); i++)
		{
			NBTTagCompound data = tags.getCompoundTagAt(i);
			Inventory.add(new ItemStack(data));
		}
		NetworkHelper.getArcaneArchivesNetwork(NetworkID).AddBlockToNetwork(name, this);
		super.readFromNBT(compound);
	}
	
	public int GetNetImmanence()
	{
		return ImmanenceGeneration - ImmanenceDrain;
	}

	public ItemStack RemoveRandomItem() {
		for (ItemStack is : Inventory)
		{
			if (is != null)
			{
				this.RemoveItem(is);
				if (is.getCount() > 64)
				{
					ItemStack item = is.copy();
					item.setCount(64);
				}
				return is;
			}
		}
		return null;
	}
	
	//TODO : DOES NOT COMPARE NBT DATA RIGHT NOW
	public boolean HasItem(ItemStack s)
	{
		for (ItemStack itemStack : Inventory)
		{
			if (itemStack.getUnlocalizedName() == s.getUnlocalizedName())
			{
				return true;
			}
		}
		return false;
	}
}

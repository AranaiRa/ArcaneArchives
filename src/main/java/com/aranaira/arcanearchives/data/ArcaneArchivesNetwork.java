package com.aranaira.arcanearchives.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.blocks.BlockTemplate;
import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.INBTSerializable;

public class ArcaneArchivesNetwork implements INBTSerializable<NBTTagCompound>
{
	private UUID playerId;
	private AAWorldSavedData parent;
	
	private Map<ImmanenceTileEntity, String> blocks = new HashMap<>();
	private ImmanenceTileEntity MatrixCoreInstance;
	private int CurrentImmanence;
	private boolean NeedsToBeUpdated = true;

	private ArcaneArchivesNetwork()
	{
		
	}
	
	public int GetImmanence()
	{
		if (NeedsToBeUpdated)
		{
			UpdateImmanence();
		}
		return CurrentImmanence;
	}
	
	public int CountBlocks(BlockTemplate block)
	{
		int tmpCount = 0;
		for (ImmanenceTileEntity ITE : blocks.keySet())
		{
			if (ITE.name == block.refName)
				tmpCount++;
		}
		return tmpCount;
	}
	
	public void UpdateImmanence()
	{
		CurrentImmanence = 0;
		int TotalGeneration = 0;
		int TotalDrain = 0;
		
		for (ImmanenceTileEntity ITE : blocks.keySet())
		{
			TotalGeneration += ITE.ImmanenceGeneration;
		}
		
		for (ImmanenceTileEntity ITE : blocks.keySet())
		{
			int tmpDrain = ITE.ImmanenceDrain;
			if (TotalGeneration > (TotalDrain + tmpDrain))
			{
				TotalDrain += tmpDrain;
				ITE.IsDrainPaid = true;
			}
			else
			{
				ITE.IsDrainPaid = false;
			}
		}
		CurrentImmanence = TotalGeneration - TotalDrain;
		NeedsToBeUpdated = false;
	}
	
	public List<NonNullList<ItemStack>> GetItemsOnNetwork()
	{
		List<NonNullList<ItemStack>> inventories = new ArrayList<>();
		
		for (ImmanenceTileEntity ITE : blocks.keySet())
		{
			if (ITE.IsInventory)
			{
				inventories.add(ITE.Inventory);
			}
		}
		
		return inventories;
	}
	
	//For testing
	public boolean AddItemToNetwork(EntityPlayer PE)
	{
		//ItemStack i = PE.getHeldItem(EnumHand.MAIN_HAND);
		ItemStack i = PE.inventory.getStackInSlot(0);
		for (ImmanenceTileEntity ITE : blocks.keySet())
		{
			if (ITE.IsInventory)
			{
				if (ITE.AddItem(i))
				{
					PE.inventory.removeStackFromSlot(0);
					return true;
				}
			}
		}
		return false;
	}

	public boolean RemoveItemFromNetwork(EntityPlayer PE) {
		for (ImmanenceTileEntity ITE : blocks.keySet())
		{
			if (ITE.IsInventory)
			{
				ItemStack s;
				if ((s = ITE.RemoveRandomItem()) != null)
				{
					PE.inventory.setInventorySlotContents(0, s);
					return true;
				}
			}
		}
		return false;
	}
	
	public Map<ImmanenceTileEntity, String> getBlocks()
	{
		return blocks;
	}
	
	public void AddBlockToNetwork(String blockName, ImmanenceTileEntity tileEntityInstance)
	{
		if (IsBlockPosAvailable(tileEntityInstance.blockpos, tileEntityInstance.Dimension))
		{
			blocks.put(tileEntityInstance, blockName);
			tileEntityInstance.hasBeenAddedToNetwork = true;
			NeedsToBeUpdated = true;
			UpdateImmanence();
		}
	}
	
	public boolean IsBlockPosAvailable(BlockPos pos, int dimID)
	{
		for (ImmanenceTileEntity ITE : blocks.keySet())
		{
			if (ITE.blockpos == pos && ITE.Dimension == dimID)
				return false;
		}
		
		return true;
	}
	
	public void RemoveBlockFromNetwork(ImmanenceTileEntity ITE)
	{
		blocks.remove(ITE);
		NeedsToBeUpdated = true;
		UpdateImmanence();
	}
	
	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound tagCompound = new NBTTagCompound();
		tagCompound.setUniqueId("playerId", playerId);
		
		return tagCompound;
	}
	

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		this.playerId = nbt.getUniqueId("playerId");
	}

	public static ArcaneArchivesNetwork newNetwork(UUID playerID) {
		ArcaneArchivesNetwork network = new ArcaneArchivesNetwork();
		network.playerId = playerID;
		return network;
	}

	public void MarkUnsaved() {
		if (getParent() != null)
		{
			getParent().markDirty();
		}
		else
		{
			//TODO: Log that a error had happened and it is not able to be saved
		}
	}
	
	public AAWorldSavedData getParent()
	{
		return parent;
	}
	
	public ArcaneArchivesNetwork setParent(AAWorldSavedData parent)
	{
		this.parent = parent;
		MarkUnsaved();
		return this;
	}

	public UUID getPlayerID() {
		return playerId;
	}

	public static ArcaneArchivesNetwork fromNBT(NBTTagCompound data) {
		ArcaneArchivesNetwork network = new ArcaneArchivesNetwork();
		network.deserializeNBT(data);
		return network;
	}

	public int GetTotalItems() {
		int tmp = 0;
		
		for (ImmanenceTileEntity ITE : blocks.keySet())
		{
			if (ITE.IsInventory)
			{
				tmp += ITE.GetTotalItems();
			}
		}
		
		return tmp;
	}

	public int GetTotalSpace() {
		int tmp = 0;
		
		for (ImmanenceTileEntity ITE : blocks.keySet())
		{
			if (ITE.IsInventory)
			{
				tmp += ITE.MaxItems;
			}
		}
		
		return tmp;
	}

}

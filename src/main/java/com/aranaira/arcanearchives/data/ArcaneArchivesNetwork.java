package com.aranaira.arcanearchives.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.blocks.BlockTemplate;
import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
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
	
	public Map<ImmanenceTileEntity, String> getBlocks()
	{
		return blocks;
	}
	
	public void AddBlockToNetwork(String blockName, ImmanenceTileEntity tileEntityInstance)
	{
		if (IsBlockPosAvailable(tileEntityInstance.blockpos))
		{
			ArcaneArchives.logger.info(tileEntityInstance.toString());
			blocks.put(tileEntityInstance, blockName);
			tileEntityInstance.hasBeenAddedToNetwork = true;
			NeedsToBeUpdated = true;
			UpdateImmanence();
		}
	}
	
	public boolean IsBlockPosAvailable(BlockPos pos)
	{
		for (ImmanenceTileEntity ITE : blocks.keySet())
		{
			if (ITE.blockpos == pos)
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
}

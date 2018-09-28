package com.aranaira.arcanearchives.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;

public class ArcaneArchivesNetwork implements INBTSerializable<NBTTagCompound>
{
	private UUID playerId;
	private AAWorldSavedData parent;
	
	private Map<String, BlockPos> blocks = new HashMap<>();

	private ArcaneArchivesNetwork()
	{
		
	}
	
	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound tagCompound = new NBTTagCompound();
		tagCompound.setUniqueId("playerId", playerId);
		NBTTagList blockData = new NBTTagList();
		
		for (String blockname : blocks.keySet())
		{
			NBTTagCompound nbttagc = new NBTTagCompound();
			nbttagc.setString("name", blockname);
			nbttagc.setLong("blockpos", this.blocks.get(blockname).toLong());
		}
		
		tagCompound.setTag("blocks", blockData);
		
		return tagCompound;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		this.playerId = nbt.getUniqueId("playerId");
		NBTTagList blocks = nbt.getTagList("blocks", 10);
		for (int i = 0; i < blocks.tagCount(); i++)
		{
			NBTTagCompound data = blocks.getCompoundTagAt(i);
			this.blocks.put(data.getString("name"), BlockPos.fromLong(data.getLong("blockpos")));
		}
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

package com.aranaira.arcanearchives.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.aranaira.arcanearchives.ArcaneArchives;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;

public class ArcaneArchivesNetwork implements INBTSerializable<NBTTagCompound>
{
	private UUID playerId;
	private AAWorldSavedData parent;
	
	private Map<BlockPos, String> blocks = new HashMap<>();

	private ArcaneArchivesNetwork()
	{
		
	}
	
	public Map<BlockPos, String> getBlocks()
	{
		return blocks;
	}
	
	public void AddBlockToNetwork(String blockName, BlockPos blockpos)
	{
		blocks.put(blockpos, blockName);
	}
	
	public void RemoveBlockFromNetwork(BlockPos blockpos)
	{
		blocks.remove(blockpos);
	}
	
	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound tagCompound = new NBTTagCompound();
		tagCompound.setUniqueId("playerId", playerId);
		NBTTagList blockData = new NBTTagList();
		
		for (BlockPos blockpos : blocks.keySet())
		{
			NBTTagCompound nbttagc = new NBTTagCompound();
			nbttagc.setLong("blockpos", blockpos.toLong());
			nbttagc.setString("name", this.blocks.get(blockpos));
			blockData.appendTag(nbttagc);
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
			BlockPos pos = BlockPos.fromLong(data.getLong("blockpos"));
			String str = data.getString("name");
			
			//TODO: GET A BETTER WAY OF DOING THIS | DONT EVEN KNOW IF THIS WORKS
			if (Block.getBlockById(Block.getStateId(Minecraft.getMinecraft().world.getBlockState(pos))).getRegistryName().toString() != str)
			{
				continue;
			}
			this.blocks.put(BlockPos.fromLong(data.getLong("blockpos")), data.getString("name"));
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

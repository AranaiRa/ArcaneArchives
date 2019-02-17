package com.aranaira.arcanearchives.util.types;

import com.aranaira.arcanearchives.data.NetworkTags;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ManifestEntry
{
	public ItemStack stack;
	public int dimension;
	public List<ItemEntry> itemEntries;
	public List<ItemEntry> consolidated;

	public ManifestEntry(@Nonnull ItemStack stack, @Nonnull int dimension, @Nonnull List<ItemEntry> itemEntries)
	{
		this.stack = stack;
		this.dimension = dimension;
		this.itemEntries = itemEntries;
		this.consolidated = new ArrayList<>();
	}

	public ItemStack getStack()
	{
		return stack;
	}

	public int getDimension()
	{
		return dimension;
	}

	public List<ItemEntry> getEntries()
	{
		return itemEntries;
	}

	public List<Vec3d> getVecPositions()
	{
		return itemEntries.stream().map(ItemEntry::asVec3d).collect(Collectors.toList());
	}

	public List<ItemEntry> consolidateEntries(boolean force)
	{
		if(consolidated.isEmpty() || force)
		{
			consolidated.clear();

			Map<BlockPos, Integer> workspace = new HashMap<>();

			for(ItemEntry entry : getEntries())
			{
				if(workspace.containsKey(entry.entryPos))
				{
					consolidated.get(workspace.get(entry.entryPos)).itemCount += entry.itemCount;
				} else
				{
					consolidated.add(entry);
					workspace.put(entry.entryPos, consolidated.size() - 1);
				}
			}
		}

		return consolidated;
	}

	public static class ItemEntry
	{
		public BlockPos entryPos;
		public String chestName;
		public int itemCount;

		public ItemEntry(BlockPos pos, String chestName, int count)
		{
			this.entryPos = pos;
			this.chestName = chestName;
			this.itemCount = count;
		}

		public static ItemEntry deserializeNBT(NBTTagCompound tag)
		{
			BlockPos pos = BlockPos.fromLong(tag.getLong(NetworkTags.ENTRY_POS));
			String chestName = tag.getString(NetworkTags.CHEST_NAME);
			int itemCount = tag.getInteger(NetworkTags.ITEM_COUNT);
			return new ItemEntry(pos, chestName, itemCount);
		}

		public Vec3d asVec3d()
		{
			return new Vec3d(entryPos.getX(), entryPos.getY(), entryPos.getZ());
		}

		public String getChestName()
		{
			return chestName;
		}

		public int getItemCount()
		{
			return itemCount;
		}

		public BlockPos getPosition()
		{
			return entryPos;
		}

		public NBTTagCompound serializeNBT()
		{
			NBTTagCompound thisEntry = new NBTTagCompound();
			thisEntry.setInteger(NetworkTags.ITEM_COUNT, itemCount);
			thisEntry.setLong(NetworkTags.ENTRY_POS, entryPos.toLong());
			thisEntry.setString(NetworkTags.CHEST_NAME, chestName);
			return thisEntry;
		}
	}
}

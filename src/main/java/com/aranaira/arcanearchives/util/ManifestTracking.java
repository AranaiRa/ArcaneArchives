package com.aranaira.arcanearchives.util;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.util.types.ManifestEntry;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.longs.Long2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = ArcaneArchives.MODID)
public class ManifestTracking
{
	private static Int2ObjectArrayMap<Long2ObjectArrayMap<IntArrayList>> bimapReference = new Int2ObjectArrayMap<>();
	private static Int2ObjectArrayMap<Long2ObjectArrayMap<Int2ObjectArrayMap<List<NBTTagCompound>>>> nbtTags = new Int2ObjectArrayMap<>();

	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
	{
		clear();
	}

	private static Long2ObjectArrayMap<IntArrayList> getDimension(int dimension)
	{
		return bimapReference.computeIfAbsent(dimension, Long2ObjectArrayMap::new);
	}

	private static Long2ObjectArrayMap<Int2ObjectArrayMap<List<NBTTagCompound>>> getTagDimension(int dimension)
	{
		return nbtTags.computeIfAbsent(dimension, Long2ObjectArrayMap::new);
	}

	public static void add(ManifestEntry entry)
	{
		for(ManifestEntry.ItemEntry innerEntry : entry.getEntries())
		{
			add(entry.getStack(), entry.getDimension(), innerEntry.getPosition());
		}
	}

	public static void add(ItemStack stack, int dimension, BlockPos pos)
	{
		int item = RecipeItemHelper.pack(stack);

		NBTTagCompound tag = stack.getTagCompound();

		IntArrayList dim = getDimension(dimension).getOrDefault(pos.toLong(), null);
		if(dim == null)
		{
			dim = new IntArrayList();
			getDimension(dimension).put(pos.toLong(), dim);
			if(tag != null && !tag.isEmpty())
			{
				Int2ObjectArrayMap<List<NBTTagCompound>> map = getTagDimension(dimension).computeIfAbsent(pos.toLong(), aLong -> new Int2ObjectArrayMap<>());
				List<NBTTagCompound> list;
				if (map.containsKey(item)) {
					list = map.get(item);
				} else {
					list = new ObjectArrayList<>();
					map.put(item, list);
				}
				list.add(tag);
			}
		}
		dim.add(item);
	}

	public static void remove(int dimension, BlockPos pos)
	{
		remove(dimension, pos.toLong());
	}

	public static void remove(int dimension, long pos)
	{
		Long2ObjectArrayMap<IntArrayList> dim = getDimension(dimension);
		if(dim.containsKey(pos))
		{
			dim.remove(pos);
		}
		if(nbtTags.containsKey(dimension))
		{
			Long2ObjectArrayMap<Int2ObjectArrayMap<List<NBTTagCompound>>> map = nbtTags.get(dimension);
			if(map.containsKey(pos))
			{
				map.remove(pos);
			}
		}
	}

	@Nullable
	public static IntArrayList get(int dimension, BlockPos pos)
	{
		return getDimension(dimension).getOrDefault(pos.toLong(), null);
	}

	@Nullable
	public static Int2ObjectArrayMap<List<NBTTagCompound>> getTags(int dimension, BlockPos pos)
	{
		if(!nbtTags.containsKey(dimension)) return null;

		Long2ObjectArrayMap<Int2ObjectArrayMap<List<NBTTagCompound>>> map = nbtTags.get(dimension);

		return map.getOrDefault(pos.toLong(), null);
	}

	public static void clear()
	{
		bimapReference.clear();
		nbtTags.clear();
	}

}

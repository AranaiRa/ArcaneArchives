package com.aranaira.arcanearchives.util;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.util.types.ManifestEntry;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.longs.Long2ObjectArrayMap;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

@Mod.EventBusSubscriber(modid = ArcaneArchives.MODID)
public class ManifestTracking
{
	private static Int2ObjectArrayMap<Long2ObjectArrayMap<IntArrayList>> bimapReference = new Int2ObjectArrayMap<>();

	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
	{
		clear();
	}

	private static Long2ObjectArrayMap<IntArrayList> getDimension(int dimension)
	{
		if(!bimapReference.containsKey(dimension))
		{
			bimapReference.put(dimension, new Long2ObjectArrayMap<>());
		}

		return bimapReference.get(dimension);
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
		IntArrayList dim = getDimension(dimension).getOrDefault(pos.toLong(), null);
		if(dim == null)
		{
			dim = new IntArrayList();
			getDimension(dimension).put(pos.toLong(), dim);
		}
		dim.add(item);
	}

	public static void remove(int dimension, BlockPos pos)
	{
		remove(dimension, pos.toLong());
	}

	public static void remove (int dimension, long pos) {
		Long2ObjectArrayMap<IntArrayList> dim = getDimension(dimension);
		if(dim.containsKey(pos))
		{
			dim.remove(pos);
		}
	}

	public static IntArrayList get(int dimension, BlockPos pos)
	{
		return getDimension(dimension).getOrDefault(pos.toLong(), null);
	}

	public static void clear () {
		bimapReference.clear();
	}

}

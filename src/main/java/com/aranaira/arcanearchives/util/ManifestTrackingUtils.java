package com.aranaira.arcanearchives.util;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.util.ManifestUtils.CollatedEntry;
import com.aranaira.arcanearchives.util.ManifestUtils.EntryDescriptor;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.crafting.IngredientNBT;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = ArcaneArchives.MODID)
public class ManifestTrackingUtils {
	private static Int2ObjectOpenHashMap<Long2ObjectOpenHashMap<List<ItemStack>>> reference = new Int2ObjectOpenHashMap<>();
	private static List<ItemStack> allTracked = new ArrayList<>();

	@SubscribeEvent
	public static void onPlayerLoggedIn (PlayerEvent.PlayerLoggedInEvent event) {
		clear();
	}

	public static void clear () {
		reference.clear();
		allTracked.clear();
	}

	public static void add (CollatedEntry entry) {
		for (EntryDescriptor innerEntry : entry.descriptions) {
			add(entry.getStack(), innerEntry.dimension, innerEntry.pos);
		}
	}

	public static void add (ItemStack stack, int dimension, BlockPos pos) {
		List<ItemStack> dim = getDimension(dimension).getOrDefault(pos.toLong(), null);
		if (dim == null) {
			dim = new ArrayList<>();
			getDimension(dimension).put(pos.toLong(), dim);
		}

		dim.add(stack);
		allTracked.add(stack);
	}

	private static Long2ObjectOpenHashMap<List<ItemStack>> getDimension (int dimension) {
		return reference.computeIfAbsent(dimension, Long2ObjectOpenHashMap::new);
	}

	public static void remove (int dimension, BlockPos pos) {
		remove(dimension, pos.toLong());
	}

	public static void remove (int dimension, long pos) {
		Long2ObjectOpenHashMap<List<ItemStack>> dim = getDimension(dimension);
		if (dim.containsKey(pos)) {
			dim.remove(pos);
		}
	}

	@Nullable
	public static List<ItemStack> get (int dimension, BlockPos pos) {
		return getDimension(dimension).getOrDefault(pos.toLong(), null);
	}

	public static List<ItemStack> getAllTracked () {
		return allTracked;
	}

	public static boolean matches (ItemStack stack) {
		return matches(stack, allTracked);
	}

	public static boolean matches (ItemStack stack, List<ItemStack> ingredients) {
		if (ingredients.isEmpty()) {
			return false;
		}

		if (stack.isEmpty()) {
			return false;
		}

		for (ItemStack otherStack : ingredients) {
			if (ItemUtils.areStacksEqualIgnoreSize(stack, otherStack)) {
				return true;
			}
		}

		return false;
	}
}

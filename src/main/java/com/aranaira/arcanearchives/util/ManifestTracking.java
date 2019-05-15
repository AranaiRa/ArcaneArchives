package com.aranaira.arcanearchives.util;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.util.types.ManifestEntry;
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
public class ManifestTracking {
	private static Int2ObjectOpenHashMap<Long2ObjectOpenHashMap<List<Ingredient>>> reference = new Int2ObjectOpenHashMap<>();
	private static List<Ingredient> allTracked = new ArrayList<>();

	@SubscribeEvent
	public static void onPlayerLoggedIn (PlayerEvent.PlayerLoggedInEvent event) {
		clear();
	}

	public static void clear () {
		reference.clear();
		allTracked.clear();
	}

	public static void add (ManifestEntry entry) {
		for (ManifestEntry.ItemEntry innerEntry : entry.getEntries()) {
			add(entry.getStack(), entry.getDimension(), innerEntry.getPosition());
		}
	}

	public static void add (ItemStack stack, int dimension, BlockPos pos) {
		NBTTagCompound tag = stack.getTagCompound();

		List<Ingredient> dim = getDimension(dimension).getOrDefault(pos.toLong(), null);
		if (dim == null) {
			dim = new ArrayList<>();
			getDimension(dimension).put(pos.toLong(), dim);
		}

		Ingredient ing;

		if (tag == null || tag.isEmpty()) {
			ing = Ingredient.fromStacks(stack);
		} else {
			ing = IngredientNBT.fromStacks(stack);
		}

		dim.add(ing);
		allTracked.add(ing);
	}

	private static Long2ObjectOpenHashMap<List<Ingredient>> getDimension (int dimension) {
		return reference.computeIfAbsent(dimension, Long2ObjectOpenHashMap::new);
	}

	public static void remove (int dimension, BlockPos pos) {
		remove(dimension, pos.toLong());
	}

	public static void remove (int dimension, long pos) {
		Long2ObjectOpenHashMap<List<Ingredient>> dim = getDimension(dimension);
		if (dim.containsKey(pos)) {
			dim.remove(pos);
		}
	}

	@Nullable
	public static List<Ingredient> get (int dimension, BlockPos pos) {
		return getDimension(dimension).getOrDefault(pos.toLong(), null);
	}

	public static List<Ingredient> getAllTracked () {
		return allTracked;
	}

	public static boolean matches (ItemStack stack) {
		return matches(stack, allTracked);
	}

	public static boolean matches (ItemStack stack, List<Ingredient> ingredients) {
		if (ingredients.isEmpty()) {
			return false;
		}

		if (stack.isEmpty()) {
			return false;
		}

		for (Ingredient ing : ingredients) {
			if (ing.apply(stack)) {
				return true;
			}
		}

		return false;
	}
}

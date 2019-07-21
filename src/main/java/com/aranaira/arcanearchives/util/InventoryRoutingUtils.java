package com.aranaira.arcanearchives.util;

import com.aranaira.arcanearchives.data.HiveNetwork;
import com.aranaira.arcanearchives.data.ServerNetwork;
import com.aranaira.arcanearchives.tileentities.BrazierTileEntity;
import com.aranaira.arcanearchives.tileentities.IBrazierRouting;
import com.aranaira.arcanearchives.tileentities.IBrazierRouting.BrazierRoutingType;
import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import com.aranaira.arcanearchives.types.IteRef;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

public class InventoryRoutingUtils {
	/*
	 * Returns whether or not this inventory can *probably* contain an item. NBT checking
	 * is not performed at this stage.
	 */
	public static int calculateWeight (IBrazierRouting inventory, ItemStack stack) {
		Int2IntOpenHashMap packedMap = inventory.getOrCalculateReference();
		BrazierRoutingType type = inventory.getRoutingType();
		int packed = RecipeItemHelper.pack(stack);
		int total = packedMap.get(packed);

		int troveScore = inventory.troveScore(stack);
		if (troveScore != -1) {
			return troveScore;
		}
		if ((type == BrazierRoutingType.NO_NEW_STACKS || type == BrazierRoutingType.GCT)) {
			if (total == 0) {
				return -1;
			} else if (type == BrazierRoutingType.NO_NEW_STACKS) {
				return 200;
			} else if (type == BrazierRoutingType.GCT) {
				return 400;
			}
		} else if (type == BrazierRoutingType.PRIORITY && total > 0) {
			return 250;
		}
		// Otherwise weight is calculated as a value between 0 and 200
		// Factors considered positively that increase weight: quantity
		// of similar items already stored.
		// Quantity of empty slots.
		int empty = inventory.countEmptySlots();
		int slotCount = inventory.totalSlots();
		if (empty == slotCount) {
			return 0;
		}

		int stackSize = stack.getMaxStackSize();
		if (inventory.slotMultiplier() > 1) {
			stackSize = (stackSize == 1) ? 1 : stackSize * inventory.slotMultiplier();
		}

		int usedSlots;

		if (total < stackSize && total > 0) {
			usedSlots = 1;
		} else {
			usedSlots = (int) Math.floor((double) total / (double) stackSize);
		}

		if (usedSlots == 0) {
			return slotCount - empty;
		}

		int percentageUsed = (int) Math.floor(((double) usedSlots / (double) slotCount) * 100) * 2;
		int percentageEmpty = (int) Math.floor(((double) empty / (double) slotCount) * 100);

		return percentageEmpty + percentageUsed;
	}

	public static List<WeightedEntry<IBrazierRouting>> buildNetworkWeights (BrazierTileEntity brazier, ItemStack stack) {
		List<WeightedEntry<IBrazierRouting>> workspace = new ArrayList<>();
		int radius = brazier.getRadius() * brazier.getRadius();
		BlockPos bPos = brazier.getPos();
		ServerNetwork network = brazier.getServerNetwork();
		network.refreshTiles();
		Iterable<IteRef> tiles;
		if (!network.isHiveMember() || brazier.getNetworkMode()) {
			tiles = network.getValidTiles();
		} else {
			HiveNetwork hive = network.getHiveNetwork();
			tiles = hive.getValidTiles();
		}
		if (tiles == null) return workspace;
		for (IteRef ite : tiles) {
			if (IBrazierRouting.class.isAssignableFrom(ite.clazz) && network.distanceSq(bPos, ite.pos) <= radius && ite.dimension == brazier.dimension) {
				ImmanenceTileEntity tile = ite.getTile();
				int weight = calculateWeight((IBrazierRouting) tile, stack);
				workspace.add(new WeightedEntry<>((IBrazierRouting) tile, weight));
			}
		}

		workspace.sort((o1, o2) -> Integer.compare(o2.weight, o1.weight));
		return workspace;
	}

	public static List<IBrazierRouting> buildNetwork (BrazierTileEntity brazier, ItemStack stack) {
		return buildNetworkWeights(brazier, stack).stream().filter(r -> r.weight > 0).map(r -> r.entry).collect(Collectors.toList());
	}

	/**
	 * @param brazier The TileEntity of the brazier currently requesting information.
	 * @param inputs
	 * @return
	 */
	public static List<ItemStack> tryInsertItems (BrazierTileEntity brazier, ItemStack reference, List<ItemStack> inputs) {
		List<IBrazierRouting> routing = buildNetwork(brazier, reference);
		routes:
		for (IBrazierRouting route : routing) {
			ListIterator<ItemStack> iterator = inputs.listIterator();
			while (iterator.hasNext()) {
				ItemStack potential = iterator.next();
				iterator.remove();

				ItemStack result = route.acceptStack(potential);
				if (!result.isEmpty()) {
					iterator.add(result);
					continue routes;
				}
			}
		}
		return inputs;
	}

	public static List<ItemStack> tryInsertItems (BrazierTileEntity brazier, ItemStack reference) {
		return tryInsertItems(brazier, reference, Lists.newArrayList(reference));
	}

	public static class WeightedEntry<T> {
		public T entry;
		public int weight;

		public WeightedEntry (T entry, int weight) {
			this.entry = entry;
			this.weight = weight;
		}
	}
}

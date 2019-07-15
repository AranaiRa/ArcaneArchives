package com.aranaira.arcanearchives.util;

import com.aranaira.arcanearchives.data.ServerNetwork;
import com.aranaira.arcanearchives.tileentities.BrazierTileEntity;
import com.aranaira.arcanearchives.tileentities.IBrazierRouting;
import com.aranaira.arcanearchives.tileentities.IBrazierRouting.BrazierRoutingType;
import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import com.aranaira.arcanearchives.util.types.IteRef;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import java.util.*;
import java.util.stream.Collectors;

public class InventoryRouting {
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
		int empty = inventory.totalEmptySlots();
		int slotCount = inventory.totalSlots();

		int stackSize = stack.getMaxStackSize();
		if (inventory.slotMultiplier() > 1) {
			stackSize = (stackSize == 1) ? 1 : stackSize * inventory.slotMultiplier();
		}

		int usedSlots = (int) Math.floor((double) slotCount / (double) stackSize);

		int percentageUsed = (int) Math.floor(((double) slotCount / (double) usedSlots) * 100);
		int percentageEmpty = (int) Math.floor(((double) empty / (double) usedSlots) * 100);

		return percentageEmpty + percentageUsed;
	}

	public static List<IBrazierRouting> buildNetwork (BrazierTileEntity brazier, ServerNetwork network, ItemStack stack) {
		List<WeightedEntry<IBrazierRouting>> workspace = new ArrayList<>();
		int radius = brazier.getRadius() * brazier.getRadius();
		BlockPos bPos = brazier.getPos();
		network.refreshTiles();
		for (IteRef ite : network.getValidTiles()) {
			if (IBrazierRouting.class.isAssignableFrom(ite.clazz) && network.distanceSq(bPos, ite.pos) <= radius && ite.dimension == brazier.dimension) {
				ImmanenceTileEntity tile = ite.getTile();
				int weight = calculateWeight((IBrazierRouting) tile, stack);
				if (weight <= -1) continue;
				workspace.add(new WeightedEntry<>((IBrazierRouting) tile, weight));
			}
		}

		workspace.sort((o1, o2) -> Integer.compare(o2.weight, o1.weight));
		return workspace.stream().map(o -> o.entry).collect(Collectors.toList());
	}

	/**
	 *
	 * @param brazier The TileEntity of the brazier currently requesting information.
	 * @param network
	 * @param inputs
	 * @return
	 */
	public static List<ItemStack> tryInsertItems (BrazierTileEntity brazier, ServerNetwork network, ItemStack reference, List<ItemStack> inputs) {
		List<IBrazierRouting> routing = buildNetwork(brazier, network, reference);
		routes: for (IBrazierRouting route : routing) {
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

	public static List<ItemStack> tryInsertItems (BrazierTileEntity brazier, ServerNetwork network, ItemStack reference) {
		return tryInsertItems(brazier, network, reference, Collections.singletonList(reference));
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

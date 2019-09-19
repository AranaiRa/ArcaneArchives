package com.aranaira.arcanearchives.util;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.data.types.HiveNetwork;
import com.aranaira.arcanearchives.data.types.ServerNetwork;
import com.aranaira.arcanearchives.tileentities.BrazierTileEntity;
import com.aranaira.arcanearchives.tileentities.BrazierTileEntity.ItemCache;
import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import com.aranaira.arcanearchives.tileentities.interfaces.IBrazierRouting;
import com.aranaira.arcanearchives.tileentities.interfaces.IBrazierRouting.BrazierRoutingType;
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
				return 4999;
			} else {
				return 5000;
			}
		}

		int empty = inventory.countEmptySlots();
		int slotCount = inventory.totalSlots();
		// Completely empty chest
		if (empty == slotCount) {
			return 0;
		}

		// Doesn't have any of the items
		if (total == 0) {
			return slotCount - empty;
		}

		int stackSize = stack.getMaxStackSize() == 1 ? 1 : stack.getMaxStackSize() * inventory.slotMultiplier();

		double potentialSlots = stackSize * slotCount;
		double result = Math.ceil((((double) total / potentialSlots) * 1000) + 500);
		return (int) result;

	}

	public static List<WeightedEntry<IBrazierRouting>> buildNetworkWeights (BrazierTileEntity brazier, ItemStack stack, boolean simulate) {
		List<WeightedEntry<IBrazierRouting>> workspace = new ArrayList<>();
		int radius = brazier.getRadius() * brazier.getRadius();
		BlockPos bPos = brazier.getPos();
		ServerNetwork network = brazier.getServerNetwork();
		if (network == null) {
			ArcaneArchives.logger.error("Brazier with tile id " + brazier.getUuid().toString() + " has an invalid server network; cannot build network weights.");
			return workspace;
		}
		Iterable<IteRef> tiles;
		if (!network.isHiveMember() || brazier.getNetworkMode()) {
			tiles = network.getValidTiles();
		} else {
			HiveNetwork hive = network.getHiveNetwork();
			if (hive == null) {
				ArcaneArchives.logger.error("Server Network with id " + network.getUuid().toString() + " is part of a hive but no hive network is available; cannot build network weights.");
				return workspace;
			}
			tiles = hive.getValidTiles();
		}
		if (tiles == null) {
			return workspace;
		}
		for (IteRef ite : tiles) {
			if (IBrazierRouting.class.isAssignableFrom(ite.clazz) && network.distanceSqNoVertical(bPos, ite.pos) <= radius && ite.dimension == brazier.dimension) {
				ImmanenceTileEntity tile = ite.getTile();
				if (tile == null) {
					continue;
				}
				int weight = calculateWeight((IBrazierRouting) tile, stack);
				workspace.add(new WeightedEntry<>((IBrazierRouting) tile, weight));
			}
		}

		workspace.sort((o1, o2) -> Integer.compare(o2.weight, o1.weight));
		return workspace;
	}

	public static List<IBrazierRouting> buildNetwork (BrazierTileEntity brazier, ItemStack stack, boolean simulate) {
		return buildNetworkWeights(brazier, stack, simulate).stream().filter(r -> r.weight >= 0).map(r -> r.entry).collect(Collectors.toList());
	}

	/**
	 * @param brazier The TileEntity of the brazier currently requesting information.
	 * @param inputs
	 * @return
	 */
	public static List<ItemStack> tryInsertItems (BrazierTileEntity brazier, ItemStack reference, List<ItemStack> inputs, boolean simulate) {
		// Considered the cached value in the brazier
		ItemCache cached = brazier.getCachedEntry(reference);
		if (cached != null && cached.valid()) {
			List<IBrazierRouting> cacheList = new ArrayList<>();
			IBrazierRouting routing = cached.getRoute();
			if (routing == null) {
				// Normally this is a bad sign
				ServerNetwork network = brazier.getServerNetwork();
				if (network != null) {
					ImmanenceTileEntity ite = network.getImmanenceTile(cached.getTileId());
					if (ite instanceof IBrazierRouting) {
						cacheList.add((IBrazierRouting) ite);
					}
				}
			} else {
				cacheList.add(routing);
			}
			if (!cacheList.isEmpty()) {
				inputs = tryInsertItems(cacheList, brazier, reference, inputs, simulate);
				if (inputs.isEmpty()) {
					return inputs;
				}
			}
		}

		List<IBrazierRouting> routing = buildNetwork(brazier, reference, simulate);
		return tryInsertItems(routing, brazier, reference, inputs, simulate);
	}

	public static List<ItemStack> tryInsertItems (List<IBrazierRouting> routing, BrazierTileEntity brazier, ItemStack reference, List<ItemStack> inputs, boolean simulate) {
		routes:
		for (IBrazierRouting route : routing) {
			ListIterator<ItemStack> iterator = inputs.listIterator();
			while (iterator.hasNext()) {
				ItemStack potential = iterator.next();
				iterator.remove();

				ItemStack result = route.acceptStack(potential, simulate);
				if (!result.isEmpty()) {
					iterator.add(result);
					continue routes;
				} else {
					brazier.cacheInsertion(reference, route, route.getUuid());
				}
			}
		}
		return inputs;
	}

	public static List<ItemStack> tryInsertItems (BrazierTileEntity brazier, ItemStack reference, boolean simulate) {
		return tryInsertItems(brazier, reference, Lists.newArrayList(reference), simulate);
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

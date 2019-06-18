package com.aranaira.arcanearchives.capabilities.tracking;

import com.aranaira.arcanearchives.inventory.handlers.TroveItemHandler;
import com.aranaira.arcanearchives.util.ItemUtilities;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class ItemTrackingTrove implements IItemTracking {
	private final TroveItemHandler handler;

	public ItemTrackingTrove (TroveItemHandler handler) {
		this.handler = handler;
	}

	public ItemTrackingTrove () {
		this.handler = null;
	}

	@Override
	public int emptySlots () {
		return handler.getMaxCount() - handler.getCount();
	}

	@Override
	public boolean contains (ItemStack stack) {
		return ItemUtilities.areStacksEqualIgnoreSize(handler.getItem(), stack);
	}

	@Override
	public boolean contains (int packed) {
		return handler.getPacked() == packed;
	}

	@Override
	public int quantity (ItemStack stack) {
		if (ItemUtilities.areStacksEqualIgnoreSize(handler.getItem(), stack)) {
			return handler.getCount();
		}
		return 0;
	}

	@Override
	public int quantity (int packed) {
		if (handler.getPacked() == packed) {
			return handler.getCount();
		}
		return 0;
	}

	@Override
	public IItemHandler internal () {
		return this.handler;
	}
}

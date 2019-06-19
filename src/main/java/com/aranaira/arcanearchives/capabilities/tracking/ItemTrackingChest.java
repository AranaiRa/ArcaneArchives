package com.aranaira.arcanearchives.capabilities.tracking;

import com.aranaira.arcanearchives.tileentities.RadiantChestTileEntity.RadiantChestHandler;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class ItemTrackingChest implements IItemTracking {
	public final RadiantChestHandler handler;

	public ItemTrackingChest (RadiantChestHandler handler) {
		this.handler = handler;
	}

	public ItemTrackingChest () {
		this.handler = null;
	}

	@Override
	public boolean contains (ItemStack stack) {
		return this.handler.itemReference.containsKey(RecipeItemHelper.pack(stack));
	}

	@Override
	public boolean contains (int packed) {
		return this.handler.itemReference.containsKey(packed);
	}

	@Override
	public int quantity (ItemStack stack) {
		return this.handler.itemReference.get(RecipeItemHelper.pack(stack));
	}

	@Override
	public int quantity (int packed) {
		return this.handler.itemReference.get(packed);
	}

	@Override
	public IItemHandler internal () {
		return this.handler;
	}
}

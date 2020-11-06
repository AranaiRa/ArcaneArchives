package com.aranaira.arcanearchives.inventory.handlers;

import com.aranaira.arcanearchives.tileentities.RadiantCraftingTableTileEntity;
import net.minecraft.inventory.container.Container;
import net.minecraftforge.items.ItemStackHandler;

public class InventoryCraftingPersistent extends InventoryCraftingItemHandler<RadiantCraftingTableTileEntity, ItemStackHandler> {
	public InventoryCraftingPersistent (Container eventHandler, ItemStackHandler parent, RadiantCraftingTableTileEntity tile, int width, int height) {
		super(eventHandler, parent, tile, width, height);
	}
}


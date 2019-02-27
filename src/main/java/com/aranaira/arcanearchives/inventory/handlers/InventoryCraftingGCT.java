package com.aranaira.arcanearchives.inventory.handlers;

import com.aranaira.arcanearchives.tileentities.GemCuttersTableTileEntity;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.InvWrapper;

public class InventoryCraftingGCT extends InventoryCraftingItemHandler<GemCuttersTableTileEntity>
{
	private ItemStackHandler tileInventory;
	private InventoryPlayer playerInventory;
	private CombinedInvWrapper wrappedInventory;

	private InventoryCraftingGCT(Container eventHandler, GemCuttersTableTileEntity tile, CombinedInvWrapper inventory, ItemStackHandler parent, InventoryPlayer playerInventory, int width, int height)
	{
		super(eventHandler, parent, tile, width, height);

		this.wrappedInventory = inventory;
		this.playerInventory = playerInventory;
		this.tileInventory = parent;
	}

	public static InventoryCraftingGCT build (Container eventHandler, GemCuttersTableTileEntity tile, ItemStackHandler tileInventory, InventoryPlayer playerInventory) {
		InvWrapper wrappedPlayerInventory = new InvWrapper(playerInventory);
		CombinedInvWrapper wrappedInventories = new CombinedInvWrapper(tileInventory, wrappedPlayerInventory);

		return new InventoryCraftingGCT(eventHandler, tile, wrappedInventories, tileInventory, playerInventory, 9, 6);
	}
}

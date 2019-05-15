package com.aranaira.arcanearchives.inventory.slots;

import com.aranaira.arcanearchives.inventory.ContainerRadiantCraftingTable;
import com.aranaira.arcanearchives.tileentities.RadiantCraftingTableTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotIRecipe extends Slot {
	private static IInventory emptyInventory = new InventoryBasic("[Null]", true, 0);
	private ContainerRadiantCraftingTable container;
	private RadiantCraftingTableTileEntity tile;
	private EntityPlayer player;
	private int recipe;

	public SlotIRecipe (ContainerRadiantCraftingTable container, int index, RadiantCraftingTableTileEntity tile, EntityPlayer player, int recipe, int xPosition, int yPosition) {
		super(emptyInventory, index, xPosition, yPosition);

		this.container = container;
		this.tile = tile;
		this.player = player;
		this.recipe = recipe;
	}

	@Override
	public boolean isItemValid (ItemStack stack) {
		return false;
	}

	@Override
	public ItemStack getStack () {
		if (tile.getRecipe(recipe) != null) {
			return tile.getRecipe(recipe).getRecipeOutput().copy();
		} else {
			return ItemStack.EMPTY;
		}
	}

	@Override
	public boolean getHasStack () {
		return !getStack().isEmpty();
	}

	@Override
	public void putStack (ItemStack stack) {
	}

	@Override
	public ItemStack decrStackSize (int amount) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canTakeStack (EntityPlayer playerIn) {
		return false;
	}

	public int getRecipeIndex () {
		return recipe;
	}
}

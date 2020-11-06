package com.aranaira.arcanearchives.inventory.slots;

import com.aranaira.arcanearchives.inventory.ContainerRadiantCraftingTable;
import com.aranaira.arcanearchives.inventory.handlers.InventoryCraftingPersistent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class SlotCraftingFastWorkbench extends SlotCrafting {
	private final EntityPlayer player;
	private final ContainerRadiantCraftingTable containerCraftingStation;
	private final InventoryCraftingPersistent craftMatrixPersistent;

	public SlotCraftingFastWorkbench (ContainerRadiantCraftingTable containerCraftingStation, EntityPlayer player, InventoryCraftingPersistent craftingInventory, IInventory inventoryIn, int slotIndex, int xPosition, int yPosition) {
		super(player, craftingInventory, inventoryIn, slotIndex, xPosition, yPosition);
		this.containerCraftingStation = containerCraftingStation;
		this.craftMatrixPersistent = craftingInventory;
		this.player = player;
	}

	@Override
	public ItemStack decrStackSize (int amount) {
		if (this.getHasStack()) {
			this.amountCrafted += Math.min(amount, this.getStack().getCount());
		}

		return super.decrStackSize(amount);
	}

	@Override
	protected void onCrafting (ItemStack stack) {
		if (this.amountCrafted > 0) {
			stack.onCrafting(this.player.world, this.player, this.amountCrafted);
			containerCraftingStation.saveLastRecipe();
			FMLCommonHandler.instance().firePlayerCraftingEvent(this.player, stack, craftMatrix);
		}

		this.amountCrafted = 0;
	}

	@Override
	public ItemStack onTake (EntityPlayer thePlayer, ItemStack stack) {
		this.onCrafting(stack);
		net.minecraftforge.common.ForgeHooks.setCraftingPlayer(thePlayer);
		NonNullList<ItemStack> nonnulllist = containerCraftingStation.getRemainingIngredients();
		net.minecraftforge.common.ForgeHooks.setCraftingPlayer(null);

		craftMatrixPersistent.setDoNotCallUpdates(true);

		for (int i = 0; i < nonnulllist.size(); ++i) {
			ItemStack itemstack = this.craftMatrix.getStackInSlot(i);
			ItemStack itemstack1 = nonnulllist.get(i);

			if (!itemstack.isEmpty()) {
				this.craftMatrix.decrStackSize(i, 1);
				itemstack = this.craftMatrix.getStackInSlot(i);
			}

			if (!itemstack1.isEmpty()) {
				if (itemstack.isEmpty()) {
					this.craftMatrix.setInventorySlotContents(i, itemstack1);
				} else if (ItemStack.areItemsEqual(itemstack, itemstack1) && ItemStack.areItemStackTagsEqual(itemstack, itemstack1)) {
					itemstack1.grow(itemstack.getCount());
					this.craftMatrix.setInventorySlotContents(i, itemstack1);
				} else if (!this.player.inventory.addItemStackToInventory(itemstack1)) {
					this.player.dropItem(itemstack1, false);
				}
			}
		}

		craftMatrixPersistent.setDoNotCallUpdates(false);
		containerCraftingStation.onCraftMatrixChanged(craftMatrixPersistent);

		return stack;
	}
}


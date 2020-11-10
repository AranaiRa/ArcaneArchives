/*package com.aranaira.arcanearchives.inventory;

import com.aranaira.arcanearchives.init.ItemRegistry;
import com.aranaira.arcanearchives.tileentities.RadiantFurnaceTileEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Slot;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.FurnaceTileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class ContainerRadiantFurnace extends Container {

	protected RadiantFurnaceTileEntity tile;
	private final PlayerEntity player;

	public ContainerRadiantFurnace (RadiantFurnaceTileEntity te, PlayerEntity player) {
		this.tile = te;
		this.player = player;

		addPlayerSlots(player.inventory);
		addFurnaceSlots(te);
	}

	private void addFurnaceSlots (RadiantFurnaceTileEntity te) {
		// Input
		this.addSlotToContainer(new SlotItemHandler(te.input, 0, 81, 27));
		// Fuel
		this.addSlotToContainer(new SlotItemHandler(te.fuel, 0, 52, 27) {
			@Override
			public boolean isItemValid (@Nonnull ItemStack stack) {
				int burnTime = FurnaceTileEntity.getItemBurnTime(stack);
				return burnTime > 0;
			}
		});
		// Output
		this.addSlotToContainer(new SlotItemHandler(te.output, 0, 110, 14) {
			@Override
			public boolean isItemValid (@Nonnull ItemStack stack) {
				return false;
			}
		});
		// Echoes
		this.addSlotToContainer(new SlotItemHandler(te.output, 1, 110, 40) {
			@Override
			public boolean isItemValid (@Nonnull ItemStack stack) {
				return false;
			}
		});
	}

	private void addPlayerSlots (PlayerInventory inventoryPlayer) {
		int xOffset = 10;
		int yOffset = 87;

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, xOffset + j * 18, yOffset + i * 18));
			}
		}
		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(inventoryPlayer, i, xOffset + i * 18, yOffset + 58));
		}
	}

	public RadiantFurnaceTileEntity getTile () {
		return this.tile;
	}

	@Override
	@Nonnull
	public ItemStack transferStackInSlot (PlayerEntity player, int index) {
		ItemStack slotStack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack stack = slot.getStack();
			slotStack = stack.copy();
			if (index < 36) { //Player Inventory -> Socket
				int burnTime = FurnaceTileEntity.getItemBurnTime(stack);
				if (burnTime > 0) {
					if (!mergeItemStack(stack, 37, 38, false)) {
						if (!mergeItemStack(stack, 36, 37, false)) {
							return ItemStack.EMPTY;
						}
					}
				} else {
					if (!mergeItemStack(stack, 36, 37, false)) {
						return ItemStack.EMPTY;
					}
				}
			} else {
				// transfer from slots to inventory
				if (!mergeItemStack(stack, 0, 36, false)) {
					return ItemStack.EMPTY;
				}
			}

			if (stack.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			}

			slot.onSlotChanged();
		}

		return slotStack;
	}

	@Override
	public void onContainerClosed (PlayerEntity playerIn) {
		super.onContainerClosed(playerIn);
	}

	@Override
	public boolean canInteractWith (PlayerEntity playerIn) {
		return true;
	}
}*/

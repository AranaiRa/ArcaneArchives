package com.aranaira.arcanearchives.inventory;

import com.aranaira.arcanearchives.tileentities.RadiantFurnaceTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class ContainerRadiantFurnace extends Container {

	protected RadiantFurnaceTileEntity tile;
	private final EntityPlayer player;

	public ContainerRadiantFurnace (RadiantFurnaceTileEntity te, EntityPlayer player) {
		this.tile = te;
		this.player = player;

		addPlayerSlots(player.inventory);
		addFurnaceSlots(te);
	}

	private void addFurnaceSlots (RadiantFurnaceTileEntity te) {
		// Input
		this.addSlotToContainer(new SlotItemHandler(te.input, 0, 81, 27) {
			/*@Override
			public boolean isItemValid (@Nonnull ItemStack stack) {
				return stack.getItem().getItemBurnTime(stack) != -1;
			}*/
		});
		// Fuel
		this.addSlotToContainer(new SlotItemHandler(te.fuel, 0, 52, 27) {
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

	private void addPlayerSlots (InventoryPlayer inventoryPlayer) {
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
	public void onContainerClosed (EntityPlayer playerIn) {
		super.onContainerClosed(playerIn);
	}

	@Override
	public boolean canInteractWith (EntityPlayer playerIn) {
		return true;
	}
}

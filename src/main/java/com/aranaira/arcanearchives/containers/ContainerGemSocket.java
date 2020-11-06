package com.aranaira.arcanearchives.inventory;

import com.aranaira.arcanearchives.inventory.handlers.GemSocketHandler;
import com.aranaira.arcanearchives.items.GemSocket;
import com.aranaira.arcanearchives.items.gems.ArcaneGemItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class ContainerGemSocket extends Container {

	private GemSocketHandler handler;
	private PlayerEntity player;
	private ItemStack socket;

	public ContainerGemSocket (PlayerEntity player) {
		//ArcaneArchives.logger.info("CONTAINER CLASS INSTANTIATED");
		this.player = player;
		socket = GemSocketHandler.findSocket(player);
		handler = GemSocketHandler.getHandler(socket);

		createPlayerInventory(player.inventory);
		createPlayerOffhand(player.inventory);
		createGemSlot();
	}

	@Override
	public boolean canInteractWith (PlayerEntity playerIn) {
		return true;
	}

	private void createGemSlot () {
		int xOffset = 81;
		int yOffset = 3;
		addSlotToContainer(new SlotItemHandler(handler.getInventory(), 0, xOffset, yOffset));
	}

	private void createPlayerInventory (PlayerInventory inventoryPlayer) {
		int xOffset = 10;
		int yOffset = 57;

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, xOffset + j * 18, yOffset + i * 18));
			}
		}
		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new net.minecraft.inventory.container.Slot(inventoryPlayer, i, xOffset + i * 18, yOffset + 58));
		}
	}

	private void createPlayerOffhand (PlayerInventory inventoryPlayer) {
		addSlotToContainer(new Slot(inventoryPlayer, 40, -23, 115));
	}

	@Override
	@Nonnull
	public ItemStack transferStackInSlot (PlayerEntity player, int index) {
		ItemStack slotStack = ItemStack.EMPTY;
		net.minecraft.inventory.container.Slot slot = inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack stack = slot.getStack();

			boolean isGem = stack.getItem() instanceof ArcaneGemItem;

			if (isGem) { //Player Inventory -> Socket
				if (index == 36) { // Off-hand to socket
					if (!mergeItemStack(stack, 37, 38, false)) {
						if (!mergeItemStack(stack, 27, 36, false)) {
							if (!mergeItemStack(stack, 0, 27, false)) {
								handler.saveToStack();
								slot.onSlotChanged();
								return ItemStack.EMPTY;
							}
						}
					}
				} else if (index == 37) { // Socket to offhand
					if (!mergeItemStack(stack, 36, 37, false)) {
						if (!mergeItemStack(stack, 27, 36, false)) {
							if (!mergeItemStack(stack, 0, 27, false)) {
								handler.saveToStack();
								slot.onSlotChanged();
								return ItemStack.EMPTY;
							}
						}
					}
				} else {
					if (!mergeItemStack(stack, 37, 38, false)) { // prioritise the socket
						if (!mergeItemStack(stack, 36, 37, false)) {
							handler.saveToStack();
							slot.onSlotChanged();
							return ItemStack.EMPTY;
						}
					} else {
						handler.saveToStack();
						slot.onSlotChanged();
						return ItemStack.EMPTY;
					}
				}
			} else {
				slot.onSlotChanged();
				return ItemStack.EMPTY;
			}
		}

		handler.saveToStack();
		return slotStack;
	}

	@Override
	@Nonnull
	public ItemStack slotClick (int slotID, int dragType, ClickType clickType, PlayerEntity player) {
		if (slotID >= 0) {
			ItemStack stack = getSlot(slotID).getStack();
			if (stack.getItem() instanceof GemSocket) {
				return ItemStack.EMPTY;
			}
		}

		return super.slotClick(slotID, dragType, clickType, player);
	}
}

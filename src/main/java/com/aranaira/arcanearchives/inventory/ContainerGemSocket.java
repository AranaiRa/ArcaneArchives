package com.aranaira.arcanearchives.inventory;

import com.aranaira.arcanearchives.inventory.handlers.GemSocketHandler;
import com.aranaira.arcanearchives.items.GemSocket;
import com.aranaira.arcanearchives.items.gems.ArcaneGemItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class ContainerGemSocket extends Container {

	private GemSocketHandler handler;
	private EntityPlayer player;
	private ItemStack socket;

	public ContainerGemSocket (EntityPlayer player) {
		//ArcaneArchives.logger.info("CONTAINER CLASS INSTANTIATED");
		this.player = player;
		socket = GemSocketHandler.findSocket(player);
		handler = GemSocketHandler.getHandler(socket);

		createPlayerInventory(player.inventory);
		createGemSlot();
	}

	@Override
	public boolean canInteractWith (EntityPlayer playerIn) {
		return true;
	}

	private void createGemSlot () {
		int xOffset = 81;
		int yOffset = 3;
		addSlotToContainer(new SlotItemHandler(handler.getInventory(), 0, xOffset, yOffset));
	}

	private void createPlayerInventory (InventoryPlayer inventoryPlayer) {
		int xOffset = 10;
		int yOffset = 57;

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, xOffset + j * 18, yOffset + i * 18));
			}
		}
		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(inventoryPlayer, i, xOffset + i * 18, yOffset + 58));
		}
	}

	@Override
	@Nonnull
	public ItemStack transferStackInSlot (EntityPlayer player, int index) {
		ItemStack slotStack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack stack = slot.getStack();
			slotStack = stack.copy();

			boolean isGem = stack.getItem() instanceof ArcaneGemItem;

			if (isGem && index < 36) { //Player Inventory -> Socket
				if (!mergeItemStack(stack, 36, 37, false)) {
					handler.saveToStack();
					return ItemStack.EMPTY;
				}
			} else {
				if (!mergeItemStack(stack, 0, 36, false)) {
					handler.saveToStack();
					return ItemStack.EMPTY;
				}
			}

			if (stack.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			}

			slot.onSlotChanged();
		}

		handler.saveToStack();
		return slotStack;
	}

	@Override
	@Nonnull
	public ItemStack slotClick (int slotID, int dragType, ClickType clickType, EntityPlayer player) {
		if (slotID >= 0) {
			ItemStack stack = getSlot(slotID).getStack();
			if (stack.getItem() instanceof GemSocket) {
				return ItemStack.EMPTY;
			}
		}

		return super.slotClick(slotID, dragType, clickType, player);
	}
}

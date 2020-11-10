/*package com.aranaira.arcanearchives.inventory;

import com.aranaira.arcanearchives.inventory.handlers.OptionalUpgradesHandler;
import com.aranaira.arcanearchives.inventory.handlers.SizeUpgradeItemHandler;
import com.aranaira.arcanearchives.items.IUpgradeItem;
import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import com.aranaira.arcanearchives.tileentities.interfaces.IUpgradeableStorage;
import com.aranaira.arcanearchives.types.UpgradeType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerUpgrades extends Container {
	private IUpgradeableStorage storage;
	private ImmanenceTileEntity tile;
	private PlayerEntity player;
	private SizeUpgradeItemHandler sizeHandler;
	private OptionalUpgradesHandler optionalHandler;

	public ContainerUpgrades (PlayerEntity player, ImmanenceTileEntity tile) {
		assert tile instanceof IUpgradeableStorage;
		this.storage = (IUpgradeableStorage) tile;
		this.tile = tile;
		this.player = player;
		this.sizeHandler = storage.getSizeUpgradesHandler();
		this.optionalHandler = storage.getOptionalUpgradesHandler();

		createPlayerInventory(player.inventory);
		createUpgradeInventory();
	}

	private void createUpgradeInventory () {
		int xOffset = 57;
		int yOffset = 8;
		int xSpacing = 25;
		int ySpacing = 36;

		// First row of upgrade slots for size
		for (int i = 0; i < 3; i++) {
			addSlotToContainer(new SlotItemHandler(sizeHandler, i, xOffset + i * xSpacing, yOffset));
		}

		// Second row of upgrade slots for option upgrades
		for (int i = 0; i < 3; i++) {
			addSlotToContainer(new SlotItemHandler(optionalHandler, i, xOffset + i * xSpacing, yOffset + ySpacing));
		}
	}

	private void createPlayerInventory (PlayerInventory inventoryPlayer) {
		int xOffset = 10;
		int yOffset = 80;

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new net.minecraft.inventory.container.Slot(inventoryPlayer, j + i * 9 + 9, xOffset + j * 18, yOffset + i * 18));
			}
		}
		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new net.minecraft.inventory.container.Slot(inventoryPlayer, i, xOffset + i * 18, yOffset + 58));
		}
	}

	@Override
	public boolean canInteractWith (PlayerEntity playerIn) {
		return true;
	}

	@Override
	public ItemStack transferStackInSlot (PlayerEntity playerIn, int index) {
		if (index < 36) {
			net.minecraft.inventory.container.Slot slot = getSlot(index);
			if (slot != null && slot.getHasStack()) {
				ItemStack stack = slot.getStack();
				if (!mergeItemStack(stack, 0, 36, true)) return ItemStack.EMPTY;
			}
		}

		return ItemStack.EMPTY;
		Slot slot = getSlot(index);
		if (slot != null && slot.getHasStack()) {
			ItemStack stack = slot.getStack();
			if (index < 36 && stack.getItem() instanceof IUpgradeItem) {
				IUpgradeItem upgrade = (IUpgradeItem) stack.getItem();
				// From player inventory to the
				int upgradeSlot = 36;
				if (upgrade.getUpgradeType(stack) == UpgradeType.SIZE) {
					int upSlot = upgrade.getSlotIsUpgradeFor(stack);
					upgradeSlot += upSlot;
					net.minecraft.inventory.container.Slot target = getSlot(upgradeSlot);
					switch (upSlot) {
						case 0:
							if (!target.getHasStack() && !mergeItemStack(stack, upgradeSlot, upgradeSlot + 1, false)) {
								return ItemStack.EMPTY;
							}
							break;
						case 1:
							if (sizeHandler.hasUpgrade(0) && !target.getHasStack() && !mergeItemStack(stack, upgradeSlot, upgradeSlot + 1, false)) {
								return ItemStack.EMPTY;
							}
							break;
						case 2:
							if (sizeHandler.hasUpgrade(0) && sizeHandler.hasUpgrade(1) && !target.getHasStack() && !mergeItemStack(stack, upgradeSlot, upgradeSlot + 1, false)) {
								return ItemStack.EMPTY;
							}
							break;
					}
				} else {
					upgradeSlot += 3;
					UpgradeType type = upgrade.getUpgradeType(stack);
					if (!optionalHandler.hasUpgrade(type)) {
						Slot target = getSlot(upgradeSlot);
						while (target != null && target.getHasStack()) {
							upgradeSlot++;
							target = getSlot(upgradeSlot);
						}
						if (target != null && mergeItemStack(stack, upgradeSlot, upgradeSlot + 1, false)) {
							return ItemStack.EMPTY;
						}
					}
				}
				return ItemStack.EMPTY;
			} else {
				if (index >= 36 && index < 39) {
					int upSlot = Math.max(index - 36, 0);
					if (!sizeHandler.extractItem(upSlot, 1, true).isEmpty()) {
						mergeItemStack(stack, 0, 36, false);
						sizeHandler.extractItem(upSlot, 1, false);
					}
				} else if (index >= 39 && index < 42) {
					if (!mergeItemStack(stack, 0, 36, false)) {
						return ItemStack.EMPTY;
					}
				}
			}
		}

		return ItemStack.EMPTY;
	}
}*/

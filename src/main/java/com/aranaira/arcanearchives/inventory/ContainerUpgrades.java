package com.aranaira.arcanearchives.inventory;

import com.aranaira.arcanearchives.inventory.handlers.OptionalUpgradesHandler;
import com.aranaira.arcanearchives.inventory.handlers.SizeUpgradeItemHandler;
import com.aranaira.arcanearchives.tileentities.IUpgradeableStorage;
import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerUpgrades extends Container {
	private IUpgradeableStorage storage;
	private ImmanenceTileEntity tile;
	private EntityPlayer player;
	private SizeUpgradeItemHandler sizeHandler;
	private OptionalUpgradesHandler optionalHandler;

	public ContainerUpgrades (EntityPlayer player, ImmanenceTileEntity tile) {
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
		int xOffset = 10;
		int yOffset = 57;

		// First row of upgrade slots for size
		for (int i = 0; i < 3; i++) {
			addSlotToContainer(new SlotItemHandler(sizeHandler, i, xOffset + i * 18, 20));
		}

		// Second row of upgrade slots for option upgrades
		for (int i = 0; i < 3; i++) {
			addSlotToContainer(new SlotItemHandler(optionalHandler, i, xOffset + i * 18, 40));
		}
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
	public boolean canInteractWith (EntityPlayer playerIn) {
		return true;
	}
}

package com.aranaira.arcanearchives.inventory;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.inventory.handlers.DevouringCharmHandler;
import com.aranaira.arcanearchives.inventory.handlers.GemSocketHandler;
import com.aranaira.arcanearchives.items.BaubleGemSocket;
import com.aranaira.arcanearchives.items.gems.ArcaneGemItem;
import com.aranaira.arcanearchives.items.gems.GemUtil;
import com.aranaira.arcanearchives.items.gems.pendeloque.ParchtearItem;
import com.aranaira.arcanearchives.items.itemblocks.RadiantTankItem;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class ContainerDevouringCharm extends Container {

	private DevouringCharmHandler handler;
	private EntityPlayer player;
	private ItemStack socket;

	public ContainerDevouringCharm(EntityPlayer player) {
		//ArcaneArchives.logger.info("CONTAINER CLASS INSTANTIATED");
		this.player = player;
		socket = player.getHeldItemMainhand();

		handler = DevouringCharmHandler.getHandler(socket);

		createPlayerInventory(player.inventory);
		createBucketSlot();
		createVoidSlots();
	}

	@Override
	public boolean canInteractWith (EntityPlayer playerIn) {
		return true;
	}

	private void createBucketSlot () {
		int xOffset = 82;
		int yOffset = 71;
		addSlotToContainer(new SlotItemHandler(handler.getInventory(), 0, xOffset, yOffset));
	}

	private void createVoidSlots () {
		int xOffset = 64;
		int yOffset = 113;
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 3; j++) {
				addSlotToContainer(new SlotItemHandler(handler.getInventory(), j + i * 3 + 1, xOffset + j * 18, yOffset + i * 18));
			}
		}
	}

	private void createPlayerInventory (InventoryPlayer inventoryPlayer) {
		int xOffset = 10;
		int yOffset = 165;

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

			if (index < 36) { //Player Inventory -> Socket
				if (!mergeItemStack(stack, 36, 43, false)) {
					return ItemStack.EMPTY;
				}
			} else {
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
	@Nonnull
	public ItemStack slotClick (int slotID, int dragType, ClickType clickType, EntityPlayer player) {
		ArcaneArchives.logger.info(slotID);
		return super.slotClick(slotID, dragType, clickType, player);
	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
		if(!player.world.isRemote) {
			ItemStack stack = getSlot(36).getStack();
			if (stack != null) {
				EntityItem item = new EntityItem(player.world, player.posX, player.posY, player.posZ, stack);
				item.setPickupDelay(0);
				player.world.spawnEntity(item);
			}
		}
	}
}

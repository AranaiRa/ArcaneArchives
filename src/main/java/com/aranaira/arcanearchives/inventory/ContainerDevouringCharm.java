package com.aranaira.arcanearchives.inventory;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.init.ItemRegistry;
import com.aranaira.arcanearchives.inventory.handlers.DevouringCharmHandler;
import com.aranaira.arcanearchives.inventory.slots.SlotImmutable;
import com.aranaira.arcanearchives.items.gems.GemUtil;
import com.aranaira.arcanearchives.util.ItemUtilities;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class ContainerDevouringCharm extends Container {

	private DevouringCharmHandler handler;
	private EntityPlayer player;
	private ItemStack socket;

	public ContainerDevouringCharm (EntityPlayer player) {
		this.player = player;
		socket = player.getHeldItemMainhand();

		handler = DevouringCharmHandler.getHandler(socket);

		createPlayerInventory(player.inventory);
		createBucketSlot();
		createVoidSlots();
		createDevourSlots();
	}

	@Override
	public boolean canInteractWith (EntityPlayer playerIn) {
		return true;
	}

	private void createBucketSlot () {
		int xOffset = 82;
		int yOffset = 71;
		addSlotToContainer(new SlotItemHandler(handler.getInventory(), 0, xOffset, yOffset) {
			private ItemStack consumeFluid (ItemStack stack) {
				if (!stack.isEmpty()) {
					if (stack.getItem() == ItemRegistry.PARCHTEAR) {
						GemUtil.manuallyRestoreCharge(stack, -1);
					} else {
						IFluidHandlerItem cap = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
						if (cap != null) {
							boolean didDrain = false;
							for (IFluidTankProperties props : cap.getTankProperties()) {
								FluidStack drain = props.getContents();
								FluidStack result = cap.drain(drain, true);
								if (result != null && result.amount > 0) {
									didDrain = true;
								}
							}
							if (didDrain && (stack.getItem() instanceof ItemBucket || stack.getItem() instanceof UniversalBucket)) {
								stack = cap.getContainer();
							}
						}
					}
				}
				return stack;
			}

			@Override
			public boolean isItemValid (@Nonnull ItemStack stack) {
				return (stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null) || stack.getItem() == ItemRegistry.PARCHTEAR) && stack.getItem() != ItemRegistry.RADIANT_AMPHORA;
			}

			@Override
			public void putStack (@Nonnull ItemStack stack) {
				super.putStack(consumeFluid(stack));
			}
		});
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

	private void createDevourSlots () {
		addSlotToContainer(new SlotItemHandler(handler.getInventory(), 0, 0, 105));
		addSlotToContainer(new SlotItemHandler(handler.getInventory(), 1, 12, 76));
		addSlotToContainer(new SlotItemHandler(handler.getInventory(), 2, 30, 49));
		addSlotToContainer(new SlotItemHandler(handler.getInventory(), 3, 134, 49));
		addSlotToContainer(new SlotItemHandler(handler.getInventory(), 4, 152, 76));
		addSlotToContainer(new SlotItemHandler(handler.getInventory(), 5, 164, 105));
	}

	private void createPlayerInventory (InventoryPlayer inventoryPlayer) {
		int xOffset = 10;
		int yOffset = 165;

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				int index = j + i * 9 + 9;
				if (inventoryPlayer.getStackInSlot(index).getItem() == ItemRegistry.DEVOURING_CHARM) {
					addSlotToContainer(new SlotImmutable(inventoryPlayer, index, xOffset + j * 18, yOffset + i * 18));
				} else {
					addSlotToContainer(new Slot(inventoryPlayer, index, xOffset + j * 18, yOffset + i * 18));
				}
			}
		}
		for (int i = 0; i < 9; i++) {
			if (inventoryPlayer.getStackInSlot(i).getItem() == ItemRegistry.DEVOURING_CHARM) {
				addSlotToContainer(new SlotImmutable(inventoryPlayer, i, xOffset + i * 18, yOffset + 58));
			} else {
				addSlotToContainer(new Slot(inventoryPlayer, i, xOffset + i * 18, yOffset + 58));
			}
		}
	}

	@Override
	@Nonnull
	public ItemStack transferStackInSlot (EntityPlayer player, int index) {
		ItemStack slotStack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack stack = slot.getStack();
			if (stack.getItem() == ItemRegistry.DEVOURING_CHARM) {
				return ItemStack.EMPTY;
			}
			slotStack = stack.copy();
			if (index < 36) { //Player Inventory -> Socket
				if (stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
					Slot fluid = getSlot(36);
					IItemHandler playerInventory = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
					if (ItemHandlerHelper.insertItemStacked(playerInventory, fluid.getStack(), false).isEmpty()) {
						fluid.putStack(ItemStack.EMPTY);
					}

					if (!mergeItemStack(stack, 36, 37, false)) {
						return ItemStack.EMPTY;
					}
				}

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
	public void onContainerClosed (EntityPlayer player) {
		Slot fluid = getSlot(36);
		if (fluid.getHasStack()) {
			ItemStack slotItem = fluid.getStack();
			IItemHandler playerInventory = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
			ItemStack result = ItemHandlerHelper.insertItemStacked(playerInventory, slotItem, false);
			if (!result.isEmpty() && !player.world.isRemote) {
				Block.spawnAsEntity(player.world, player.getPosition(), result);
			}
		}
	}
}

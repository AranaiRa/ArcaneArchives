package com.aranaira.arcanearchives.inventory;

import com.aranaira.arcanearchives.init.ItemRegistry;
import com.aranaira.arcanearchives.inventory.handlers.DevouringCharmHandler;
import com.aranaira.arcanearchives.inventory.slots.SlotImmutable;
import com.aranaira.arcanearchives.items.gems.GemUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
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

	public boolean FLIPPED = false;

	private DevouringCharmHandler handler;
	private PlayerEntity player;
	private ItemStack socket;

	public ContainerDevouringCharm (PlayerEntity player) {
		this.player = player;
		socket = player.getHeldItemMainhand();

		handler = DevouringCharmHandler.getHandler(socket);

		createPlayerInventory(player.inventory);
		createBucketSlot();
		createVoidSlots();
		createAutoVoidSlots();
	}

	@Override
	public boolean canInteractWith (PlayerEntity playerIn) {
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
							if (didDrain && (stack.getItem() instanceof BucketItem || stack.getItem() instanceof UniversalBucket)) {
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

			@Override
			public boolean isEnabled () {
				return !FLIPPED;
			}
		});
	}

	private void createVoidSlots () {
		int xOffset = 64;
		int yOffset = 113;
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 3; j++) {
				addSlotToContainer(new SlotItemHandler(handler.getInventory(), j + i * 3 + 1, xOffset + j * 18, yOffset + i * 18) {
					@Override
					public boolean isEnabled () {
						return !FLIPPED;
					}
				});
			}
		}
	}

	private void createAutoVoidSlots () {
		int xOffset = 58;
		int yOffset = 99;
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 3; j++) {
				addSlotToContainer(new SlotAutovoidHandler(handler.getAutovoidInventory(), j + i * 3, xOffset + j * 24, yOffset + i * 24));
			}
		}
	}

	private void createPlayerInventory (PlayerInventory inventoryPlayer) {
		int xOffset = 10;
		int yOffset = 165;

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				int index = j + i * 9 + 9;
				if (inventoryPlayer.getStackInSlot(index).getItem() == ItemRegistry.DEVOURING_CHARM) {
					addSlotToContainer(new SlotImmutable(inventoryPlayer, index, xOffset + j * 18, yOffset + i * 18));
				} else {
					addSlotToContainer(new net.minecraft.inventory.container.Slot(inventoryPlayer, index, xOffset + j * 18, yOffset + i * 18));
				}
			}
		}
		for (int i = 0; i < 9; i++) {
			if (inventoryPlayer.getStackInSlot(i).getItem() == ItemRegistry.DEVOURING_CHARM) {
				addSlotToContainer(new SlotImmutable(inventoryPlayer, i, xOffset + i * 18, yOffset + 58));
			} else {
				addSlotToContainer(new net.minecraft.inventory.container.Slot(inventoryPlayer, i, xOffset + i * 18, yOffset + 58));
			}
		}
	}

	@Override
	@Nonnull
	public ItemStack transferStackInSlot (PlayerEntity player, int index) {
		ItemStack slotStack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);

		if (FLIPPED) {
			return ItemStack.EMPTY;
		} else {
			if (slot != null && slot.getHasStack()) {
				ItemStack stack = slot.getStack();
				if (stack.getItem() == ItemRegistry.DEVOURING_CHARM) {
					return ItemStack.EMPTY;
				}
				slotStack = stack.copy();
				if (index < 36) { //Player Inventory -> Socket
					if (stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
						net.minecraft.inventory.container.Slot fluid = getSlot(36);
						IItemHandler playerInventory = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.UP);
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
					if (!mergeItemStack(stack, 27, 36, false)) {
						if (!mergeItemStack(stack, 0, 27, false)) {
							return ItemStack.EMPTY;
						}
					}
				}

				if (stack.isEmpty()) {
					slot.putStack(ItemStack.EMPTY);
				}

				slot.onSlotChanged();
			}
		}

		return slotStack;
	}

	@Override
	public void onContainerClosed (PlayerEntity player) {
		net.minecraft.inventory.container.Slot fluid = getSlot(36);
		if (fluid.getHasStack()) {
			ItemStack slotItem = fluid.getStack();
			IItemHandler playerInventory = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.UP);
			ItemStack result = ItemHandlerHelper.insertItemStacked(playerInventory, slotItem, false);
			if (!result.isEmpty() && !player.world.isRemote) {
				Block.spawnAsEntity(player.world, player.getPosition(), result);
			}
		}
	}

	@Override
	public ItemStack slotClick (int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {
		net.minecraft.inventory.container.Slot slot = slotId < 0 ? null : this.inventorySlots.get(slotId);
		if (slot instanceof SlotAutovoidHandler) {
			if (dragType == 2) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.putStack(player.inventory.getItemStack());
			}
			return player.inventory.getItemStack();
		}
		return super.slotClick(slotId, dragType, clickTypeIn, player);
	}

	public class SlotAutovoidHandler extends SlotItemHandler {
		public SlotAutovoidHandler (IItemHandler itemHandler, int index, int xPosition, int yPosition) {
			super(itemHandler, index, xPosition, yPosition);
		}

		@Override
		public void putStack (@Nonnull ItemStack stack) {
			getItemHandler().insertItem(getSlotIndex(), stack, false);
		}

		@Override
		public boolean canTakeStack (PlayerEntity playerIn) {
			return true;
		}

		@Override
		public boolean isItemValid (@Nonnull ItemStack stack) {
			return true;
		}

		@Override
		public boolean isEnabled () {
			return FLIPPED;
		}

		@Override
		public int getItemStackLimit (@Nonnull ItemStack stack) {
			return 1;
		}
	}
}

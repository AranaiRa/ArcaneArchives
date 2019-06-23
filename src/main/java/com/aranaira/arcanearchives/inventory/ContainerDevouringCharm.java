package com.aranaira.arcanearchives.inventory;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.init.ItemRegistry;
import com.aranaira.arcanearchives.inventory.handlers.DevouringCharmHandler;
import com.aranaira.arcanearchives.items.gems.GemUtil;
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
	}

	@Override
	public boolean canInteractWith (EntityPlayer playerIn) {
		return true;
	}

	private void createBucketSlot () {
		int xOffset = 82;
		int yOffset = 71;
		addSlotToContainer(new SlotItemHandler(handler.getInventory(), 0, xOffset, yOffset) {
			private void consumeFluid () {
				ItemStack stack = getStack();
				if (!stack.isEmpty()) {
					if (stack.getItem() == ItemRegistry.PARCHTEAR) {
						GemUtil.manuallyRestoreCharge(stack, -1);
					} else {
						IFluidHandlerItem cap = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
						if (cap != null) {
							for (IFluidTankProperties props : cap.getTankProperties()) {
								FluidStack drain = props.getContents();
								cap.drain(drain, true);
							}
							if ((stack.getItem() instanceof ItemBucket || stack.getItem() instanceof UniversalBucket)) {
								stack = cap.getContainer();
							}
						}
					}
					if (!player.world.isRemote) {
						IItemHandler inventory = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
						ItemStack result = ItemHandlerHelper.insertItemStacked(inventory, stack, false);
						if (!result.isEmpty()) {
							Block.spawnAsEntity(player.world, player.getPosition(), result);
						}
					}
					putStack(ItemStack.EMPTY);
				}
			}

			@Override
			public boolean isItemValid (@Nonnull ItemStack stack) {
				return (stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null) || stack.getItem() == ItemRegistry.PARCHTEAR) && stack.getItem() != ItemRegistry.RADIANT_AMPHORA;
			}

			@Override
			public void putStack (@Nonnull ItemStack stack) {
				super.putStack(stack);
				consumeFluid();
			}

			@Override
			public void onSlotChange (@Nonnull ItemStack p_75220_1_, @Nonnull ItemStack p_75220_2_) {
				super.onSlotChange(p_75220_1_, p_75220_2_);
				consumeFluid();
			}

			@Nonnull
			@Override
			public ItemStack decrStackSize (int amount) {
				ItemStack result = super.decrStackSize(amount);
				consumeFluid();
				return result;
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
				if (stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
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
	}
}

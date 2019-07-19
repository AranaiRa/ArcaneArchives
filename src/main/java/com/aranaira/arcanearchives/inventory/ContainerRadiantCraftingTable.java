package com.aranaira.arcanearchives.inventory;

import com.aranaira.arcanearchives.inventory.handlers.InventoryCraftingPersistent;
import com.aranaira.arcanearchives.inventory.slots.SlotCraftingFastWorkbench;
import com.aranaira.arcanearchives.inventory.slots.SlotIRecipe;
import com.aranaira.arcanearchives.network.Networking;
import com.aranaira.arcanearchives.network.PacketRadiantCrafting;
import com.aranaira.arcanearchives.tileentities.RadiantCraftingTableTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

// nearly the same as ContainerWorkbench but uses the TileEntities inventory
@Mod.EventBusSubscriber
public class ContainerRadiantCraftingTable extends Container {
	public final BlockPos pos;
	protected final World world;
	private final ItemStackHandler itemHandler;
	private final EntityPlayer player;
	private final InventoryCraftingPersistent craftMatrix;
	private final InventoryCraftResult craftResult;
	private IRecipe lastRecipe;
	private IRecipe lastLastRecipe;
	private IRecipe actualLastLastRecipe;
	private IRecipe actualLastRecipe;
	private List<SlotIRecipe> recipeSlots = Arrays.asList(new SlotIRecipe[3]);

	private RadiantCraftingTableTileEntity tile;

	public ContainerRadiantCraftingTable (RadiantCraftingTableTileEntity tile, EntityPlayer player, InventoryPlayer playerInventory) {
		super();

		this.tile = tile;

		this.world = tile.getWorld();
		this.pos = tile.getPos();
		this.itemHandler = tile.getInventory();

		craftResult = new InventoryCraftResult();
		craftMatrix = new InventoryCraftingPersistent(this, itemHandler, tile, 3, 3);
		this.player = player;

		this.addSlotToContainer(new SlotCraftingFastWorkbench(this, playerInventory.player, this.craftMatrix, this.craftResult, 0, 136, 42));

		int i;
		int j;

		for (i = 0; i < 3; ++i) {
			for (j = 0; j < 3; ++j) {
				this.addSlotToContainer(new Slot(this.craftMatrix, j + i * 3, 24 + j * 18, 24 + i * 18));
			}
		}

		int index = 9;

		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 9; col++) {
				this.addSlotToContainer(new Slot(playerInventory, index, 23 + col * 18, 115 + row * 18));
				index++;
			}
		}

		index = 0;
		for (int col = 0; col < 9; col++) {
			this.addSlotToContainer(new Slot(playerInventory, index, 23 + col * 18, 173));
			index++;
		}

		/*this.recipeSlots.set(0, new SlotIRecipe(this, index++, tile, player, 0, 174, 16));
		this.recipeSlots.set(1, new SlotIRecipe(this, index++, tile, player, 1, 174, 42));
		this.recipeSlots.set(2, new SlotIRecipe(this, index, tile, player, 2, 174, 68));

		this.addSlotToContainer(recipeSlots.get(0));
		this.addSlotToContainer(recipeSlots.get(1));
		this.addSlotToContainer(recipeSlots.get(2));*/
	}

	@SubscribeEvent
	public static void onCraftingStationGuiOpened (PlayerContainerEvent.Open event) {
		// by default the container does not update after it has been opened.
		// we need it to check its recipe
		if (event.getContainer() instanceof ContainerRadiantCraftingTable) {
			((ContainerRadiantCraftingTable) event.getContainer()).onCraftMatrixChanged();
		}
	}

	public void onCraftMatrixChanged () {
		this.onCraftMatrixChanged(this.craftMatrix);
	}

	public void saveLastRecipe () {
		actualLastLastRecipe = actualLastRecipe;
		actualLastRecipe = lastRecipe;
	}

	@Nonnull
	@Override
	public ItemStack transferStackInSlot (EntityPlayer playerIn, int index) {
		Slot slot = this.inventorySlots.get(index);

		if (slot == null || !slot.getHasStack()) {
			return ItemStack.EMPTY;
		}

		ItemStack original = slot.getStack().copy();
		ItemStack itemstack = slot.getStack().copy();

		// slot that was clicked on not empty?
		int end = this.inventorySlots.size();

		// Is it a slot in the main inventory? (aka not player inventory)
		if (index < 10) {
			// try to put it into the player inventory (if we have a player inventory)
			if (!this.mergeItemStack(itemstack, 10, end, true)) {
				return ItemStack.EMPTY;
			}
		}
		// Slot is in the player inventory (if it exists), transfer to main inventory
		else if (!this.mergeItemStack(itemstack, 0, 10, false)) {
			return ItemStack.EMPTY;
		}

		slot.onSlotChanged();

		if (itemstack.getCount() == original.getCount()) {
			return ItemStack.EMPTY;
		}

		// update slot we pulled from
		slot.putStack(itemstack);
		slot.onTake(player, itemstack);

		if (slot.getHasStack() && slot.getStack().isEmpty()) {
			slot.putStack(ItemStack.EMPTY);
		}

		return original;
	}

	// only refills items that are already present
	protected boolean mergeItemStackRefill (@Nonnull ItemStack stack, int startIndex, int endIndex, boolean useEndIndex) {
		if (stack.getCount() <= 0) {
			return false;
		}

		boolean flag1 = false;
		int k = startIndex;

		if (useEndIndex) {
			k = endIndex - 1;
		}

		Slot slot;
		ItemStack itemstack1;

		if (stack.isStackable()) {
			while (stack.getCount() > 0 && (!useEndIndex && k < endIndex || useEndIndex && k >= startIndex)) {
				slot = this.inventorySlots.get(k);
				itemstack1 = slot.getStack();

				if (!itemstack1.isEmpty() && itemstack1.getItem() == stack.getItem() && (!stack.getHasSubtypes() || stack.getMetadata() == itemstack1.getMetadata()) && ItemStack.areItemStackTagsEqual(stack, itemstack1) && this.canMergeSlot(stack, slot)) {
					int l = itemstack1.getCount() + stack.getCount();
					int limit = Math.min(stack.getMaxStackSize(), slot.getItemStackLimit(stack));

					if (l <= limit) {
						stack.setCount(0);
						itemstack1.setCount(l);
						slot.onSlotChanged();
						flag1 = true;
					} else if (itemstack1.getCount() < limit) {
						stack.shrink(limit - itemstack1.getCount());
						itemstack1.setCount(limit);
						slot.onSlotChanged();
						flag1 = true;
					}
				}

				if (useEndIndex) {
					--k;
				} else {
					++k;
				}
			}
		}

		return flag1;
	}

	// only moves items into empty slots
	protected boolean mergeItemStackMove (@Nonnull ItemStack stack, int startIndex, int endIndex, boolean useEndIndex) {
		if (stack.getCount() <= 0) {
			return false;
		}

		boolean flag1 = false;
		int k;

		if (useEndIndex) {
			k = endIndex - 1;
		} else {
			k = startIndex;
		}

		while (!useEndIndex && k < endIndex || useEndIndex && k >= startIndex) {
			Slot slot = this.inventorySlots.get(k);
			ItemStack itemstack1 = slot.getStack();

			if (itemstack1.isEmpty() && slot.isItemValid(stack) && this.canMergeSlot(stack, slot)) // Forge: Make sure to respect isItemValid in the slot.
			{
				int limit = slot.getItemStackLimit(stack);
				ItemStack stack2 = stack.copy();
				if (stack2.getCount() > limit) {
					stack2.setCount(limit);
					stack.shrink(limit);
				} else {
					stack.setCount(0);
				}
				slot.putStack(stack2);
				slot.onSlotChanged();
				flag1 = true;

				if (stack.isEmpty()) {
					break;
				}
			}

			if (useEndIndex) {
				--k;
			} else {
				++k;
			}
		}


		return flag1;
	}

	@Override
	public boolean canMergeSlot (ItemStack p_94530_1_, Slot p_94530_2_) {
		return p_94530_2_.inventory != this.craftResult && super.canMergeSlot(p_94530_1_, p_94530_2_);
	}

	@Override
	public void onCraftMatrixChanged (IInventory inventoryIn) {
		this.slotChangedCraftingGrid(this.world, this.player, this.craftMatrix, this.craftResult);
	}

	// update crafting
	@Override
	public void setAll (List<ItemStack> p_190896_1_) {
		craftMatrix.setDoNotCallUpdates(true);
		super.setAll(p_190896_1_);
		craftMatrix.setDoNotCallUpdates(false);
		craftMatrix.onCraftMatrixChanged();
	}

	@Override
	public boolean canInteractWith (EntityPlayer playerIn) {
		return true;
	}

	// Fix for a vanilla bug: doesn't take Slot.getMaxStackSize into account
	@Override
	protected boolean mergeItemStack (@Nonnull ItemStack stack, int startIndex, int endIndex, boolean useEndIndex) {
		boolean ret = mergeItemStackRefill(stack, startIndex, endIndex, useEndIndex);
		if (!stack.isEmpty() && stack.getCount() > 0) {
			ret |= mergeItemStackMove(stack, startIndex, endIndex, useEndIndex);
		}
		return ret;
	}

	@Override
	protected void slotChangedCraftingGrid (World world, EntityPlayer player, InventoryCrafting inv, InventoryCraftResult result) {
		ItemStack itemstack = ItemStack.EMPTY;

		if (lastRecipe == null || !lastRecipe.matches(inv, world)) {
			lastRecipe = CraftingManager.findMatchingRecipe(inv, world);
		}

		if (lastRecipe != null) {
			itemstack = lastRecipe.getCraftingResult(inv);
		}

		if (!world.isRemote) {
			result.setInventorySlotContents(0, itemstack);
			EntityPlayerMP entityplayermp = (EntityPlayerMP) player;
			if (lastLastRecipe != lastRecipe) {
				entityplayermp.connection.sendPacket(new SPacketSetSlot(this.windowId, 0, itemstack));
			} else if (lastLastRecipe != null && !ItemStack.areItemStacksEqual(lastLastRecipe.getCraftingResult(inv), lastRecipe.getCraftingResult(inv))) {
				entityplayermp.connection.sendPacket(new SPacketSetSlot(this.windowId, 0, itemstack));
			}
			Networking.CHANNEL.sendTo(new PacketRadiantCrafting.LastRecipe(lastRecipe), entityplayermp);
		}

		lastLastRecipe = lastRecipe;
	}

	/**
	 * @return the starting slot for the player inventory. Present for usage in the JEI crafting station support
	 */
	public InventoryCrafting getCraftMatrix () {
		return craftMatrix;
	}

	public void updateLastRecipeFromServer (IRecipe recipe) {
		if (lastRecipe == recipe) {
			return;
		}

		lastRecipe = recipe;
		if (recipe != null && recipe.matches(craftMatrix, world)) {
			ItemStack stack = recipe.getCraftingResult(craftMatrix);
			if (!stack.isEmpty()) {
				this.craftResult.setInventorySlotContents(0, stack);
			}
		}
	}

	public NonNullList<ItemStack> getRemainingItems () {
		if (lastRecipe != null && lastRecipe.matches(craftMatrix, world)) {
			return lastRecipe.getRemainingItems(craftMatrix);
		}
		return craftMatrix.stackList;
	}
}


package com.aranaira.arcanearchives.inventory;

import com.aranaira.arcanearchives.inventory.slots.SlotRecipeHandler;
import com.aranaira.arcanearchives.network.NetworkHandler;
import com.aranaira.arcanearchives.network.PacketGemCutters;
import com.aranaira.arcanearchives.recipe.gct.GCTRecipe;
import com.aranaira.arcanearchives.recipe.gct.GCTRecipeList;
import com.aranaira.arcanearchives.tileentities.GemCuttersTableTileEntity;
import invtweaks.api.container.ContainerSection;
import invtweaks.api.container.ContainerSectionCallback;
import invtweaks.api.container.InventoryContainer;
import it.unimi.dsi.fastutil.ints.Int2IntMap.Entry;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional.Method;
import net.minecraftforge.items.*;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import javax.annotation.Nonnull;
import java.util.*;

@InventoryContainer
public class ContainerGemCuttersTable extends Container {
	private static final int SLOT_OUTPUT = 0;
	private final SlotItemHandler slotOutput;
	private final IInventory playerInventory;
	private final IItemHandlerModifiable tileInventory;
	private final IItemHandlerModifiable outputInv = new ItemStackHandler(1);
	private final IItemHandler combinedInventory;
	private final GemCuttersTableTileEntity tile;
	private final EntityPlayer player;
	private final World world;
	private Runnable updateRecipeGUI;
	private List<Slot> playerSlots = new ArrayList<>();
	private List<Slot> tileSlots = new ArrayList<>();
	private List<Slot> playerHotbar = new ArrayList<>();

	public ContainerGemCuttersTable (IItemHandlerModifiable tileInventory, GemCuttersTableTileEntity tile, EntityPlayer player) {
		this.tileInventory = tileInventory;
		this.tile = tile;
		this.playerInventory = player.inventory;
		this.player = player;
		this.world = player.world;

		IItemHandler mainPlayerInv = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
		if (!(mainPlayerInv instanceof IItemHandlerModifiable)) {
			throw new IllegalStateException("Expected main player inventory to be modifiable");
		}
		this.combinedInventory = new CombinedInvWrapper(tileInventory, (IItemHandlerModifiable) mainPlayerInv);

		//Output Slot
		this.slotOutput = new SlotItemHandler(outputInv, SLOT_OUTPUT, 95, 18) {
			@Override
			public ItemStack onTake (EntityPlayer thePlayer, ItemStack stack) {
				GCTRecipe recipe = tile.getCurrentRecipe();
				for (Entry entry : recipe.getMatchingSlots(combinedInventory).int2IntEntrySet()) {
					ItemStack result = combinedInventory.extractItem(entry.getIntKey(), entry.getIntValue(), false);
					if (!player.world.isRemote) {
						recipe.handleItemResult(world, player, tile, result);
					}
				}
				if (!player.world.isRemote) {
					stack = recipe.onCrafted(player, stack);
				}
				return super.onTake(player, stack);
			}

			@Override
			public boolean isItemValid (@Nonnull ItemStack stack) {
				return false;
			}

			@Override
			public boolean canTakeStack (EntityPlayer player) {
				return tile.getCurrentRecipe().matches(combinedInventory) && tile.getCurrentRecipe().craftable(player, tile);
			}
		};

		this.addSlotToContainer(slotOutput);

		//Player Inventory
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				Slot thisSlot = new Slot(playerInventory, j + i * 9 + 9, 23 + j * 18, 166 + i * 18) {
					@Override
					public void onSlotChanged () {
						super.onSlotChanged();
						updateRecipe();
					}
				};
				this.addSlotToContainer(thisSlot);
				playerSlots.add(thisSlot);
			}
		}

		//Player Hotbar
		for (int k = 0; k < 9; ++k) {
			Slot thisSlot = new Slot(playerInventory, k, 23 + k * 18, 224) {
				@Override
				public void onSlotChanged () {
					super.onSlotChanged();
					updateRecipe();
				}
			};
			this.addSlotToContainer(thisSlot);
			playerHotbar.add(thisSlot);
		}

		//GCT Inventory
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 9; j++) {
				Slot thisSlot = new SlotItemHandler(tileInventory, i * 9 + j, 23 + j * 18, 105 + i * 18) {
					@Override
					public void onSlotChanged () {
						super.onSlotChanged();
						updateRecipe();
					}
				};
				this.addSlotToContainer(thisSlot);
				inventorySlots.add(thisSlot);
			}
		}

		//Recipe Selection Slots
		for (int x = 6; x > -1; x--) {
			this.addSlotToContainer(new SlotRecipeHandler(x, x * 18 + 41, 70, tile));
		}

		updateRecipe();
	}

	private void updateRecipe () {
		if (updateRecipeGUI != null) {
			updateRecipeGUI.run();
		}

		ItemStack itemstack;

		GCTRecipe curRecipe = tile.getCurrentRecipe();
		if (curRecipe != null) {
			itemstack = curRecipe.getRecipeOutput().copy();
			if (curRecipe.matches(combinedInventory)) {
				outputInv.setStackInSlot(SLOT_OUTPUT, itemstack);
			} else {
				outputInv.setStackInSlot(SLOT_OUTPUT, ItemStack.EMPTY);
				return;
			}
		} else {
			outputInv.setStackInSlot(SLOT_OUTPUT, ItemStack.EMPTY);
			return;
		}

		if (tile.getPenultimateRecipe() != tile.getLastRecipe()) {
			playerInventory.setInventorySlotContents(0, itemstack);
		} else if (tile.getPenultimateRecipe() != null && tile.getPenultimateRecipe() == tile.getLastRecipe() && !ItemStack.areItemStacksEqual(tile.getPenultimateRecipe().getRecipeOutput(), tile.getLastRecipe().getRecipeOutput())) {
			playerInventory.setInventorySlotContents(0, itemstack);
		}
		if (tile.getLastRecipe() != null && !world.isRemote) {
			NetworkHandler.CHANNEL.sendTo(new PacketGemCutters.LastRecipe(tile.getLastRecipe()), (EntityPlayerMP) player);
		}

		tile.updatePenultimateRecipe();
	}

	public void setUpdateRecipeGUI (Runnable updateRecipeGUI) {
		this.updateRecipeGUI = updateRecipeGUI;
	}

	@Override
	@Nonnull
	public ItemStack transferStackInSlot (EntityPlayer playerIn, int index) {
		Slot slot = this.inventorySlots.get(index);

		if (slot == null || !slot.getHasStack()) {
			return ItemStack.EMPTY;
		}

		ItemStack original = slot.getStack().copy();
		ItemStack itemstack = slot.getStack().copy();

		// Player inventory
		if (index >= 1 && index <= 36) {
			if (!this.mergeItemStack(itemstack, 37, 55, false)) {
				return ItemStack.EMPTY;
			}
		}
		// Output slot to player inventory/tile inventory to player inventory
		else if (!this.mergeItemStack(itemstack, 1, 37, true)) {
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

	@Override
	@Nonnull
	public ItemStack slotClick (int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
		if (slotId >= 54 && slotId <= 68) {
			Slot baseSlot = getSlot(slotId);
			if (!(baseSlot instanceof SlotRecipeHandler)) {
				return super.slotClick(slotId, dragType, clickTypeIn, player);
			}

			SlotRecipeHandler slot = (SlotRecipeHandler) baseSlot;

			if (world.isRemote) {
				tile.setRecipe(slot.getRelativeIndex());
			}
			updateRecipe();
			if (player.world.isRemote) {

				updateRecipeGUI.run();
			}

			return ItemStack.EMPTY;
		}

		return super.slotClick(slotId, dragType, clickTypeIn, player);
	}

	@Override
	public boolean canMergeSlot (ItemStack p_94530_1_, Slot p_94530_2_) {
		return super.canMergeSlot(p_94530_1_, p_94530_2_);
	}

	@Override
	public void onContainerClosed (EntityPlayer playerIn) {
		super.onContainerClosed(playerIn);
	}

	@Override
	public boolean canInteractWith (@Nonnull EntityPlayer playerIn) {
		return true;
	}

	public Map<GCTRecipe, Boolean> updateRecipeStatus () {
		Map<GCTRecipe, Boolean> map = new HashMap<>();

		for (GCTRecipe recipe : GCTRecipeList.instance.getRecipeList()) {
			map.put(recipe, recipe.matches(combinedInventory));
		}

		return map;
	}

	public void updateLastRecipeFromServer (GCTRecipe recipe) {
		tile.setLastRecipe(recipe);
		if (recipe != null) {
			tileInventory.setStackInSlot(0, recipe.getRecipeOutput());
		}
	}

	public GemCuttersTableTileEntity getTile () {
		return tile;
	}

	// Slot range INCLUDES startSlot EXCLUDES stopSlot
	public List<Slot> getSlotRange (int startSlot, int stopSlot) {
		List<Slot> output = new ArrayList<>();
		for (int i = startSlot; i < stopSlot; i++) {
			output.add(getSlot(i));
		}
		return output;
	}

	public Map<ContainerSection, List<Slot>> map = null;

	@ContainerSectionCallback
	@SuppressWarnings("unused")
	public Map<ContainerSection, List<Slot>> containerSectionListMap () {
		if (map == null) {
			map = new HashMap<>();
			map.put(ContainerSection.INVENTORY, playerSlots);
			map.put(ContainerSection.CRAFTING_OUT, Collections.singletonList(slotOutput));
			map.put(ContainerSection.INVENTORY_HOTBAR, playerHotbar);
			map.put(ContainerSection.CHEST, inventorySlots);
		}

		return map;
	}
}

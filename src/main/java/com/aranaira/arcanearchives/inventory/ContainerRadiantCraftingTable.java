package com.aranaira.arcanearchives.inventory;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.inventory.handlers.InventoryCraftingPersistent;
import com.aranaira.arcanearchives.inventory.slots.SlotCraftingFastWorkbench;
import com.aranaira.arcanearchives.inventory.slots.SlotIRecipe;
import com.aranaira.arcanearchives.network.AAPacketHandler;
import com.aranaira.arcanearchives.network.PacketRadiantCrafting;
import com.aranaira.arcanearchives.tileentities.RadiantCraftingTableTileEntity;
import net.minecraft.client.gui.GuiScreen;
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
public class ContainerRadiantCraftingTable extends Container
{
	protected final BlockPos pos;
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

	public ContainerRadiantCraftingTable(RadiantCraftingTableTileEntity tile, EntityPlayer player, InventoryPlayer playerInventory)
	{
		super();

		this.tile = tile;

		this.world = tile.getWorld();
		this.pos = tile.getPos();
		this.itemHandler = tile.getInventory();

		craftResult = new InventoryCraftResult();
		craftMatrix = new InventoryCraftingPersistent(this, itemHandler, tile, 3, 3);
		this.player = player;

		this.addSlotToContainer(new SlotCraftingFastWorkbench(this, playerInventory.player, this.craftMatrix, this.craftResult, 0, 136, 42));

		this.recipeSlots.set(0, new SlotIRecipe(this, tile, player, 0, 174, 16));
		this.recipeSlots.set(1, new SlotIRecipe(this, tile, player, 1, 174, 42));
		this.recipeSlots.set(2, new SlotIRecipe(this, tile, player, 2, 174, 68));

		this.addSlotToContainer(recipeSlots.get(0));
		this.addSlotToContainer(recipeSlots.get(1));
		this.addSlotToContainer(recipeSlots.get(2));

		int i;
		int j;

		for(i = 0; i < 3; ++i)
		{
			for(j = 0; j < 3; ++j)
			{
				this.addSlotToContainer(new Slot(this.craftMatrix, j + i * 3, 24 + j * 18, 24 + i * 18));
			}
		}

		int index = 9;

		for(int row = 0; row < 3; row++)
		{
			for(int col = 0; col < 9; col++)
			{
				this.addSlotToContainer(new Slot(playerInventory, index, 23 + col * 18, 115 + row * 18));
				index++;
			}
		}

		index = 0;
		for(int col = 0; col < 9; col++)
		{
			this.addSlotToContainer(new Slot(playerInventory, index, 23 + col * 18, 173));
			index++;
		}
	}

	@SubscribeEvent
	public static void onCraftingStationGuiOpened(PlayerContainerEvent.Open event)
	{
		// by default the container does not update after it has been opened.
		// we need it to check its recipe
		if(event.getContainer() instanceof ContainerRadiantCraftingTable)
		{
			((ContainerRadiantCraftingTable) event.getContainer()).onCraftMatrixChanged();
		}
	}

	public void saveLastRecipe () {
		actualLastLastRecipe = actualLastRecipe;
		actualLastRecipe = lastRecipe;
	}

	@Override
	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player)
	{
		if(slotId == -999) return super.slotClick(slotId, dragType, clickTypeIn, player);

		Slot slot = getSlot(slotId);
		if(!(slot instanceof SlotIRecipe)) return super.slotClick(slotId, dragType, clickTypeIn, player);

		if(!world.isRemote) return ItemStack.EMPTY;

		SlotIRecipe recipeSlot = (SlotIRecipe) slot;

		int recipeId = recipeSlot.getRecipeIndex();

		// whatever right-click is
		if(GuiScreen.isShiftKeyDown() && dragType == 0)
		{
			tile.setRecipe(recipeId, null);
		} else if(dragType == 1)
		{
			if(GuiScreen.isShiftKeyDown())
			{
				if (actualLastLastRecipe != null)
				{
					tile.setRecipe(recipeId, actualLastLastRecipe);
				}
			} else
			{
				IRecipe cur = CraftingManager.findMatchingRecipe(craftMatrix, world);
				if (cur != null) {
					tile.setRecipe(recipeId, cur);
				} else {
					tile.setRecipe(recipeId, actualLastRecipe);
				}
			}
		} else if(dragType == 0)
		{
			// just a normal click
			IRecipe recipe = tile.getRecipe(recipeId);
			IRecipe cur = CraftingManager.findMatchingRecipe(craftMatrix, world);
			if(recipe != cur)
			{
				ArcaneArchives.logger.info("Now we should put in the previous recipes");
			}
		}

		return ItemStack.EMPTY;
	}

	@Nonnull
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		// slot that was clicked on not empty?
		if(slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			int end = this.inventorySlots.size();

			// Is it a slot in the main inventory? (aka not player inventory)
			if(index < 13)
			{
				// try to put it into the player inventory (if we have a player inventory)
				if(!this.mergeItemStack(itemstack1, 13, end, true))
				{
					return ItemStack.EMPTY;
				}
			}
			// Slot is in the player inventory (if it exists), transfer to main inventory
			else if(!this.mergeItemStack(itemstack1, 3, 13, false))
			{
				return ItemStack.EMPTY;
			}

			if(itemstack1.isEmpty())
			{
				slot.putStack(ItemStack.EMPTY);
			} else
			{
				slot.onSlotChanged();
			}
		}

		return itemstack;
	}

	// update crafting
	@Override
	public void setAll(List<ItemStack> p_190896_1_)
	{
		craftMatrix.setDoNotCallUpdates(true);
		super.setAll(p_190896_1_);
		craftMatrix.setDoNotCallUpdates(false);
		craftMatrix.onCraftMatrixChanged();
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return true;
	}

	public void onCraftMatrixChanged()
	{
		this.onCraftMatrixChanged(this.craftMatrix);
	}

	@Override
	public void onCraftMatrixChanged(IInventory inventoryIn)
	{
		this.slotChangedCraftingGrid(this.world, this.player, this.craftMatrix, this.craftResult);
	}

	@Override
	protected void slotChangedCraftingGrid(World world, EntityPlayer player, InventoryCrafting inv, InventoryCraftResult result)
	{
		ItemStack itemstack = ItemStack.EMPTY;

		if(lastRecipe == null || !lastRecipe.matches(inv, world))
		{
			lastRecipe = CraftingManager.findMatchingRecipe(inv, world);
		}

		if(lastRecipe != null)
		{
			itemstack = lastRecipe.getCraftingResult(inv);
		}

		if(!world.isRemote)
		{
			result.setInventorySlotContents(0, itemstack);
			EntityPlayerMP entityplayermp = (EntityPlayerMP) player;
			if(lastLastRecipe != lastRecipe)
			{
				entityplayermp.connection.sendPacket(new SPacketSetSlot(this.windowId, 0, itemstack));
			} else if(lastLastRecipe != null && lastLastRecipe == lastRecipe && !ItemStack.areItemStacksEqual(lastLastRecipe.getCraftingResult(inv), lastRecipe.getCraftingResult(inv)))
			{
				entityplayermp.connection.sendPacket(new SPacketSetSlot(this.windowId, 0, itemstack));
			}
			AAPacketHandler.CHANNEL.sendTo(new PacketRadiantCrafting.LastRecipeMessage(lastRecipe), entityplayermp);
		}

		lastLastRecipe = lastRecipe;
	}

	@Override
	public boolean canMergeSlot(ItemStack p_94530_1_, Slot p_94530_2_)
	{
		return p_94530_2_.inventory != this.craftResult && super.canMergeSlot(p_94530_1_, p_94530_2_);
	}

	/**
	 * @return the starting slot for the player inventory. Present for usage in the JEI crafting station support
	 */
	public InventoryCrafting getCraftMatrix()
	{
		return craftMatrix;
	}

	public void updateLastRecipeFromServer(IRecipe recipe)
	{
		lastRecipe = recipe;
		if(recipe != null)
		{
			this.craftResult.setInventorySlotContents(0, recipe.getCraftingResult(craftMatrix));
		}
	}

	public NonNullList<ItemStack> getRemainingItems()
	{
		if(lastRecipe != null && lastRecipe.matches(craftMatrix, world))
		{
			return lastRecipe.getRemainingItems(craftMatrix);
		}
		return craftMatrix.stackList;
	}

}


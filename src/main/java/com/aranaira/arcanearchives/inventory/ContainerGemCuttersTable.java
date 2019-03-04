package com.aranaira.arcanearchives.inventory;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.inventory.handlers.InventoryCraftingGCT;
import com.aranaira.arcanearchives.inventory.slots.SlotRecipeHandler;
import com.aranaira.arcanearchives.network.NetworkHandler;
import com.aranaira.arcanearchives.network.PacketGemCutters;
import com.aranaira.arcanearchives.recipe.gct.GCTRecipe;
import com.aranaira.arcanearchives.recipe.gct.GCTRecipeList;
import com.aranaira.arcanearchives.tileentities.GemCuttersTableTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = ArcaneArchives.MODID)
public class ContainerGemCuttersTable extends Container
{
	private IInventory playerInventory;
	private GemCuttersTableTileEntity tile;
	private boolean isServer;
	private GemCuttersTableTileEntity.GemCuttersTableItemHandler tileInventory;
	private Runnable updateRecipeGUI;
	private InventoryCraftResult craftResult;
	private InventoryCraftingGCT craftMatrix;
	private EntityPlayer player;
	private World world;

	private GCTRecipe lastRecipe;
	private GCTRecipe lastLastRecipe;

	public ContainerGemCuttersTable(GemCuttersTableTileEntity tile, EntityPlayer player, boolean serverSide)
	{
		this.tile = tile;
		this.isServer = serverSide;
		this.playerInventory = player.inventory;
		this.tileInventory = tile.getInventory();
		this.player = player;
		this.world = player.world;

		this.craftResult = new InventoryCraftResult();
		this.craftMatrix = InventoryCraftingGCT.build(this, tile, tileInventory, (InventoryPlayer) playerInventory); // TODO: Fake players?

		this.addSlotToContainer(new SlotCrafting(player, craftMatrix, craftResult, 0, 95, 18));
		/*{
			@Override
			public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack)
			{
				this.onCrafting(stack);
				return stack;
			}
		});*/

		for(int i = 0; i < 3; ++i)
		{
			for(int j = 0; j < 9; ++j)
			{
				this.addSlotToContainer(new Slot(craftMatrix, j + i * 9 + 9, 23 + j * 18, 166 + i * 18));
			}
		}

		for(int k = 0; k < 9; ++k)
		{
			this.addSlotToContainer(new Slot(craftMatrix, k, 23 + k * 18, 224));
		}

		int slot = 36;
		for(int i = 0; i < 2; i++)
		{
			for(int j = 0; j < 9; j++)
			{
				this.addSlotToContainer(new Slot(craftMatrix, slot++, 23 + j * 18, 105 + i * 18));
			}
		}

		for(int x = 6; x > -1; x--)
		{
			this.addSlotToContainer(new SlotRecipeHandler(x, x * 18 + 41, 71, tile));
		}
	}

	@Override
	public boolean canInteractWith(@Nonnull EntityPlayer playerIn)
	{
		return true;
	}

	public void setUpdateRecipeGUI(Runnable updateRecipeGUI)
	{
		this.updateRecipeGUI = updateRecipeGUI;
		this.tileInventory.addHook(updateRecipeGUI);
	}

	@Override
	@Nonnull
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		ItemStack stack;
		final Slot slot = inventorySlots.get(index);

		if(slot != null && slot.getHasStack())
		{
			final ItemStack slotStack = slot.getStack();
			stack = slotStack.copy();

			//Chest inventory
			if(index < 36)
			{
				if(!mergeItemStack(slotStack, 36, 54, true)) return ItemStack.EMPTY;
			}
			//Players inventory
			else
			{
				if(!mergeItemStack(slotStack, 0, 36, true)) return ItemStack.EMPTY;
			}

			if(slotStack.isEmpty())
			{
				slot.putStack(ItemStack.EMPTY);
			} else
			{
				slot.onSlotChanged();
			}
		} else
		{
			return ItemStack.EMPTY;
		}

		return stack;
	}

	@Override
	@Nonnull
	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player)
	{
		if(slotId >= 54 && slotId <= 68)
		{
			Slot baseSlot = getSlot(slotId);
			if(!(baseSlot instanceof SlotRecipeHandler))
			{
				return super.slotClick(slotId, dragType, clickTypeIn, player);
			}

			SlotRecipeHandler slot = (SlotRecipeHandler) baseSlot;

			// Client-side call
			getTile().setRecipe(slot.getRelativeIndex()); // getSlot(slotId).getStack());

			if(player.world.isRemote)
			{
				updateRecipeGUI.run();
			}

			return ItemStack.EMPTY;
		}

		return super.slotClick(slotId, dragType, clickTypeIn, player);
	}

	public GemCuttersTableTileEntity getTile()
	{
		return tile;
	}

	public Map<GCTRecipe, Boolean> updateRecipeStatus()
	{
		Map<GCTRecipe, Boolean> map = new HashMap<>();

		for(GCTRecipe recipe : GCTRecipeList.getRecipeList())
		{
			map.put(recipe, recipe.matches(craftMatrix, world));
		}

		return map;
	}

	@Override
	public void onContainerClosed(EntityPlayer playerIn)
	{
		super.onContainerClosed(playerIn);

		this.tileInventory.deleteHook(this.updateRecipeGUI);
	}

	@Override
	public void setAll(List<ItemStack> p_190896_1_)
	{
		craftMatrix.setDoNotCallUpdates(true);
		super.setAll(p_190896_1_);
		craftMatrix.setDoNotCallUpdates(false);
		craftMatrix.onCraftMatrixChanged();
	}

	@SubscribeEvent
	public static void onCraftingStationGuiOpened(PlayerContainerEvent.Open event)
	{
		// by default the container does not update after it has been opened.
		// we need it to check its recipe
		if(event.getContainer() instanceof ContainerGemCuttersTable)
		{
			((ContainerGemCuttersTable) event.getContainer()).onCraftMatrixChanged();
		}
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
		if(updateRecipeGUI != null) updateRecipeGUI.run();

		ItemStack itemstack = ItemStack.EMPTY;

		GCTRecipe curRecipe = tile.getRecipe();
		if(curRecipe != null)
		{
			itemstack = curRecipe.getRecipeOutput().copy();
			if(curRecipe.matches(inv, world))
			{
				result.setInventorySlotContents(0, itemstack);
			} else
			{
				result.setInventorySlotContents(0, ItemStack.EMPTY);
				return;
			}
		} else
		{
			result.setInventorySlotContents(0, ItemStack.EMPTY);
			return;
		}

		if(!world.isRemote)
		{
			EntityPlayerMP entityplayermp = (EntityPlayerMP) player;
			if(lastLastRecipe != lastRecipe)
			{
				entityplayermp.connection.sendPacket(new SPacketSetSlot(this.windowId, 0, itemstack));
			} else if(lastLastRecipe != null && lastLastRecipe == lastRecipe && !ItemStack.areItemStacksEqual(lastLastRecipe.getCraftingResult(inv), lastRecipe.getCraftingResult(inv)))
			{
				entityplayermp.connection.sendPacket(new SPacketSetSlot(this.windowId, 0, itemstack));
			}
			if(lastRecipe != null)
				NetworkHandler.CHANNEL.sendTo(new PacketGemCutters.LastRecipe(lastRecipe), entityplayermp);
		}

		lastLastRecipe = lastRecipe;
	}

	@Override
	public boolean canMergeSlot(ItemStack p_94530_1_, Slot p_94530_2_)
	{
		return p_94530_2_.inventory != this.craftResult && super.canMergeSlot(p_94530_1_, p_94530_2_);
	}

	public void updateLastRecipeFromServer(GCTRecipe recipe)
	{
		lastRecipe = recipe;
		if(recipe != null)
		{
			this.craftResult.setInventorySlotContents(0, recipe.getCraftingResult(craftMatrix));
		}
	}
}

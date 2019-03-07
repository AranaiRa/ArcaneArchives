package com.aranaira.arcanearchives.inventory;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.inventory.handlers.InventoryCraftingGCT;
import com.aranaira.arcanearchives.inventory.slots.SlotRecipeHandler;
import com.aranaira.arcanearchives.network.NetworkHandler;
import com.aranaira.arcanearchives.network.PacketGemCutters;
import com.aranaira.arcanearchives.recipe.gct.GCTRecipe;
import com.aranaira.arcanearchives.recipe.gct.GCTRecipeList;
import com.aranaira.arcanearchives.tileentities.GemCuttersTableTileEntity;
import com.aranaira.arcanearchives.util.types.IngredientStack;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap.Entry;
import it.unimi.dsi.fastutil.ints.IntIterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.*;

import java.util.*;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = ArcaneArchives.MODID)
public class ContainerGemCuttersTable extends Container
{
	private static final int SLOT_OUTPUT = 0;
    private IInventory playerInventory;
	private GemCuttersTableTileEntity tile;
	private boolean isServer;
	private GemCuttersTableTileEntity.GemCuttersTableItemHandler tileInventory;
	private Runnable updateRecipeGUI;
	private EntityPlayer player;
	private World world;
	private final IItemHandlerModifiable outputInv = new ItemStackHandler(1);

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
		
		//Output Slot
		this.addSlotToContainer(new SlotItemHandler(outputInv, SLOT_OUTPUT, 95, 18)
		    {
		        @Override
		        public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack)
		        {
		            for (Entry entry : tile.getRecipe().getMatchingSlots(tileInventory).int2IntEntrySet())
		            {
		                tileInventory.extractItem(entry.getIntKey(), entry.getIntValue(), false);
		            }
                    updateRecipe();
		            return super.onTake(player, stack);
		        }
		        
		        @Override
		        public boolean canTakeStack(EntityPlayer player)
		        {
		            return tile.getRecipe().matches(tileInventory);
		        }
		    });

	    //Player Inventory
		for(int i = 0; i < 3; ++i)
		{
			for(int j = 0; j < 9; ++j)
			{
				this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 23 + j * 18, 166 + i * 18));
			}
		}

		//Player Hotbar
		for(int k = 0; k < 9; ++k)
		{
			this.addSlotToContainer(new Slot(playerInventory, k, 23 + k * 18, 224));
		}
		
		//GCT Inventory
		for(int i = 0; i < 2; i++)
		{
			for(int j = 0; j < 9; j++)
			{
				this.addSlotToContainer(new SlotItemHandler(tileInventory, i * 9 + j, 23 + j * 18, 105 + i * 18)
				    {
				        @Override
				        public void onSlotChanged()
				        {
				            super.onSlotChanged();
				            updateRecipe();
				        }
				    });
			}
		}

		//Recipe Selection Slots
		for(int x = 6; x > -1; x--)
		{
			this.addSlotToContainer(new SlotRecipeHandler(x, x * 18 + 41, 70, tile));
		}
		
		updateRecipe();
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

	        getTile().setRecipe(slot.getRelativeIndex());
			updateRecipe();

			if(player.world.isRemote)
			{
				updateRecipeGUI.run();
			}

			return ItemStack.EMPTY;
		}

		return super.slotClick(slotId, dragType, clickTypeIn, player);
	}

    private void updateRecipe()
    {   
        if(updateRecipeGUI != null) updateRecipeGUI.run();

        ItemStack itemstack = ItemStack.EMPTY;

        GCTRecipe curRecipe = tile.getRecipe();
        if(curRecipe != null)
        {
            itemstack = curRecipe.getRecipeOutput().copy();
            if(curRecipe.matches(tileInventory))
            {
                outputInv.setStackInSlot(SLOT_OUTPUT, itemstack);
            } 
            else
            {
                outputInv.setStackInSlot(SLOT_OUTPUT, ItemStack.EMPTY);
                return;
            }
        } 
        else
        {
            outputInv.setStackInSlot(SLOT_OUTPUT, ItemStack.EMPTY);
            return;
        }

        if(lastLastRecipe != lastRecipe)
        {
            playerInventory.setInventorySlotContents(0, itemstack);
        } 
        else if(lastLastRecipe != null && lastLastRecipe == lastRecipe && !ItemStack.areItemStacksEqual(lastLastRecipe.getRecipeOutput(), lastRecipe.getRecipeOutput()))
        {
            playerInventory.setInventorySlotContents(0, itemstack);
        }
        if(lastRecipe != null && !world.isRemote)
            NetworkHandler.CHANNEL.sendTo(new PacketGemCutters.LastRecipe(lastRecipe), (EntityPlayerMP) player);

        lastLastRecipe = lastRecipe;
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
			map.put(recipe, recipe.matches(tileInventory));
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
	public boolean canMergeSlot(ItemStack p_94530_1_, Slot p_94530_2_)
	{
		return super.canMergeSlot(p_94530_1_, p_94530_2_);
	}

	public void updateLastRecipeFromServer(GCTRecipe recipe)
	{
		lastRecipe = recipe;
		if(recipe != null)
		{
			tileInventory.setStackInSlot(0, recipe.getRecipeOutput());
		}
	}
}

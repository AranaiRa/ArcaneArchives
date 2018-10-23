package com.aranaira.arcanearchives.client;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.util.NetworkHelper;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.SlotItemHandler;

public class NetworkContainer extends Container 
{
	//The crafting inventory required for crafting.
	public InventoryCrafting mInventoryCrafting;
	
	//Required for the crafting inventory, dont ask me.
	private final ContainerWorkbench eventHandler;
	
	public NetworkItemHandler networkItemHandler;
	
	private EntityPlayer player;
	
	
	public NetworkContainer()
	{
		//int i = 45;

		networkItemHandler = new NetworkItemHandler(Minecraft.getMinecraft().player.getUniqueID());
		//127 higher?
		
		int j = 26;
		for (int y = 2; y > -1; y--)
		{
			for (int x = 8; x > -1; x--)
			{
				this.addSlotToContainer(new AASlotItemHandler(networkItemHandler, j, 45 + (18 * x), 33 + (18 * y), Minecraft.getMinecraft().player.getUniqueID()));
				j--;
			}
		}
		
		for (int y = 0; y < 2; y++)
		{
			for (int x = 0; x < 8; x++)
			{
				Slot temp = new Slot(mInventoryCrafting, x, x, x);
			}
		}
		
		eventHandler = new ContainerWorkbench(Minecraft.getMinecraft().player.inventory, Minecraft.getMinecraft().player.world, new BlockPos(0,0,0));
		mInventoryCrafting = new InventoryCrafting(eventHandler, 3, 3);
		player = Minecraft.getMinecraft().player;
		
		for (int x = 0; x < 3; x++)
		{
			for (int y = 0; y < 3; y++)
			{
				eventHandler.inventorySlots.get(y * 3 + x + 1).xPos = 63 + (18 * x);
				eventHandler.inventorySlots.get(y * 3 + x + 1).yPos = 91 + (18 * y);
			}
		}
		
		eventHandler.inventorySlots.get(0).xPos = 171;
		eventHandler.inventorySlots.get(0).yPos = 109;
		
		
		
		for (int i = 0; i < 10; i++)
		{
			this.addSlotToContainer(eventHandler.inventorySlots.get(i));
		}
		
		//Creates the slots for the players inventory.
		int i = 35;
		//Inventory.
		for (int y = 2; y > -1; y--)
		{
			for (int x = 8; x > -1; x--)
			{
				this.addSlotToContainer(new Slot(Minecraft.getMinecraft().player.inventory, i, 45 + (18 * x), 160 + (18 * y)));
				
				i--;
			}
		}
		//Hotbar.
		for (int x = 8; x > -1; x--)
		{
			this.addSlotToContainer(new Slot(Minecraft.getMinecraft().player.inventory, i, 45 + (18 * x), 218));
			i--;
		}
	}
	
	public NetworkContainer(EntityPlayer player)
	{
		this.player = player;
		
		eventHandler = new ContainerWorkbench(player.inventory, player.world, new BlockPos(0,0,0));
		mInventoryCrafting = new InventoryCrafting(eventHandler, 3, 3);


		networkItemHandler = new NetworkItemHandler(player.getUniqueID());
		//127 higher?
		
		int j = 26;
		for (int y = 2; y > -1; y--)
		{
			for (int x = 8; x > -1; x--)
			{
				this.addSlotToContainer(new AASlotItemHandler(networkItemHandler, j, 45 + (18 * x), 40 + (18 * y), player.getUniqueID()));
				j--;
			}
		}

		for (int i = 0; i < 10; i++)
		{
			this.addSlotToContainer(eventHandler.inventorySlots.get(i));
		}
		
		//Creates the slots for the players inventory.
		int i = 35;
		//Inventory.
		for (int y = 2; y > -1; y--)
		{
			for (int x = 8; x > -1; x--)
			{
				this.addSlotToContainer(new Slot(player.inventory, i, 45 + (18 * x), 167 + (18 * y)));
				
				i--;
			}
		}
		//Hotbar.
		for (int x = 8; x > -1; x--)
		{
			this.addSlotToContainer(new Slot(player.inventory, i, 45 + (18 * x), 225));
			i--;
		}
	}
	
	public void SetSearchString(String s)
	{
		networkItemHandler.setSearchString(s);;
	}
	
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public void detectAndSendChanges() {
		IRecipe ir = CraftingManager.findMatchingRecipe(mInventoryCrafting, player.world);
		
		//This is probably a terrible way to go about this, but MEH.
		if (ir != null)
		{
			eventHandler.inventorySlots.get(0).inventory.setInventorySlotContents(eventHandler.inventorySlots.get(0).getSlotIndex(), ir.getCraftingResult(mInventoryCrafting));;
		}
		else
		{
			eventHandler.inventorySlots.get(0).inventory.setInventorySlotContents(eventHandler.inventorySlots.get(0).getSlotIndex(), new ItemStack(Blocks.AIR));
		}
		
		super.detectAndSendChanges();
	}
	
	@Override
	public void putStackInSlot(int slotID, ItemStack stack) 
	{
		
	}
	
	@Override
	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) 
	{
		if (slotId < 27)
		{
			int modifiedSlotID = 26 - slotId;
			
			if (clickTypeIn == ClickType.PICKUP || clickTypeIn == ClickType.PICKUP_ALL)
			{
				if (!player.inventory.getItemStack().isEmpty())
				{
					player.inventory.setItemStack(NetworkHelper.getArcaneArchivesNetwork(player.getUniqueID()).InsertItem(player.inventory.getItemStack(), player.world.isRemote));
				}
				else
				{
					player.inventory.setItemStack(NetworkHelper.getArcaneArchivesNetwork(player.getUniqueID()).ExtractItem(networkItemHandler.getStackInSlot(modifiedSlotID), networkItemHandler.getStackInSlot(modifiedSlotID).getCount(), player.world.isRemote));
				}
			}
			else if (clickTypeIn == ClickType.QUICK_MOVE)
			{
				player.inventory.addItemStackToInventory(NetworkHelper.getArcaneArchivesNetwork(player.getUniqueID()).ExtractItem(networkItemHandler.getStackInSlot(modifiedSlotID), networkItemHandler.getStackInSlot(modifiedSlotID).getCount(), player.world.isRemote));
			}
			
			return player.inventory.getItemStack();
		}
		
		if (clickTypeIn == ClickType.QUICK_MOVE)
		{
			this.inventorySlots.get(slotId).inventory.setInventorySlotContents(this.inventorySlots.get(slotId).getSlotIndex(), NetworkHelper.getArcaneArchivesNetwork(player.getUniqueID()).InsertItem(this.inventorySlots.get(slotId).getStack(), player.world.isRemote));
			
			return player.inventory.getItemStack();
		}
		
		return super.slotClick(slotId, dragType, clickTypeIn, player);
	}
}

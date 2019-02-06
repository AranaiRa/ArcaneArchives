package com.aranaira.arcanearchives.common;

import com.aranaira.arcanearchives.data.NetworkHelper;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class NetworkContainer extends Container
{
	//The crafting inventory required for crafting.
	private InventoryCrafting mInventoryCrafting;

	//Required for the crafting inventory, dont ask me.
	private final ContainerWorkbench eventHandler;

	private NetworkItemHandler networkItemHandler;

	private EntityPlayer player;


	public NetworkContainer(EntityPlayer player)
	{
		this.player = player;
		InventoryPlayer playerInventory = player.inventory;
		//int i = 45;

		networkItemHandler = new NetworkItemHandler(player.getUniqueID());
		//127 higher?

		int j = 26;
		for(int y = 2; y > -1; y--)
		{
			for(int x = 8; x > -1; x--)
			{
				this.addSlotToContainer(new NetworkSlotItemHandler(networkItemHandler, j, 46 + (18 * x), 33 + (18 * y), player));
				j--;
			}
		}

		for(int y = 0; y < 2; y++)
		{
			for(int x = 0; x < 8; x++)
			{
				Slot temp = new Slot(mInventoryCrafting, x, x, x);
			}
		}

		eventHandler = new ContainerWorkbench(playerInventory, player.world, new BlockPos(0, 0, 0));
		mInventoryCrafting = new InventoryCrafting(eventHandler, 3, 3);

		for(int x = 0; x < 3; x++)
		{
			for(int y = 0; y < 3; y++)
			{
				eventHandler.inventorySlots.get(y * 3 + x + 1).xPos = 64 + (18 * x);
				eventHandler.inventorySlots.get(y * 3 + x + 1).yPos = 91 + (18 * y);
			}
		}

		eventHandler.inventorySlots.get(0).xPos = 171;
		eventHandler.inventorySlots.get(0).yPos = 109;


		for(int i = 0; i < 10; i++)
		{
			this.addSlotToContainer(eventHandler.inventorySlots.get(i));
		}

		//Creates the slots for the players inventory.
		int i = 35;
		//Inventory.
		for(int y = 2; y > -1; y--)
		{
			for(int x = 8; x > -1; x--)
			{
				this.addSlotToContainer(new Slot(playerInventory, i, 46 + (18 * x), 160 + (18 * y)));

				i--;
			}
		}
		//Hotbar.
		for(int x = 8; x > -1; x--)
		{
			this.addSlotToContainer(new Slot(playerInventory, i, 46 + (18 * x), 218));
			i--;
		}
	}

	public void SetSearchString(String s)
	{
		networkItemHandler.setSearchString(s);
	}


	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return true;
	}

	@Override
	public void detectAndSendChanges()
	{
		eventHandler.detectAndSendChanges();

		super.detectAndSendChanges();
	}

	@Override
	public void onCraftMatrixChanged(IInventory inventoryIn)
	{
		eventHandler.onCraftMatrixChanged(inventoryIn);
		super.onCraftMatrixChanged(inventoryIn);
	}

	@Override
	protected void slotChangedCraftingGrid(
			World p_192389_1_, EntityPlayer p_192389_2_, InventoryCrafting p_192389_3_, InventoryCraftResult p_192389_4_)
	{
		super.slotChangedCraftingGrid(p_192389_1_, p_192389_2_, p_192389_3_, p_192389_4_);
	}


	@Override
	public void putStackInSlot(int slotID, ItemStack stack)
	{
		//Not sure if this really does anything.
		super.putStackInSlot(slotID, stack);
	}

	// TODO: Lots of client crashes
	@Override
	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player)
	{
		if(slotId < 27 && slotId > -1)
		{
			int modifiedSlotID = 26 - slotId;

			if(clickTypeIn == ClickType.PICKUP || clickTypeIn == ClickType.PICKUP_ALL)
			{
				if(!player.inventory.getItemStack().isEmpty())
				{
					player.inventory.setItemStack(NetworkHelper.getArcaneArchivesNetwork(player.getUniqueID()).InsertItem(player.inventory.getItemStack(), player.world.isRemote));
				} else
				{
					player.inventory.setItemStack(NetworkHelper.getArcaneArchivesNetwork(player.getUniqueID()).ExtractItem(networkItemHandler.getStackInSlot(modifiedSlotID), networkItemHandler.getStackInSlot(modifiedSlotID).getCount(), player.world.isRemote));
				}
			} else if(clickTypeIn == ClickType.QUICK_MOVE)
			{
				player.inventory.addItemStackToInventory(NetworkHelper.getArcaneArchivesNetwork(player.getUniqueID()).ExtractItem(networkItemHandler.getStackInSlot(modifiedSlotID), networkItemHandler.getStackInSlot(modifiedSlotID).getCount(), player.world.isRemote));
			}

			return player.inventory.getItemStack();
		}

		if(clickTypeIn == ClickType.QUICK_MOVE)
		{
			this.inventorySlots.get(slotId).inventory.setInventorySlotContents(this.inventorySlots.get(slotId).getSlotIndex(), NetworkHelper.getArcaneArchivesNetwork(player.getUniqueID()).InsertItem(this.inventorySlots.get(slotId).getStack(), player.world.isRemote));

			return player.inventory.getItemStack();
		}

		return super.slotClick(slotId, dragType, clickTypeIn, player);
	}
}

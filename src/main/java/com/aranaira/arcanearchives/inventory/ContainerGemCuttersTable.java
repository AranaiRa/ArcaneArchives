package com.aranaira.arcanearchives.inventory;

import com.aranaira.arcanearchives.inventory.slots.SlotRecipeHandler;
import com.aranaira.arcanearchives.registry.crafting.GemCuttersTableRecipe;
import com.aranaira.arcanearchives.tileentities.GemCuttersTableTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import scala.xml.dtd.EMPTY;

import javax.annotation.Nonnull;

public class ContainerGemCuttersTable extends Container
{
	private GemCuttersTableTileEntity tile;
	private boolean isServer;
	private IInventory playerInventory;
	private ItemStackHandler tileInventory;
	private ItemStackHandler tileOutput;
	private SlotOutput outputSlot;

	public ContainerGemCuttersTable(GemCuttersTableTileEntity tile, IInventory playerInventory, boolean serverSide)
	{
		this.tile = tile;
		this.isServer = serverSide;
		this.playerInventory = playerInventory;
		this.tileInventory = tile.getInventory();
		this.tileOutput = tile.getOutput();

		int i = 35;
		for(int y = 2; y > -1; y--)
		{
			for(int x = 8; x > -1; x--)
			{
				this.addSlotToContainer(new Slot(playerInventory, i, 23 + (18 * x), 166 + (18 * y)));
				i--;
			}
		}

		for(int x = 8; x > -1; x--)
		{
			this.addSlotToContainer(new Slot(playerInventory, i, 23 + (18 * x), 224));
			i--;
		}

		outputSlot = new SlotOutput(tileOutput, this, 95, 18);

		this.addSlotToContainer(outputSlot);

		//selector - 1 - 8
		{
			int y = 0;

			for(int x = 6; x > -1; x--)
			{
				this.addSlotToContainer(new SlotRecipeHandler(x, x * 18 + 41, y * 18 + 70, tile));
			}
		}

		i = 17;
		for(int y = 1; y > -1; y--)
		{
			for(int x = 8; x > -1; x--)
			{
				this.addSlotToContainer(new SlotItemHandler(tileInventory, i, x * 18 + 23, y * 18 + 105));
				i--;
			}
		}
	}

	@Override
	public boolean canInteractWith(@Nonnull EntityPlayer playerIn)
	{
		return true;
	}

	@Override
	@Nonnull
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		ItemStack stack = ItemStack.EMPTY;
		final Slot slot = inventorySlots.get(index);

		if(slot != null && slot.getHasStack())
		{
			final ItemStack slotStack = slot.getStack();
			stack = slotStack.copy();

			//Chest inventory
			if(index < 36)
			{
				if(!mergeItemStack(slotStack, 45, 62, true)) return ItemStack.EMPTY;
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
		}

		return stack;
	}

	@Override
	protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection)
	{
		boolean temp = super.mergeItemStack(stack, startIndex, endIndex, reverseDirection);
		this.getTile().updateOutput();
		return temp;
	}

	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
	}

	@Override
	@Nonnull
	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player)
	{
		if(!player.world.isRemote)
		{
			if(slotId <= 43 && slotId >= 37)
			{
				getTile().setRecipe(getSlot(slotId).getStack());
				return ItemStack.EMPTY;
			}
		}

		if (!player.world.isRemote && slotId == 36)
		{
			GemCuttersTableRecipe recipe = getTile().getRecipe();
			if(recipe == null) return ItemStack.EMPTY;

			if(recipe.matchesRecipe(tileInventory, new InvWrapper(playerInventory)))
			{
				return super.slotClick(slotId, dragType, clickTypeIn, player);
			}
			else
			{
				return ItemStack.EMPTY;
			}
		}

		return super.slotClick(slotId, dragType, clickTypeIn, player);
	}

	public GemCuttersTableTileEntity getTile()
	{
		return tile;
	}

	public boolean getRecipeStatus()
	{
		GemCuttersTableRecipe recipe = getTile().getRecipe();
		if(recipe == null)
		{
			return false;
		}

		return recipe.matchesRecipe(tileInventory, new InvWrapper(playerInventory));
	}

	public class SlotOutput extends SlotItemHandler
	{
		private ItemStack stack = ItemStack.EMPTY;
		private Container cont;

		SlotOutput(IItemHandler handler, Container cont, int xPosition, int yPosition)
		{
			super(handler, 0, xPosition, yPosition);
			this.cont = cont;
		}

		@Override
		public boolean isItemValid(@Nonnull ItemStack stack)
		{
			return false;
		}

		@Override
		public void onSlotChanged()
		{
		}

		@Override
		public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack)
		{
			GemCuttersTableRecipe recipe = getTile().getRecipe();
			if(recipe == null) return ItemStack.EMPTY;
			GemCuttersTableTileEntity tile = getTile();
			ItemStackHandler tileInv = tile.getInventory();
			InvWrapper ply = new InvWrapper(playerInventory);

			if(thePlayer.world.isRemote)
			{
				if(!recipe.matchesRecipe(tileInv, ply))
				{
					stack = ItemStack.EMPTY;
				}
			} else
			{
				if(!recipe.matchesRecipe(tileInv, ply))
				{
					stack = ItemStack.EMPTY;
				} else if(!recipe.consume(tileInv, ply))
				{
					stack = ItemStack.EMPTY;
				}
			}

			if (thePlayer instanceof EntityPlayerMP)
			{
				tile.updateOutput();
				((EntityPlayerMP) thePlayer).sendAllContents(cont, cont.getInventory());
			}

			return stack;
		}
	}
}

package com.aranaira.arcanearchives.inventory.slots;

import com.aranaira.arcanearchives.inventory.ContainerGemCuttersTable;
import com.aranaira.arcanearchives.registry.crafting.GemCuttersTableRecipe;
import com.aranaira.arcanearchives.tileentities.GemCuttersTableTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nonnull;

public class SlotGCTOutput extends SlotItemHandler
{
	private ContainerGemCuttersTable containerGemCuttersTable;
	private Container cont;

	public SlotGCTOutput(ContainerGemCuttersTable containerGemCuttersTable, IItemHandler handler, Container cont, int xPosition, int yPosition)
	{
		super(handler, 0, xPosition, yPosition);
		this.containerGemCuttersTable = containerGemCuttersTable;
		this.cont = cont;
	}

	@Override
	public boolean isItemValid(@Nonnull ItemStack stack)
	{
		return false;
	}

	@Override
	public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack)
	{
		GemCuttersTableRecipe recipe = containerGemCuttersTable.getTile().getRecipe();
		if(recipe == null) return ItemStack.EMPTY;
		GemCuttersTableTileEntity tile = containerGemCuttersTable.getTile();
		ItemStackHandler tileInv = tile.getInventory();
		InvWrapper ply = new InvWrapper(containerGemCuttersTable.playerInventory);

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

		if(thePlayer instanceof EntityPlayerMP)
		{
			tile.updateRecipe();
			tile.updateOutput();
			((EntityPlayerMP) thePlayer).sendAllContents(cont, cont.getInventory());
		}

		return stack;
	}
}

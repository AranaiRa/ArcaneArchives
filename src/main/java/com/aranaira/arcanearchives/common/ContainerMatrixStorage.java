package com.aranaira.arcanearchives.common;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.data.ArcaneArchivesNetwork;
import com.aranaira.arcanearchives.init.RecipeLibrary;
import com.aranaira.arcanearchives.tileentities.MatrixStorageTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantCraftingTableTileEntity;
import com.aranaira.arcanearchives.util.NetworkHelper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerMatrixStorage extends Container 
{
	MatrixStorageTileEntity mTileEntity;
	
	public ContainerMatrixStorage(MatrixStorageTileEntity MSTE, IInventory playerInventory)
	{
		//ArcaneArchivesNetwork aanetwork = NetworkHelper.getArcaneArchivesNetwork(playerIn.getUniqueID());
		mTileEntity = MSTE;
		
		ArcaneArchives.logger.info("^^^^NULL CHECKS");
		ArcaneArchives.logger.info("inv null? "+playerInventory.equals(null));
		ArcaneArchives.logger.info("te null? "+MSTE.equals(null));
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}
}

package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.common.ContainerRadiantChest;
import com.aranaira.arcanearchives.init.BlockLibrary;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class RadiantChestTileEntity extends TileEntity implements ITickable 
{
	private final IItemHandler inventory = new ItemStackHandler(54);
	
	public RadiantChestTileEntity() 
	{
		BlockLibrary.TILE_ENTITIES.put("radiantchest", this);
	}
	
	@Override
	public void update() 
	{
		
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) 
	{
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory);
		return super.getCapability(capability, facing);
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) 
	{
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return true;
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		// Inventory
		CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(inventory, null, compound.getTagList("inventory", NBT.TAG_COMPOUND));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		// Inventory
		compound.setTag("inventory", CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(inventory, null));
		
		return compound;
	}
}

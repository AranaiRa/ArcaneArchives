package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.common.AAItemStackHandler;
import com.aranaira.arcanearchives.common.ContainerRadiantChest;
import com.aranaira.arcanearchives.init.BlockLibrary;

import net.minecraft.client.Minecraft;
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
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class RadiantChestTileEntity extends ImmanenceTileEntity implements ITickable 
{
	private String mName = "";
	private final IItemHandler mInventory = new AAItemStackHandler(54);
	
	public RadiantChestTileEntity() 
	{
		super("radiantchest");
		this.ImmanenceDrain = 0;
		this.ImmanenceGeneration = 0;
		this.IsInventory = false;
	}
	
	@Override
	public void update() 
	{
		
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) 
	{
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(mInventory);
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
		CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(mInventory, null, compound.getTagList("inventory", NBT.TAG_COMPOUND));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		// Inventory
		compound.setTag("inventory", CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(mInventory, null));
		
		return compound;
	}
	
	public String getName()
	{
		return mName;
	}

	public void setContents(ItemStack[] chestContents, ItemStack[] secondaryChestContents, boolean secondaryChest) 
	{
		for (int i = 0; i < chestContents.length; i++)
		{
			mInventory.insertItem(i, chestContents[i], false);
		}
		if (secondaryChest)
			for (int i = 0; i < secondaryChestContents.length; i++)
			{
				mInventory.insertItem(i + 27, secondaryChestContents[i], false);
			}
	}

	public void setFacing(EnumFacing chestFacing) {
	
		
	}
	
	
}

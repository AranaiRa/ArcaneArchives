package com.aranaira.arcanearchives.tileentities;

import javax.annotation.Nullable;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.common.ContainerRadiantChest;
import com.aranaira.arcanearchives.common.GCTItemHandler;
import com.aranaira.arcanearchives.init.BlockLibrary;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
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

public class GemcuttersTableTileEntity extends AATileEntity implements ITickable
{
	private String mName = "gemcutterstable";
	private final IItemHandler mInventory = new GCTItemHandler(54);
	
	public GemcuttersTableTileEntity() 
	{
		BlockLibrary.TILE_ENTITIES.put(mName, this);
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
	
	
	
	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound tag = super.getUpdateTag();
		if (((GCTItemHandler)this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)).mRecipe != null)
		{
			NBTTagCompound item = ((GCTItemHandler)this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)).mRecipe.getOutput().writeToNBT(tag);
			tag.setTag("recipe", item);
		}
		tag.setInteger("page", ((GCTItemHandler)this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)).getPage());
		return tag;
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) 
	{
		if (pkt.getNbtCompound().hasKey("recipe"))
			((GCTItemHandler)this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)).setRecipe(new ItemStack((NBTTagCompound) pkt.getNbtCompound().getTag("recipe")));
		((GCTItemHandler)this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)).setPage(pkt.getNbtCompound().getInteger("page"));
		super.onDataPacket(net, pkt);
	}
	

    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket()
    {
    	NBTTagCompound compound = new NBTTagCompound();
    	
    	if (((GCTItemHandler)this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)).mRecipe != null)
		{
			NBTTagCompound item = ((GCTItemHandler)this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)).mRecipe.getOutput().writeToNBT(compound);
			compound.setTag("recipe", item);
		}
    	
		compound.setInteger("page", ((GCTItemHandler)this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)).getPage());
    	
    	SPacketUpdateTileEntity spute = new SPacketUpdateTileEntity(pos, 0, compound);
        return spute;
    }
}

package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.common.AAItemStackHandler;
import com.aranaira.arcanearchives.util.LargeItemNBTUtil;
import com.aranaira.arcanearchives.util.LargeSlotSerialization;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Iterator;

public class RadiantChestTileEntity extends ImmanenceTileEntity implements LargeSlotSerialization
{
	public String mName = "";
	private final AAItemStackHandler mInventory = new AAItemStackHandler(54);

	public RadiantChestTileEntity()
	{
		super("radiantchest");
		this.ImmanenceDrain = 0;
		this.ImmanenceGeneration = 0;
		this.IsInventory = false;
	}

	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing)
	{
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(mInventory);
		return super.getCapability(capability, facing);
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing facing)
	{
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return true;
		return super.hasCapability(capability, facing);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		// Inventory
		//CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(mInventory, null, compound.getTagList("inventory", NBT.TAG_COMPOUND));
		NBTTagList tags = compound.getTagList("radiant_inventory", 10);
		deserializeHandler(tags, mInventory);
		mName = compound.getString("name");
	}

	@Override
	@Nonnull
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		// Inventory
		//compound.setTag("inventory", CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(mInventory, null));

		compound.setTag("radiant_inventory", serializeHandler(mInventory));
		compound.setString("name", mName);

		return super.writeToNBT(compound);
	}

	public String getName()
	{
		return mName;
	}

	public void setContents(ItemStack[] chestContents, ItemStack[] secondaryChestContents, boolean secondaryChest)
	{
		for(int i = 0; i < chestContents.length; i++)
		{
			mInventory.insertItem(i, chestContents[i], false);
		}
		if(secondaryChest) for(int i = 0; i < secondaryChestContents.length; i++)
		{
			mInventory.insertItem(i + 27, secondaryChestContents[i], false);
		}
	}

	public void setFacing(EnumFacing chestFacing)
	{


	}

	public boolean Contains(ItemStack item)
	{
		for(int i = 0; i < 54; i++)
		{
			if(ItemStack.areItemsEqual(item, mInventory.getStackInSlot(i)))
			{
				return true;
			}
		}
		return false;
	}

	public AAItemStackHandler getInventory () {
		return mInventory;
	}

	@Override
	@Nonnull
	public NBTTagCompound getUpdateTag()
	{
		NBTTagCompound tag = super.getUpdateTag();
		tag.setString("name", mName);
		return tag;
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		mName = pkt.getNbtCompound().getString("name");
		super.onDataPacket(net, pkt);
	}


	@Nullable
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		NBTTagCompound compound = new NBTTagCompound();
		compound.setString("name", mName);
		return new SPacketUpdateTileEntity(pos, 0, compound);
	}

}

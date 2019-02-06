package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.inventory.handlers.AAItemStackHandler;
import com.aranaira.arcanearchives.util.LargeSlotSerialization;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;

public class RadiantChestTileEntity extends ImmanenceTileEntity implements ITickable, LargeSlotSerialization
{
	private final AAItemStackHandler mInventory = new AAItemStackHandler(54);
	public String chestName = "";

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
		NBTTagCompound inv = compound.getCompoundTag("radiant_inventory");
		largeDeserializeHandler(inv, mInventory);
		chestName = compound.getString("chestName");
	}

	@Override
	@Nonnull
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		// Inventory
		//compound.setTag("inventory", CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(mInventory, null));

		compound.setTag("radiant_inventory", largeSerializeHandler(mInventory));
		compound.setString("chestName", chestName);

		return compound;
	}

	@Override
	public void update()
	{
		super.update();
	}

	public String getChestName()
	{
		return chestName;
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

	public AAItemStackHandler getInventory()
	{
		return mInventory;
	}

	@Override
	public NBTTagCompound getUpdateTag()
	{
		return writeToNBT(new NBTTagCompound());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		ArcaneArchives.logger.info(Minecraft.getMinecraft().player.getDisplayNameString());
		readFromNBT(pkt.getNbtCompound());
		super.onDataPacket(net, pkt);
	}

	@Nonnull
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		NBTTagCompound compound = writeToNBT(new NBTTagCompound());

		return new SPacketUpdateTileEntity(pos, 0, compound);
	}
}

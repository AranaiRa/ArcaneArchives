package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.network.NetworkHandler;
import com.aranaira.arcanearchives.network.PacketRadiantChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class RadiantChestTileEntity extends ImmanenceTileEntity implements ITickable
{
	private final ItemStackHandler mInventory = new ItemStackHandler(54);
	public String chestName = "";

	public RadiantChestTileEntity()
	{
		super("radiantchest");
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
		if (!compound.hasKey(AATileEntity.Tags.INVENTORY)) {
			ArcaneArchives.logger.info(String.format("Radiant Chest tile entity at %d/%d/%d is missing its inventory."));
		}
		mInventory.deserializeNBT(compound.getCompoundTag(AATileEntity.Tags.INVENTORY));
		chestName = compound.getString(Tags.CHEST_NAME);
	}

	@Override
	@Nonnull
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		compound.setTag(AATileEntity.Tags.INVENTORY, mInventory.serializeNBT());
		compound.setString(Tags.CHEST_NAME, chestName);

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

	public void setChestName(String newName)
	{
		this.chestName = (newName == null) ? "" : newName;
		this.updateChestName();
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

	public ItemStackHandler getInventory()
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
		readFromNBT(pkt.getNbtCompound());
		super.onDataPacket(net, pkt);
	}

	@Override
	@Nonnull
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		NBTTagCompound compound = writeToNBT(new NBTTagCompound());

		return new SPacketUpdateTileEntity(pos, 0, compound);
	}

	private void updateChestName()
	{
		if(world == null) return;

		if (this.world.isRemote)
		{
			PacketRadiantChest.SetName packet = new PacketRadiantChest.SetName(getPos(), getChestName(), world.provider.getDimension());
			NetworkHandler.CHANNEL.sendToServer(packet);
		}
	}

	public static class Tags
	{
		public static final String CHEST_NAME = "chestName";

		private Tags()
		{
		}
	}
}

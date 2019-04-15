package com.aranaira.arcanearchives.tileentities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nonnull;

public class RadiantTankTileEntity extends ImmanenceTileEntity
{
	public static final int BASE_CAPACITY = Fluid.BUCKET_VOLUME * 16;
	private final FluidTank inventory = new FluidTank(BASE_CAPACITY);
	private int upgrades = 0;

	public void update()
	{
		if(world.isRemote) return;

		defaultServerSideUpdate();
	}

	public RadiantTankTileEntity()
	{
		super("radianttrove");
	}

	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing)
	{
		if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
		{
			return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(inventory);
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing facing)
	{
		if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) return true;
		return super.hasCapability(capability, facing);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		this.inventory.readFromNBT(compound.getCompoundTag(Tags.HANDLER_ITEM));
	}

	@Override
	@Nonnull
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		compound.setTag(Tags.HANDLER_ITEM, this.inventory.writeToNBT(new NBTTagCompound()));

		return compound;
	}

	public FluidTank getInventory()
	{
		return inventory;
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

	public static class Tags
	{
		public static final String HANDLER_ITEM = "handler_item";

		private Tags()
		{
		}
	}
}

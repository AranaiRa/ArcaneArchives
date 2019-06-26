package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.AAGuiHandler;
import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.inventory.handlers.OptionalUpgradesHandler;
import com.aranaira.arcanearchives.inventory.handlers.SizeUpgradeItemHandler;
import com.aranaira.arcanearchives.inventory.handlers.TroveUpgradeItemHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

import javax.annotation.Nonnull;

public class RadiantTankTileEntity extends ImmanenceTileEntity implements IUpgradeableStorage {
	public static final int BASE_CAPACITY = Fluid.BUCKET_VOLUME * 16;
	private final FluidTank inventory = new FluidTank(BASE_CAPACITY);

	private TroveUpgradeItemHandler sizeUpgrades = new TroveUpgradeItemHandler();
	private OptionalUpgradesHandler optionalUpgrades = new OptionalUpgradesHandler();

	public boolean wasCreativeDrop = false;

	public RadiantTankTileEntity () {
		super("radianttank");
	}

	@Override
	public void update () {
		if (world.isRemote) {
			return;
		}

		defaultServerSideUpdate();
	}

	public int getCapacity () {
		return BASE_CAPACITY * (getModifiedCapacity() + 1);
	}

	@Override
	@Nonnull
	public NBTTagCompound writeToNBT (NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setTag(Tags.HANDLER_ITEM, this.inventory.writeToNBT(new NBTTagCompound()));
		compound.setTag(Tags.OPTIONAL_UPGRADES, this.optionalUpgrades.serializeNBT());
		compound.setTag(Tags.SIZE_UPGRADES, this.sizeUpgrades.serializeNBT());

		return compound;
	}

	@Override
	public void readFromNBT (NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.sizeUpgrades.deserializeNBT(compound.getTagList(Tags.SIZE_UPGRADES, NBT.TAG_BYTE));
		this.optionalUpgrades.deserializeNBT(compound.getCompoundTag(Tags.OPTIONAL_UPGRADES));
		validateCapacity();
		this.inventory.readFromNBT(compound.getCompoundTag(Tags.HANDLER_ITEM));
		validateCapacity();
	}

	private void validateCapacity () {
		if (inventory.getCapacity() != getCapacity()) {
			inventory.setCapacity(getCapacity());
		}
	}

	public NBTTagCompound serializeStack (NBTTagCompound tag) {
		if (inventory.getFluid() != null) {
			tag.setTag(FluidHandlerItemStack.FLUID_NBT_KEY, inventory.writeToNBT(new NBTTagCompound()));
		}
		tag.setTag(Tags.SIZE_UPGRADES, sizeUpgrades.serializeNBT());
		tag.setTag(Tags.OPTIONAL_UPGRADES, optionalUpgrades.serializeNBT());
		return tag;
	}

	public FluidTank getInventory () {
		return inventory;
	}

	@Override
	@Nonnull
	public SPacketUpdateTileEntity getUpdatePacket () {
		NBTTagCompound compound = writeToNBT(new NBTTagCompound());

		return new SPacketUpdateTileEntity(pos, 0, compound);
	}

	public void deserializeStack (NBTTagCompound tag) {
		this.sizeUpgrades.deserializeNBT(tag.getTagList(Tags.SIZE_UPGRADES, NBT.TAG_BYTE));
		this.optionalUpgrades.deserializeNBT(tag.getCompoundTag(Tags.OPTIONAL_UPGRADES));
		this.validateCapacity();
		this.inventory.readFromNBT(tag.getCompoundTag(FluidHandlerItemStack.FLUID_NBT_KEY));
		this.validateCapacity();

	}

	@Override
	public NBTTagCompound getUpdateTag () {
		return writeToNBT(new NBTTagCompound());
	}

	@Override
	public void onDataPacket (NetworkManager net, SPacketUpdateTileEntity pkt) {
		readFromNBT(pkt.getNbtCompound());
		super.onDataPacket(net, pkt);
	}

	@Override
	public boolean hasCapability (@Nonnull Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability (@Nonnull Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(inventory);
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public SizeUpgradeItemHandler getSizeUpgradesHandler () {
		return sizeUpgrades;
	}

	@Override
	public OptionalUpgradesHandler getOptionalUpgradesHandler () {
		return optionalUpgrades;
	}

	@Override
	public void handleManipulationInterface (EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (player.world.isRemote) return;

		player.openGui(ArcaneArchives.instance, AAGuiHandler.UPGRADES, world, pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public int getModifiedCapacity () {
		return sizeUpgrades.getUpgradesCount();
	}

	public static class Tags {
		public static final String HANDLER_ITEM = "handler_item";
		public static final String SIZE_UPGRADES = "size_upgrades";
		public static final String OPTIONAL_UPGRADES = "optional_upgrades";

		private Tags () {
		}
	}
}

package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.inventory.handlers.ItemStackWrapper;
import com.aranaira.arcanearchives.inventory.handlers.OptionalUpgradesHandler;
import com.aranaira.arcanearchives.inventory.handlers.SizeUpgradeItemHandler;
import com.aranaira.arcanearchives.tileentities.interfaces.IUpgradeableStorage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RadiantFurnaceTileEntity extends ImmanenceTileEntity implements IUpgradeableStorage {
	private OptionalUpgradesHandler optionalUpgrades = new OptionalUpgradesHandler();

	private ItemStackHandler inventory = new ItemStackHandler(4);
	public ItemStackWrapper fuel = new ItemStackWrapper(0, inventory);
	public ItemStackWrapper input = new ItemStackWrapper(1, inventory);
	public ItemStackWrapper combined = new ItemStackWrapper(0, 1, inventory);
	public ItemStackWrapper output = new ItemStackWrapper(2, 3, inventory);

	public RadiantFurnaceTileEntity () {
		super("radiant_furnace");
	}

	@Override
	public boolean hasCapability (Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
	}

	@Nullable
	@Override
	public <T> T getCapability (Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability != CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return null;
		}

		if (facing == EnumFacing.UP) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(combined);
		} else {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(output);
		}
	}

	@Override
	public SizeUpgradeItemHandler getSizeUpgradesHandler () {
		return null;
	}

	@Override
	public OptionalUpgradesHandler getOptionalUpgradesHandler () {
		return optionalUpgrades;
	}

	@Override
	public int getModifiedCapacity () {
		return 0;
	}

	@Override
	@Nonnull
	public NBTTagCompound writeToNBT (NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setTag(Tags.OPTIONAL_UPGRADES, optionalUpgrades.serializeNBT());
		return compound;
	}

	@Override
	public void readFromNBT (NBTTagCompound compound) {
		super.readFromNBT(compound);
		optionalUpgrades.deserializeNBT(compound.getCompoundTag(Tags.OPTIONAL_UPGRADES));
	}

	public static class Tags {
		public static final String OPTIONAL_UPGRADES = "optional_upgrades";

		public Tags () {}
	}
}

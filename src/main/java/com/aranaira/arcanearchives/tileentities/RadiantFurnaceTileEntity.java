package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.inventory.handlers.OptionalUpgradesHandler;
import com.aranaira.arcanearchives.inventory.handlers.SizeUpgradeItemHandler;
import com.aranaira.arcanearchives.tileentities.interfaces.IUpgradeableStorage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class RadiantFurnaceTileEntity extends ImmanenceTileEntity implements IUpgradeableStorage {
	private OptionalUpgradesHandler optionalUpgrades = new OptionalUpgradesHandler();

	public RadiantFurnaceTileEntity (String name) {
		super(name);
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

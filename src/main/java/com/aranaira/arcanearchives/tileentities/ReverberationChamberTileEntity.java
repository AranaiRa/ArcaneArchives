/*package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.api.immanence.IImmanenceGenerator;
import com.aranaira.arcanearchives.api.immanence.IImmanenceSource;
import com.aranaira.arcanearchives.api.immanence.ImmanenceBonusType;
import com.aranaira.arcanearchives.immanence.ImmanenceSource;
import com.aranaira.arcanearchives.init.ItemRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ReverberationChamberTileEntity extends ImmanenceTileEntity implements IImmanenceGenerator {
	// Current burn time per item is 30 seconds
	public static final int BURN_TIME = 20 * 30;

	private int burnTime;
	private int burnTimeTotal;

	private ItemStackHandler inventory = new ItemStackHandler(1);

	public ReverberationChamberTileEntity () {
		super("conformance_chamber");
	}

	public boolean isBurning () {
		return burnTime > 0;
	}

	@Override
	public boolean hasCapability (Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
	}

	@Nullable
	@Override
	public <T> T getCapability (Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory);
		}

		return null;
	}

	@Override
	public void update () {
		super.update();

		if (world.isRemote) {
			return;
		}

		if (isBurning()) {
			this.burnTime--;
		} else {
			ItemStack fuel = this.inventory.getStackInSlot(0);
			if (fuel.getItem() == ItemRegistry.ECHO) {
				this.burnTime = BURN_TIME;
				this.burnTimeTotal = BURN_TIME;
			}
		}

		this.defaultServerSideUpdate();
	}

	public ItemStackHandler getInventory () {
		return inventory;
	}

	public int getBurnTime () {
		return burnTime;
	}

	public int getBurnTimeTotal () {
		return burnTimeTotal;
	}

	@Override
	@Nonnull
	public NBTTagCompound writeToNBT (NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setTag(Tags.INVENTORY, inventory.serializeNBT());
		compound.setInteger(Tags.BURN_TIME, burnTime);
		compound.setInteger(Tags.BURN_TIME_TOTAL, burnTimeTotal);
		return compound;
	}

	@Override
	public void readFromNBT (NBTTagCompound compound) {
		super.readFromNBT(compound);
		inventory.deserializeNBT(compound.getCompoundTag(Tags.INVENTORY));
		burnTime = compound.getInteger(Tags.BURN_TIME);
		burnTimeTotal = compound.getInteger(Tags.BURN_TIME_TOTAL);
	}

	@Nullable
	@Override
	public IImmanenceSource generateImmanence () {
		if (!isBurning()) {
			return null;
		}

		return new ImmanenceSource("reverberation", 0.5f, ImmanenceBonusType.MULTIPLICATIVE);
	}

	public static class Tags {
		public static final String INVENTORY = "inventory";
		public static final String BURN_TIME = "burn_time";
		public static final String BURN_TIME_TOTAL = "burn_time_total";

		public Tags () {}
	}
}*/

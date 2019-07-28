package com.aranaira.arcanearchives.inventory.handlers;

import com.aranaira.arcanearchives.tileentities.RadiantTroveTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantTroveTileEntity.TroveItemHandler.Tags;
import com.aranaira.arcanearchives.types.enums.UpgradeType;
import com.aranaira.arcanearchives.util.ItemUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TroveItemBlockItemHandler implements ITroveItemHandler, ICapabilityProvider {
	private TroveUpgradeItemHandler upgrades = new TroveUpgradeItemHandler();
	private OptionalUpgradesHandler optionals = new OptionalUpgradesHandler();
	private ItemStack container;

	private static int BASE_COUNT = RadiantTroveTileEntity.BASE_COUNT;
	private int count = 0;
	private ItemStack reference = ItemStack.EMPTY;

	public TroveItemBlockItemHandler (ItemStack container) {
		this.container = container;

		NBTTagCompound tag = ItemUtils.getOrCreateTagCompound(container);
		if (tag.hasKey(RadiantTroveTileEntity.Tags.HANDLER_ITEM)) {
			NBTTagCompound incoming = tag.getCompoundTag(RadiantTroveTileEntity.Tags.HANDLER_ITEM);
			this.count = incoming.getInteger(Tags.COUNT);
			this.reference = new ItemStack(incoming.getCompoundTag(Tags.REFERENCE));
		}

		if (tag.hasKey(RadiantTroveTileEntity.Tags.OPTIONAL_UPGRADES)) {
			this.optionals.deserializeNBT(tag.getCompoundTag(RadiantTroveTileEntity.Tags.OPTIONAL_UPGRADES));
		}

		if (tag.hasKey(RadiantTroveTileEntity.Tags.SIZE_UPGRADES)) {
			this.upgrades.deserializeNBT(tag.getCompoundTag(RadiantTroveTileEntity.Tags.SIZE_UPGRADES));
		}
	}

	public int getUpgrades () {
		return upgrades.getUpgradesCount();
	}

	@Override
	public int getSlots () {
		return 1;
	}

	@Override
	public int getSlotLimit (int slot) {
		return (getUpgrades() + 1) * BASE_COUNT;
	}

	@Override
	public boolean hasCapability (@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
	}

	@Nullable
	@Override
	public <T> T getCapability (@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this);
		} else {
			return null;
		}
	}

	@Override
	public ItemStack getReference () {
		return this.reference;
	}

	@Override
	public int getCount () {
		return count;
	}

	@Override
	public int getMaxCount () {
		return (getUpgrades() + 1) * BASE_COUNT;
	}

	@Override
	public void setCount (int count) {
		this.count = count;
	}

	@Override
	public boolean isVoiding () {
		return this.optionals.hasUpgrade(UpgradeType.VOID);
	}

	@Override
	public void update () {
		saveToStack();
	}

	@Override
	public ItemStack getItem () {
		return reference;
	}

	@Override
	public ItemStack getItemCurrent () {
		if (getCount() == 0) {
			return ItemStack.EMPTY;
		}

		return reference;
	}

	@Override
	public void setReference (ItemStack reference) {
		this.reference = reference;
	}

	@Override
	public boolean isEmpty () {
		return false;
	}

	public void saveToStack () {
		NBTTagCompound tag = ItemUtils.getOrCreateTagCompound(container);
		NBTTagCompound result = new NBTTagCompound();
		result.setInteger(Tags.COUNT, this.count);
		result.setTag(Tags.REFERENCE, this.reference.serializeNBT());
		result.setInteger(Tags.UPGRADES, getUpgrades());
		tag.setTag(RadiantTroveTileEntity.Tags.HANDLER_ITEM, result);
	}
}

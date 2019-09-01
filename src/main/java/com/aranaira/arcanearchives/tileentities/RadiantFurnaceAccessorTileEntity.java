package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.util.WorldUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

public class RadiantFurnaceAccessorTileEntity extends ImmanenceTileEntity {
	private boolean bottom;
	private EnumFacing offset;
	private EnumFacing front;

	public RadiantFurnaceAccessorTileEntity (String name, EnumFacing offset, boolean bottom) {
		super(name);
		this.bottom = bottom;
		// This might need to be opposite
		this.offset = offset;
		this.front = EnumFacing.fromAngle(offset.getHorizontalAngle() - 90);
	}

	@Nullable
	public RadiantFurnaceTileEntity getParent () {
		BlockPos parent = bottom ? getPos().offset(offset) : getPos().down().offset(offset);
		return WorldUtil.getTileEntity(RadiantFurnaceTileEntity.class, world, parent);
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

		RadiantFurnaceTileEntity parent = getParent();
		if (parent == null) {
			return null;
		}

		if (bottom && facing == front) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(parent.fuel);
		} else if (!bottom && facing == EnumFacing.UP) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(parent.combined);
		} else {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(parent.output);
		}
	}
}

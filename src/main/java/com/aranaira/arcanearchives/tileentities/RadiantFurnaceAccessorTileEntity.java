/*package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.tileentities.interfaces.IDirectionalTileEntity;
import com.aranaira.arcanearchives.tileentities.interfaces.INamedTileEntity;
import com.aranaira.arcanearchives.util.WorldUtil;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RadiantFurnaceAccessorTileEntity extends TileEntity implements INamedTileEntity, IDirectionalTileEntity {
	private final String name = "radiant_furnace_accessor";
	private boolean bottom;
	private Direction offset;
	private Direction front;

	public RadiantFurnaceAccessorTileEntity () {
		this(Direction.DOWN, true);
	}

	public RadiantFurnaceAccessorTileEntity (Direction offset, boolean bottom) {
		this.bottom = bottom;
		// This might need to be opposite
		this.offset = offset;
		if (offset != null) {
			this.front = Direction.fromAngle(offset.getHorizontalAngle() - 90);
		}
	}

	@Nullable
	public RadiantFurnaceTileEntity getParent () {
		BlockPos parent = bottom ? getPos().offset(offset) : getPos().down().offset(offset);
		return WorldUtil.getTileEntity(RadiantFurnaceTileEntity.class, world, parent);
	}

	@Override
	public boolean hasCapability (Capability<?> capability, @Nullable Direction facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
	}

	@Nullable
	@Override
	public <T> T getCapability (Capability<T> capability, @Nullable Direction facing) {
		if (capability != CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return null;
		}

		RadiantFurnaceTileEntity parent = getParent();
		if (parent == null) {
			return null;
		}

		if (facing == null) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(parent.combined);
		} else if (bottom && facing == front) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(parent.fuel);
		} else if (!bottom) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(parent.combined);
		} else {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(parent.output);
		}
	}

	@Override
	@Nonnull
	public CompoundNBT writeToNBT (CompoundNBT compound) {
		super.writeToNBT(compound);
		compound.setInteger(Tags.OFFSET, offset.ordinal());
		compound.setBoolean(Tags.BOTTOM, bottom);
		return compound;
	}

	@Override
	public void readFromNBT (CompoundNBT compound) {
		super.readFromNBT(compound);
		offset = Direction.byIndex(compound.getInteger(Tags.OFFSET));
		bottom = compound.getBoolean(Tags.BOTTOM);
	}

	@Override
	public String getName () {
		return this.name;
	}

	@Override
	public void setName (String name) {
	}

	public static class Tags {
		public static final String OFFSET = "facing";
		public static final String BOTTOM = "bottom";

		public Tags () {}
	}

}*/

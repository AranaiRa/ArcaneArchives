package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.blocks.MonitoringCrystal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class MonitoringCrystalTileEntity extends ImmanenceTileEntity implements IManifestTileEntity {
	private BlockPos target = null;

	public MonitoringCrystalTileEntity () {
		super("monitoring_crystal_tile_entity");
	}

	@Override
	public String getDescriptor () {
		return "Monitoring Crystal";
	}

	@Override
	public String getChestName () {
		return "";
	}

	@Override
	@Nullable
	public IItemHandler getInventory () {
		BlockPos tar = getTarget();
		if (tar == null) {
			return null;
		}

		TileEntity te = world.getTileEntity(tar);
		if (te == null) {
			return null;
		}

		// Monitoring Crystals don't work on ITEs.
		if (te instanceof ImmanenceTileEntity) {
			return null;
		}

		if (te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
			return te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		}

		if (te instanceof IInventory) {
			return new InvWrapper((IInventory) te);
		}

		return null;
	}

	@Nullable
	public BlockPos getTarget () {
		if (target == null) {
			IBlockState me = world.getBlockState(getPos());
			EnumFacing facing = me.getValue(MonitoringCrystal.FACING).getOpposite();
			target = getPos().offset(facing);
		}

		return target;
	}
}

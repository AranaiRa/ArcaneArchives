package com.aranaira.arcanearchives.tileentities;

import net.minecraft.block.state.IBlockState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class MonitoringCrystalTileEntity extends ImmanenceTileEntity
{
	public MonitoringCrystalTileEntity()
	{
		super("monitoring_crystal_tile_entity");
	}
}

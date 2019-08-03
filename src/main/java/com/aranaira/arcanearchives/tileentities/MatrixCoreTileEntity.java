package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class MatrixCoreTileEntity extends ImmanenceTileEntity {
	public MatrixCoreTileEntity () {
		super("matrix_core_tile_entity");
		// immanenceGeneration = Integer.MAX_VALUE;
	}

	@Override
	public void update () {

		super.update();
	}

	@Override
	public void onDataPacket (NetworkManager net, SPacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
	}

	@Override
	public boolean shouldRefresh (World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
		return (oldState.getBlock() != newSate.getBlock());
	}
}

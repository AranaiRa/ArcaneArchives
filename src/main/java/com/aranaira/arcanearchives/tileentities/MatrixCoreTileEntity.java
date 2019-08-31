package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.api.immanence.IImmanenceGenerator;
import com.aranaira.arcanearchives.api.immanence.IImmanenceSource;
import com.aranaira.arcanearchives.api.immanence.ImmanenceBonusType;
import com.aranaira.arcanearchives.immanence.ImmanenceSource;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class MatrixCoreTileEntity extends ImmanenceTileEntity implements IImmanenceGenerator {
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

	@Nullable
	@Override
	public IImmanenceSource generateImmanence () {
		return new ImmanenceSource("matrix_core", 1000.0f, ImmanenceBonusType.ADDITIVE);
	}
}

package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.util.handlers.ConfigHandler;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class MatrixStorageTileEntity extends ImmanenceTileEntity
{

	public MatrixStorageTileEntity()
	{
		super("matrix_storage_tile_entity");
		immanenceDrain = ConfigHandler.values.iStorageMatrixDrain;
		// maxItems = ConfigHandler.values.iStorageMatrixItemCap;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{

		return super.writeToNBT(compound);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{

		super.readFromNBT(compound);
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
	{
		return (oldState.getBlock() != newSate.getBlock());
	}


}

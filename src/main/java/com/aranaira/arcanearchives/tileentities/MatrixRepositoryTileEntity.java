package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.util.handlers.ConfigHandler;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MatrixRepositoryTileEntity extends ImmanenceTileEntity {

	
	public MatrixRepositoryTileEntity() {
		super("matrix_repository_tile_entity");
		ImmanenceDrain = ConfigHandler.values.iRepositoryMatrixDrain;
		Inventory = new ItemStack[ConfigHandler.values.iRepositoryMatrixItemCap];
		IsInventory = true;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		
		super.readFromNBT(compound);
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
	{
	    return (oldState.getBlock() != newSate.getBlock());
	}
	
}

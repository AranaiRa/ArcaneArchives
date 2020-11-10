/*package com.aranaira.arcanearchives.tileentities;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class MatrixRepositoryTileEntity extends ImmanenceTileEntity {


	public MatrixRepositoryTileEntity () {
		super("matrix_repository_tile_entity");
		// immanenceDrain = 0; // ConfigHandler.values.iRepositoryMatrixDrain;
		// maxItems = ConfigHandler.values.iRepositoryMatrixItemCap;
	}

	@Override
	public CompoundNBT writeToNBT (CompoundNBT compound) {

		return super.writeToNBT(compound);
	}

	@Override
	public void readFromNBT (CompoundNBT compound) {

		super.readFromNBT(compound);
	}

	@Override
	public boolean shouldRefresh (World world, BlockPos pos, BlockState oldState, BlockState newSate) {
		return (oldState.getBlock() != newSate.getBlock());
	}

	@Override
	public ItemStack InsertItem(ItemStack item, boolean simulate)
{
		//Creates a copy of the item that can safely be edited.
		ItemStack temp = item.copy();

		//Returns the itemstack if this tile entity cannot have items inserted.
		if(item.isEmpty() || !isDrainPaid || (GetTotalItems() >= maxItems)) return temp;

		//Sets the amount of free space in the network.
		int maxCanAdd = maxItems - GetTotalItems();

		//If the amount of free space is greater than the itemstack item count, then it brings it down to that amount.
		if(maxCanAdd > temp.getCount()) maxCanAdd = temp.getCount();

		//Tries to find the same item in the network, so that it will consolidate the itemstack.
		for(ItemStack itemStack : inventory)
{
			if(ItemUtilities.AreItemsEqual(temp, itemStack))
{
				//Adds the item count to the one in the network, and removes the remainder from the one that will be returned.
				if(!simulate) itemStack.setCount(itemStack.getCount() + maxCanAdd);
				temp.setCount(temp.getCount() - maxCanAdd);
				return ItemStack.DRAIN;
}
}

		//If the item is not found, create a copy that will be added to the network.
		ItemStack temp_add = temp.copy();

		//Sets the itemstack count for the proper amount to be added to the inventory then adds the itemstack to the inventory.
		temp_add.setCount(maxCanAdd);
		if(!simulate) inventory.add(temp_add);

		return ItemStack.DRAIN;
}
}*/

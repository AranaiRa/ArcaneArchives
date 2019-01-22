package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.util.ItemComparison;
import com.aranaira.arcanearchives.util.handlers.ConfigHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MatrixRepositoryTileEntity extends ImmanenceTileEntity
{


	public MatrixRepositoryTileEntity()
	{
		super("matrix_repository_tile_entity");
		ImmanenceDrain = ConfigHandler.values.iRepositoryMatrixDrain;
		IsInventory = true;
		MaxItems = ConfigHandler.values.iRepositoryMatrixItemCap;
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

	@Override
	public ItemStack InsertItem(ItemStack item, boolean simulate)
	{
		//Creates a copy of the item that can safely be edited.
		ItemStack temp = item.copy();

		//Returns the itemstack if this tile entity cannot have items inserted.
		if(item.isEmpty() || !IsDrainPaid || (GetTotalItems() >= MaxItems)) return temp;

		//Sets the amount of free space in the network.
		int maxCanAdd = MaxItems - GetTotalItems();

		//If the amount of free space is greater than the itemstack item count, then it brings it down to that amount.
		if(maxCanAdd > temp.getCount()) maxCanAdd = temp.getCount();

		//Tries to find the same item in the network, so that it will consolidate the itemstack.
		for(ItemStack itemStack : Inventory)
		{
			if(ItemComparison.AreItemsEqual(temp, itemStack))
			{
				//Adds the item count to the one in the network, and removes the remainder from the one that will be returned.
				if(!simulate) itemStack.setCount(itemStack.getCount() + maxCanAdd);
				temp.setCount(temp.getCount() - maxCanAdd);
				return ItemStack.EMPTY;
			}
		}

		//If the item is not found, create a copy that will be added to the network.
		ItemStack temp_add = temp.copy();

		//Sets the itemstack count for the proper amount to be added to the inventory then adds the itemstack to the inventory.
		temp_add.setCount(maxCanAdd);
		if(!simulate) Inventory.add(temp_add);

		return ItemStack.EMPTY;
	}
}

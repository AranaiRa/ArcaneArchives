package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.tileentities.MatrixRepositoryTileEntity;
import com.aranaira.arcanearchives.tileentities.MatrixStorageTileEntity;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MatrixStorage extends BlockTemplate {

	public static final String name = "matrix_storage";
	
	public MatrixStorage()
	{
		super(name, Material.GLASS);
		setLightLevel(16/16f);
		this.Height = 3;
	}
	
	@Override
	public boolean hasOBJModel()
	{
		return true;
	}

    @Override
    public boolean isOpaqueCube(IBlockState state) 
    {
        return false;
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
    	return true;
    }
    
	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		tileEntityInstance = new MatrixStorageTileEntity();
		return tileEntityInstance;
	}
	

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) 
	{
		tileEntityInstance.name = name;
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}
}

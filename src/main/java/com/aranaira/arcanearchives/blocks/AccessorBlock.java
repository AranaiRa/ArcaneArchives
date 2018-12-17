package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.init.BlockLibrary;
import com.aranaira.arcanearchives.tileentities.AccessorTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AccessorBlock extends Block 
{
	BlockTemplate Parent;
	BlockPos pos;
	
	public AccessorBlock(Material materialIn) 
	{
		super(materialIn);
		setUnlocalizedName("accessorBlock");
		setRegistryName("accessorBlock");
		BlockLibrary.BLOCKS.add(this);
	}
	
	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) 
	{
		Parent.RemoveChild(this);
		Parent.DestroyChildren(worldIn);
		worldIn.destroyBlock(Parent.pos, true);
		super.onBlockDestroyedByPlayer(worldIn, pos, state);
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) 
	{
		return EnumBlockRenderType.INVISIBLE;
	}

    @Override
    public boolean isOpaqueCube(IBlockState state) 
    {
        return false;
    }
    
    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) 
    {

    	this.pos = pos;
    	
    	super.onBlockAdded(worldIn, pos, state);
    }
    
    @Override
    public boolean hasTileEntity() 
    {
    	return true;
    }
    
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) 
    {
    	return new AccessorTileEntity();
    }
    
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) 
    {
    	if (Parent == null)
    	{
    		return false;
    	}
    	
    	return Parent.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }
}


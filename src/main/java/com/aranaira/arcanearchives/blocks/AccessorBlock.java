package com.aranaira.arcanearchives.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AccessorBlock extends Block 
{
	BlockTemplate Parent;
	BlockPos pos;
	
	public AccessorBlock(Material materialIn) {
		super(materialIn);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
		Parent.RemoveChild(this);
		Parent.DestroyChildren(worldIn);
		worldIn.destroyBlock(Parent.pos, true);
		super.onBlockDestroyedByPlayer(worldIn, pos, state);
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.INVISIBLE;
	}

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }
    
    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {

    	this.pos = pos;
    	
    	super.onBlockAdded(worldIn, pos, state);
    }
}

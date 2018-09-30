package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.init.BlockLibrary;
import com.aranaira.arcanearchives.init.ItemLibrary;
import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import com.aranaira.arcanearchives.util.IHasModel;
import com.aranaira.arcanearchives.util.NetworkHelper;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockTemplate extends Block implements IHasModel {

	public static ImmanenceTileEntity tileEntityInstance;
	public static boolean HasTileEntity;
	public BlockTemplate(String name, Material materialIn) {
		super(materialIn);
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(ArcaneArchives.TAB_AA);
		
		BlockLibrary.BLOCKS.add(this);
		ItemLibrary.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
		
		
	}
	
	public boolean hasOBJModel()
	{
		return false;
	}

	@Override
	public void registerModels()
	{
		ArcaneArchives.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		if (HasTileEntity)
		{

			tileEntityInstance.NetworkID = placer.getUniqueID();
			tileEntityInstance.Dimension = worldIn.getWorldType().getId();
			tileEntityInstance.blockpos = pos;
			if (!worldIn.isRemote)
				NetworkHelper.getArcaneArchivesNetwork(placer.getUniqueID()).AddBlockToNetwork(tileEntityInstance.name, tileEntityInstance);
		}
	}
	

	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
		
		if (tileEntityInstance != null)
			NetworkHelper.getArcaneArchivesNetwork(tileEntityInstance.NetworkID).RemoveBlockFromNetwork(tileEntityInstance);
		
		super.onBlockDestroyedByPlayer(worldIn, pos, state);
	}
}

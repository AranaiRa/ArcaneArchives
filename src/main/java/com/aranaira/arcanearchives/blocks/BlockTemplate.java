package com.aranaira.arcanearchives.blocks;

import java.util.ArrayList;
import java.util.List;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.init.BlockLibrary;
import com.aranaira.arcanearchives.init.ItemLibrary;
import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantChestTileEntity;
import com.aranaira.arcanearchives.util.IHasModel;
import com.aranaira.arcanearchives.util.NetworkHelper;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockTemplate extends Block implements IHasModel {

	public ImmanenceTileEntity tileEntityInstance;
	public int PlaceLimit = -1;
	public String refName;
	
	public List<AccessorBlock> Accessors;
	public int Width = 1;
	public int Height = 1;
	BlockPos pos;
	
	public BlockTemplate(String name, Material materialIn) {
		super(materialIn);
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(ArcaneArchives.TAB_AA);
		refName = name;
		BlockLibrary.BLOCKS.add(this);
		ItemLibrary.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
		Accessors = new ArrayList<AccessorBlock>();
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
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) 
	{
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);

		if (hasTileEntity(getDefaultState()) && tileEntityInstance != null)
		{
			tileEntityInstance.NetworkID = placer.getUniqueID();
			tileEntityInstance.Dimension = worldIn.provider.getDimension();
			tileEntityInstance.blockpos = pos;
			if (!worldIn.isRemote)
			{
				if (tileEntityInstance instanceof RadiantChestTileEntity)
					NetworkHelper.getArcaneArchivesNetwork(placer.getUniqueID()).AddRadiantChest((RadiantChestTileEntity)tileEntityInstance);
				else
					NetworkHelper.getArcaneArchivesNetwork(placer.getUniqueID()).AddBlockToNetwork(tileEntityInstance.name, tileEntityInstance);
			}
		}
		
		if (Height > 1 && Width == 3)
		{
			for (int x = -1; x < 2; x++)
				for (int z = -1; z < 2; z++)
					for (int y = 0; y < 5; y++)
					{
						if (z == 0 && x == 0 && y == 0)
							continue;
						AccessorBlock temp = new AccessorBlock(this.blockMaterial);
						temp.Parent = this;
						temp.setUnlocalizedName(getUnlocalizedName());
						worldIn.setBlockState(pos.add(x, y, z), temp.getBlockState().getBaseState());
						Accessors.add(temp);
					}
		}
		else if (Height > 1 && Width == 1)
		{
			for (int y = 1; y < Height + 1; y++)
			{
				AccessorBlock temp = new AccessorBlock(this.blockMaterial);
				temp.Parent = this;
				temp.setUnlocalizedName(getUnlocalizedName());
				worldIn.setBlockState(pos.add(0, y, 0), temp.getBlockState().getBaseState());
				
				Accessors.add(temp);
			}
		}
		else if (Height == 1 && Width == 2)
		{
			AccessorBlock temp = new AccessorBlock(this.blockMaterial);
			temp.Parent = this;
			temp.setUnlocalizedName(getUnlocalizedName());
			worldIn.setBlockState(pos.add(1, 0, 0), temp.getBlockState().getBaseState());
			Accessors.add(temp);
		}
	}
	

	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
		
		
		DestroyChildren(worldIn);
		
		super.onBlockDestroyedByPlayer(worldIn, pos, state);
	}
	
	public void RemoveChild(AccessorBlock b)
	{
		Accessors.remove(b);
	}
	
	public void DestroyChildren(World worldIn)
	{
		if (!worldIn.isRemote && tileEntityInstance != null)
			NetworkHelper.getArcaneArchivesNetwork(tileEntityInstance.NetworkID).RemoveBlockFromNetwork(tileEntityInstance);
		for (AccessorBlock b : Accessors)
		{
			try 
			{
				worldIn.destroyBlock(b.pos, false);
			}
			catch(Exception e)
			{
				
			}
		}
	}
	
	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		this.pos = pos;
		super.onBlockAdded(worldIn, pos, state);
	}
}

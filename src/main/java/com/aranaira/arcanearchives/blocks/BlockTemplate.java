package com.aranaira.arcanearchives.blocks;

import java.util.ArrayList;
import java.util.List;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.init.BlockLibrary;
import com.aranaira.arcanearchives.init.ItemLibrary;
import com.aranaira.arcanearchives.items.ItemBlockTemplate;
import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantChestTileEntity;
import com.aranaira.arcanearchives.util.IHasModel;
import com.aranaira.arcanearchives.util.NetworkHelper;

import javafx.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockTemplate extends Block implements IHasModel {

	public ImmanenceTileEntity tileEntityInstance;
	public int PlaceLimit = -1;
	public String refName;

	public List<AccessorBlock> Accessors;
	private static int Width = 1;
	private static int Height = 1;
	BlockPos pos;

	public BlockTemplate(String name, Material materialIn) {
		super(materialIn);
		setUnlocalizedName(name);
		setRegistryName(new ResourceLocation(ArcaneArchives.MODID, name));
		setCreativeTab(ArcaneArchives.TAB_AA);
		BlockLibrary.BLOCKS.add(this);
		ItemLibrary.ITEMS.add(new ItemBlockTemplate(this));
		Accessors = new ArrayList<>();
		setHarvestLevel("pickaxe", 0);
	}

	public static int getWidth ()
	{
		return Width;
	}

	public static int getHeight ()
	{
		return Height;
	}

	public static void setWidth (int width)
	{
		Width = width;
	}

	public static void setHeight (int height)
	{
		Height = height;
	}

	public static void setWidthAndHeight (Pair<Integer, Integer> values)
	{
		Width = values.getKey();
		Height = values.getValue();
	}

	public static void setWidthAndHeight (int width, int height)
	{
		Width = width;
		Height = height;
	}

	public static Pair<Integer, Integer> getWidthAndHeight () {
		return new Pair<>(Width, Height);
	}

	public boolean hasOBJModel()
	{
		return false;
	}

	@Override
	public void registerModels()
	{
		ArcaneArchives.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
		
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) 
	{
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		
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
		/*if (!worldIn.isRemote && hasTileEntity(getDefaultState()))
			NetworkHelper.getArcaneArchivesNetwork(tileEntityInstance.NetworkID).triggerUpdate();*/
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

package com.aranaira.arcanearchives.blocks;

import java.util.ArrayList;
import java.util.List;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.init.BlockLibrary;
import com.aranaira.arcanearchives.init.ItemLibrary;
import com.aranaira.arcanearchives.items.ItemBlockTemplate;
import com.aranaira.arcanearchives.tileentities.AccessorTileEntity;
import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantChestTileEntity;
import com.aranaira.arcanearchives.util.IHasModel;
import com.aranaira.arcanearchives.util.Placeable;
import com.aranaira.arcanearchives.util.Tuple;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockTemplate extends Block implements IHasModel {

	public ImmanenceTileEntity tileEntityInstance;
	public int PlaceLimit = -1;
	public String refName;

	private static Placeable.Size size;

	public BlockTemplate(String name, Material materialIn) {
		super(materialIn);
		setUnlocalizedName(name);
		setRegistryName(new ResourceLocation(ArcaneArchives.MODID, name));
		setCreativeTab(ArcaneArchives.TAB_AA);
		BlockLibrary.BLOCKS.add(this);
		ItemLibrary.ITEMS.add(new ItemBlockTemplate(this));

		setHarvestLevel("pickaxe", 0);
	}

	public static Placeable.Size getSize () {
		return size;
	}

	public static void setSize (int w, int h, int l) {
		size = new Placeable.Size(w, h, l);
	}

	public static void setSize (Placeable.Size newSize) {
		size = newSize;
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
		
		if (size.height > 1 && size.width == 3)
		{
			for (int x = -1; x < 2; x++)
				for (int z = -1; z < 2; z++)
					for (int y = 0; y < 5; y++)
					{
						if (z == 0 && x == 0 && y == 0)
							continue;

						createAccessorBlock(worldIn, pos.add(x, y, z), pos);
					}
		}
		else if (size.height > 1 && size.width == 1)
		{
			for (int y = 1; y < size.height + 1; y++)
			{
				createAccessorBlock(worldIn, pos.add(0, y, 0), pos);
			}
		}
		else if (size.height == 1 && size.width == 2)
		{
			createAccessorBlock(worldIn, pos.add(1, 0, 0), pos);
		}

		if (!worldIn.isRemote) {
			TileEntity te = worldIn.getTileEntity(pos);
			if (te instanceof ImmanenceTileEntity) {
				((ImmanenceTileEntity) te).setSize(getSize());
			}
		}
	}
	
	public void createAccessorBlock(World worldIn, BlockPos pos, BlockPos parentPos)
	{
		worldIn.setBlockState(pos, BlockLibrary.ACCESSOR.getDefaultState());
		
		((ImmanenceTileEntity) worldIn.getTileEntity(parentPos)).AddAccessor(pos);
		
		((AccessorTileEntity) worldIn.getTileEntity(pos)).ParentPos = parentPos;
	}

	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
		DestroyChildren(worldIn, pos);
		
		super.onBlockDestroyedByPlayer(worldIn, pos, state);
	}
	
	
	public void DestroyChildren(World worldIn, BlockPos pos)
	{
		/*if (!worldIn.isRemote && hasTileEntity(getDefaultState()))
			NetworkHelper.getArcaneArchivesNetwork(tileEntityInstance.NetworkID).triggerUpdate();*/
		for (BlockPos b : ((ImmanenceTileEntity)worldIn.getTileEntity(pos)).mAccessors)
		{
			worldIn.destroyBlock(b, false);
		}
	}
	
	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		
		super.onBlockAdded(worldIn, pos, state);
	}
}

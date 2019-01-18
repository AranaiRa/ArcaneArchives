package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.init.BlockLibrary;
import com.aranaira.arcanearchives.init.ItemLibrary;
import com.aranaira.arcanearchives.items.ItemBlockTemplate;
import com.aranaira.arcanearchives.util.IHasModel;
import com.aranaira.arcanearchives.util.Placeable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class BlockTemplate extends Block implements IHasModel {

	public int placeLimit;
	public Placeable.Size size;
	Class entityClass;

	public BlockTemplate(String name, Material materialIn) {
		super(materialIn);
		setUnlocalizedName(name);
		setRegistryName(new ResourceLocation(ArcaneArchives.MODID, name));
		setCreativeTab(ArcaneArchives.TAB_AA);
		BlockLibrary.BLOCKS.add(this);
		ItemLibrary.ITEMS.add(new ItemBlockTemplate(this));
		setHarvestLevel("pickaxe", 0);
	}

	public ITextComponent getNameComponent () {
		return new TextComponentTranslation(String.format("tile.%s.name", getUnlocalizedName()));
	}

	public void setEntityClass (Class clazz) {
		this.entityClass = clazz;
	}

	public Class getEntityClass () {
		return this.entityClass;
	}

	public int getPlaceLimit () {
		return placeLimit;
	}

	public void setPlaceLimit (int newPlaceLimit) {
		placeLimit = newPlaceLimit;
	}

	public Placeable.Size getSize () {
		return size;
	}

	public void setSize (int w, int h, int l) {
		size = new Placeable.Size(w, h, l);
	}

	public void setSize (Placeable.Size newSize) {
		size = newSize;
	}

	public boolean hasAccessors () {
		return size.hasAccessors();
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
		
		/*if (size.height > 1 && size.width == 3)
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
		else if (size.height > 1 && size.width == 1)
		{
			for (int y = 1; y < size.height + 1; y++)
			{
				AccessorBlock temp = new AccessorBlock(this.blockMaterial);
				temp.Parent = this;
				temp.setUnlocalizedName(getUnlocalizedName());
				worldIn.setBlockState(pos.add(0, y, 0), temp.getBlockState().getBaseState());
				
				Accessors.add(temp);
			}
		}
		else if (size.height == 1 && size.width == 2)
		{
			AccessorBlock temp = new AccessorBlock(this.blockMaterial);
			temp.Parent = this;
			temp.setUnlocalizedName(getUnlocalizedName());
			worldIn.setBlockState(pos.add(1, 0, 0), temp.getBlockState().getBaseState());
			Accessors.add(temp);
		}*/
	}

	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
		DestroyChildren(worldIn);
		
		super.onBlockDestroyedByPlayer(worldIn, pos, state);
	}
	
	public void RemoveChild(AccessorBlock b)
	{
	}
	
	public void DestroyChildren(World worldIn)
	{
		/*if (!worldIn.isRemote && hasTileEntity(getDefaultState()))
			NetworkHelper.getArcaneArchivesNetwork(tileEntityInstance.NetworkID).triggerUpdate();*/
		/*fr (AccessorBlock b : Accessors)
		{
			try 
			{
				worldIn.destroyBlock(b.pos, false);
			}
			catch(Exception e)
			{
				
			}
		}*/
	}
	
	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		super.onBlockAdded(worldIn, pos, state);
	}
}

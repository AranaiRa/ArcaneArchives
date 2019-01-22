package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.init.BlockLibrary;
import com.aranaira.arcanearchives.init.ItemLibrary;
import com.aranaira.arcanearchives.items.ItemBlockTemplate;
import com.aranaira.arcanearchives.tileentities.AATileEntity;
import com.aranaira.arcanearchives.util.IHasModel;
import com.aranaira.arcanearchives.util.Placeable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;

public class BlockTemplate extends Block implements IHasModel
{
	public static final PropertyEnum<EnumFacing> FACING = PropertyEnum.create("facing", EnumFacing.class);

	public int placeLimit = -1;
	public Placeable.Size size;
	Class entityClass;

	public BlockTemplate(String name, Material materialIn)
	{
		super(materialIn);
		setUnlocalizedName(name);
		setRegistryName(new ResourceLocation(ArcaneArchives.MODID, name));
		setCreativeTab(ArcaneArchives.TAB_AA);
		BlockLibrary.BLOCKS.add(this);
		ItemLibrary.ITEMS.add(new ItemBlockTemplate(this));
		setHarvestLevel("pickaxe", 0);
	}

	public ITextComponent getNameComponent()
	{
		return new TextComponentTranslation(String.format("%s.name", getUnlocalizedName()));
	}

	public Class getEntityClass()
	{
		return this.entityClass;
	}

	public void setEntityClass(Class clazz)
	{
		this.entityClass = clazz;
	}

	public int getPlaceLimit()
	{
		return placeLimit;
	}

	public void setPlaceLimit(int newPlaceLimit)
	{
		placeLimit = newPlaceLimit;
	}

	public Placeable.Size getSize()
	{
		return size;
	}

	public void setSize(Placeable.Size newSize)
	{
		size = newSize;
	}

	public void setSize(int w, int h, int l)
	{
		size = new Placeable.Size(w, h, l);
	}

	public boolean hasAccessors()
	{
		return size != null && size.hasAccessors();
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
	public int getMetaFromState(IBlockState state) {
		return 0;
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof AATileEntity) {
			return state.withProperty(FACING, ((AATileEntity) tile).getFacing());
		}

		return state;
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		if (hasTileEntity(getDefaultState()))
		{
			return new BlockStateContainer(this, FACING);
		}

		return new BlockStateContainer(this);
	}
}



package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.init.BlockLibrary;
import com.aranaira.arcanearchives.init.ItemLibrary;
import com.aranaira.arcanearchives.items.ItemBlockTemplate;
import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import com.aranaira.arcanearchives.util.IHasModel;
import com.aranaira.arcanearchives.util.Placeable;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BlockTemplate extends Block implements IHasModel
{
	private int placeLimit = -1;
	public Placeable.Size size;
	private Class entityClass;

	public BlockTemplate(String name, Material materialIn)
	{
		super(materialIn);
		setTranslationKey(name);
		setRegistryName(new ResourceLocation(ArcaneArchives.MODID, name));
		setCreativeTab(ArcaneArchives.TAB);
		BlockLibrary.BLOCKS.add(this);
		ItemLibrary.ITEMS.add(new ItemBlockTemplate(this));
		setHarvestLevel("pickaxe", 0);
	}

	public ITextComponent getNameComponent()
	{
		return new TextComponentTranslation(String.format("%s.name", getTranslationKey()));
	}

	public Class getEntityClass()
	{
		return this.entityClass;
	}

	void setEntityClass(Class clazz)
	{
		this.entityClass = clazz;
	}

	public int getPlaceLimit()
	{
		return placeLimit;
	}

	void setPlaceLimit(int newPlaceLimit)
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

	void setSize(int w, int h, int l)
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

	// TODO: Check to see if this is actually necessary
	@Override
	public void onBlockPlacedBy(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityLivingBase placer, @Nonnull ItemStack stack)
	{
		TileEntity te = worldIn.getTileEntity(pos);
		if(te instanceof ImmanenceTileEntity)
		{
			((ImmanenceTileEntity) te).SetNetworkID(placer.getUniqueID());
		}

		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		if(hasTileEntity(getDefaultState()))
		{
			return new ExtendedBlockState(this, new IProperty[]{}, new IUnlistedProperty[]{Properties.AnimationProperty});
		}

		return new ExtendedBlockState(this, new IProperty[]{}, new IUnlistedProperty[]{});
	}
}



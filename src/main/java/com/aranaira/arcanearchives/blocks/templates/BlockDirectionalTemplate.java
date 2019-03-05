package com.aranaira.arcanearchives.blocks.templates;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;

public class BlockDirectionalTemplate extends BlockTemplate
{
	public static final PropertyDirection FACING = PropertyDirection.create("facing");

	/**
	 * Creates a new directional block and adds it to the BlockRegistry, and if createItemBlock is true,
	 * a matching ItemBlock to the ItemRegistry. Functionally just calls {@link BlockTemplate#BlockTemplate(String, Material)}
	 *
	 * @param name       The name of the block, used for translation key and registry name
	 * @param materialIn The material of the block
	 */
	protected BlockDirectionalTemplate(String name, Material materialIn)
	{
		super(name, materialIn);
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
	{
		return getDefaultState().withProperty(FACING, EnumFacing.fromAngle(placer.rotationYaw - 90));
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(FACING).getIndex();
	}

	@Override
	@SuppressWarnings("deprecation")
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(FACING, EnumFacing.byIndex(meta & 7));
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		if(hasTileEntity(getDefaultState()))
		{
			return new ExtendedBlockState(this, new IProperty[]{FACING}, new IUnlistedProperty[]{Properties.AnimationProperty});
		}

		return new ExtendedBlockState(this, new IProperty[]{FACING}, new IUnlistedProperty[]{});
	}
}

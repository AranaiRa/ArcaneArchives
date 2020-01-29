package com.aranaira.arcanearchives.blocks.templates;

import com.aranaira.arcanearchives.tileentities.AATileEntity;
import com.aranaira.arcanearchives.util.WorldUtil;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

// TODO: Horizontal versus omnidirectional
public abstract class DirectionalBlock extends TemplateBlock {
	public static final PropertyDirection FACING = PropertyDirection.create("facing");

	public DirectionalBlock (Material materialIn) {
		super(materialIn);
	}

	public PropertyEnum<EnumFacing> getFacingProperty () {
		return FACING;
	}

	@Override
	@SuppressWarnings("deprecation")
	public IBlockState getStateFromMeta (int meta) {
		return getDefaultState().withProperty(getFacingProperty(), EnumFacing.byIndex(meta & 7));
	}

	@Override
	public int getMetaFromState (IBlockState state) {
		return state.getValue(getFacingProperty()).getIndex();
	}

	@Override
	public IBlockState getStateForPlacement (World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return getDefaultState().withProperty(getFacingProperty(), EnumFacing.fromAngle(placer.rotationYaw - 90));
	}

	@Override
	protected BlockStateContainer createBlockState () {
		return new BlockStateContainer(this, getFacingProperty());
	}

	@Override
	public boolean rotateBlock (World world, BlockPos pos, EnumFacing axis) {
		AATileEntity tile = WorldUtil.getTileEntity(AATileEntity.class, world, pos);
		boolean result = super.rotateBlock(world, pos, axis);
		if (tile != null) {
			tile.validate();
			world.setTileEntity(pos, tile);
		}
		return result;
	}


}

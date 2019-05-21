package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.blocks.templates.BlockDirectionalTemplate;
import com.aranaira.arcanearchives.blocks.templates.BlockTemplate;
import com.aranaira.arcanearchives.init.BlockRegistry;
import com.aranaira.arcanearchives.init.ItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class QuartzSliver extends BlockDirectionalTemplate {

	public static final String name = "quartz_sliver";

	public QuartzSliver() {
		super(name, Material.ROCK);
		setLightLevel(16 / 16f);
		setHardness(0.0f);
	}

	@Override
	@SuppressWarnings("deprecation")
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos)
	{
		//TODO: make sure changed block is the one that the sliver is attached to
		world.setBlockState(pos, Blocks.AIR.getDefaultState());
		this.dropBlockAsItem(world, pos, getDefaultState(), 0);
	}

	@Override
	public void getDrops (@Nonnull NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, @Nonnull IBlockState state, int fortune) {
		drops.add(new ItemStack(BlockRegistry.QUARTZ_SLIVER, 1));
	}

	@Override
	public void addInformation (ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		//TODO: add tooltip
	}

	@Override
	@SuppressWarnings("deprecation")
	public IBlockState withRotation (IBlockState state, Rotation rot) {
		return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
	}

	@Override
	@SuppressWarnings("deprecation")
	public IBlockState withMirror (IBlockState state, Mirror mirrorIn) {
		return state.withProperty(FACING, mirrorIn.mirror(state.getValue(FACING)));
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isFullCube (IBlockState state) {
		return false;
	}

	@Override
	@Nonnull
	@SuppressWarnings("deprecation")
	public AxisAlignedBB getBoundingBox (IBlockState state, IBlockAccess source, BlockPos pos) {
		EnumFacing facing = state.getValue(FACING);
		if (facing == EnumFacing.UP) {
			return new AxisAlignedBB(0.4375, 0.0, 0.4375, 0.5625, 0.375, 0.5625);
		} else if (facing == EnumFacing.DOWN) {
			return new AxisAlignedBB(0.4375, 0.625, 0.4375, 0.5625, 1.0, 0.5625);
		} else if (facing == EnumFacing.SOUTH) {
			return new AxisAlignedBB(0.4375, 0.4375, 0.0, 0.5625, 0.5625, 0.375);
		} else if (facing == EnumFacing.NORTH) {
			return new AxisAlignedBB(0.4375, 0.4375, 0.625, 0.5625, 0.5625, 1.0);
		} else if (facing == EnumFacing.EAST) {
			return new AxisAlignedBB(0.0, 0.4375, 0.4375, 0.375, 0.5625, 0.5625);
		} else {
			return new AxisAlignedBB(0.625, 0.4375, 0.4375, 1.0, 0.5625, 0.5625);
		}
	}

	@Override
	@SuppressWarnings("deprecation")
	public BlockFaceShape getBlockFaceShape (IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

	@Nullable
	@Override
	@SuppressWarnings("deprecation")
	public AxisAlignedBB getCollisionBoundingBox (IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return NULL_AABB;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isOpaqueCube (IBlockState state) {
		return false;
	}

	@Override
	public IBlockState getStateForPlacement (World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return this.getDefaultState().withProperty(FACING, facing);
	}

	@Override
	public int getMetaFromState (IBlockState state) {
		return state.getValue(FACING).getIndex();
	}

	@Override
	public IBlockState getStateFromMeta (int meta) {
		IBlockState iblockstate = this.getDefaultState();
		iblockstate = iblockstate.withProperty(FACING, EnumFacing.byIndex(meta));
		return iblockstate;
	}

	@Override
	protected BlockStateContainer createBlockState () {
		return new BlockStateContainer(this, FACING);
	}
}

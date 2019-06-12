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
		super(name, Material.GLASS);
		setLightLevel(16 / 16f);
		setHardness(0.0f);
	}

	private boolean canPlaceOn(World worldIn, BlockPos pos)
	{
		IBlockState state = worldIn.getBlockState(pos);
		return state.getBlock().canPlaceTorchOnTop(state, worldIn, pos);
	}

	protected boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state)
	{
		if (state.getBlock() == this && this.canPlaceAt(worldIn, pos, (EnumFacing)state.getValue(FACING)))
		{
			return true;
		}
		else
		{
			if (worldIn.getBlockState(pos).getBlock() == this)
			{
				this.dropBlockAsItem(worldIn, pos, state, 0);
				worldIn.setBlockToAir(pos);
			}

			return false;
		}
	}

	private boolean canPlaceAt(World worldIn, BlockPos pos, EnumFacing facing)
	{
		BlockPos blockpos = pos.offset(facing.getOpposite());
		IBlockState iblockstate = worldIn.getBlockState(blockpos);
		Block block = iblockstate.getBlock();
		BlockFaceShape blockfaceshape = iblockstate.getBlockFaceShape(worldIn, blockpos, facing);

		if (facing.equals(EnumFacing.UP) && this.canPlaceOn(worldIn, blockpos))
		{
			return true;
		}
		else if (facing != EnumFacing.UP && facing != EnumFacing.DOWN)
		{
			return !isExceptBlockForAttachWithPiston(block) && blockfaceshape == BlockFaceShape.SOLID;
		}
		else
		{
			return false;
		}
	}

	@Override
	@SuppressWarnings("deprecation")
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos)
	{
		this.onNeighborChangeInternal(world, pos, state);
		//TODO: make sure changed block is the one that the sliver is attached to
		//world.setBlockState(pos, Blocks.AIR.getDefaultState());
		//this.dropBlockAsItem(world, pos, getDefaultState(), 0);
	}

	/**
	 * Shamelessly stolen from BlockTorch
	 * @param worldIn
	 * @param pos
	 * @param state
	 * @return
	 */
	protected boolean onNeighborChangeInternal(World worldIn, BlockPos pos, IBlockState state)
	{
		if (!this.checkForDrop(worldIn, pos, state))
		{
			return true;
		}
		else
		{
			EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
			EnumFacing.Axis enumfacing$axis = enumfacing.getAxis();
			EnumFacing enumfacing1 = enumfacing.getOpposite();
			BlockPos blockpos = pos.offset(enumfacing1);
			boolean flag = false;

			if (enumfacing$axis.isHorizontal() && worldIn.getBlockState(blockpos).getBlockFaceShape(worldIn, blockpos, enumfacing) != BlockFaceShape.SOLID)
			{
				flag = true;
			}
			else if (enumfacing$axis.isVertical() && !this.canPlaceOn(worldIn, blockpos))
			{
				flag = true;
			}

			if (flag)
			{
				this.dropBlockAsItem(worldIn, pos, state, 0);
				worldIn.setBlockToAir(pos);
				return true;
			}
			else
			{
				return false;
			}
		}
	}

	@Override
	public void getDrops (@Nonnull NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, @Nonnull IBlockState state, int fortune) {
		drops.add(new ItemStack(BlockRegistry.QUARTZ_SLIVER, 1));
	}

	@Override
	public void addInformation (ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.item.quartz_sliver"));
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

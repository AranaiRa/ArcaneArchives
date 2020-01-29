package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.blocks.templates.DirectionalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class QuartzSliver extends DirectionalBlock implements IHasModel {
	public static final String name = "quartz_sliver";

	public QuartzSliver () {
		super(name, Material.GLASS);
		setLightLevel(16 / 16f);
		setHardness(0.0f);
		setTickRandomly(true);
		setDefaultState(this.getDefaultState().withProperty(getFacingProperty(), EnumFacing.DOWN));
	}

	@Override
	public boolean hasOBJModel () {
		return true;
	}

	@Override
	public boolean canPlaceTorchOnTop (IBlockState state, IBlockAccess world, BlockPos pos) {
		return false;
	}

	@Override
	public Item getItemDropped (IBlockState state, Random rand, int fortune) {
		return getItemBlock();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation (ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.item.quartz_sliver"));
	}

	@Override
	@SuppressWarnings("deprecation")
	public IBlockState withRotation (IBlockState state, Rotation rot) {
		return state.withProperty(getFacingProperty(), rot.rotate(state.getValue(getFacingProperty())));
	}

	@Override
	@SuppressWarnings("deprecation")
	public IBlockState withMirror (IBlockState state, Mirror mirrorIn) {
		return state.withProperty(getFacingProperty(), mirrorIn.mirror(state.getValue(getFacingProperty())));
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isFullCube (IBlockState state) {
		return false;
	}

	/*@Override
	public void onNeighborChange (IBlockAccess world, BlockPos pos, BlockPos neighbor) {
		IBlockState state = world.getBlockState(pos);
		EnumFacing facing = state.getValue(getFacingProperty()).getOpposite();
		if (pos.offset(facing).equals(neighbor) && (world.isAirBlock(neighbor))) {
			((World) world).setBlockToAir(pos);
			this.dropBlockAsItem((World) world, pos, state, 0);
		}
	}*/

	@Override
	@Nonnull
	@SuppressWarnings("deprecation")
	public AxisAlignedBB getBoundingBox (IBlockState state, IBlockAccess source, BlockPos pos) {
		EnumFacing facing = state.getValue(getFacingProperty());
		if (facing == EnumFacing.UP) {
			return new AxisAlignedBB(0.4, 0.0, 0.4, 0.6, 0.5, 0.6);
		} else if (facing == EnumFacing.DOWN) {
			return new AxisAlignedBB(0.4, 0.5, 0.4, 0.6, 1.0, 0.6);
		} else if (facing == EnumFacing.SOUTH) {
			return new AxisAlignedBB(0.4, 0.4, 0.0, 0.6, 0.6, 0.5);
		} else if (facing == EnumFacing.NORTH) {
			return new AxisAlignedBB(0.4, 0.4, 0.5, 0.6, 0.6, 1.0);
		} else if (facing == EnumFacing.EAST) {
			return new AxisAlignedBB(0.0, 0.4, 0.4, 0.5, 0.6, 0.6);
		} else {
			return new AxisAlignedBB(0.5, 0.4, 0.4, 1.0, 0.6, 0.6);
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
		return this.getDefaultState().withProperty(getFacingProperty(), facing);
	}

	@Override
	public int getMetaFromState (IBlockState state) {
		return state.getValue(getFacingProperty()).getIndex();
	}

	@Override
	public IBlockState getStateFromMeta (int meta) {
		return getDefaultState().withProperty(getFacingProperty(), EnumFacing.byIndex(meta));
	}

	@Override
	protected BlockStateContainer createBlockState () {
		return new BlockStateContainer(this, getFacingProperty());
	}
}

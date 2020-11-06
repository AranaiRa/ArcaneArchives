package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.blocks.templates.DirectionalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.BlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
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

public class RadiantLantern extends DirectionalBlock {

	public static final String NAME = "radiant_lantern";

	public RadiantLantern () {
		super(NAME, Material.GLASS);
		setLightLevel(16 / 16f);
		setHardness(0.3f);
		setHarvestLevel("pickaxe", 0);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation (ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.device.radiant_lantern"));
	}

	@Override
	@SuppressWarnings("deprecation")
	public BlockState withRotation (BlockState state, Rotation rot) {
		return state.withProperty(getFacingProperty(), rot.rotate(state.getValue(getFacingProperty())));
	}

	@Override
	@SuppressWarnings("deprecation")
	public BlockState withMirror (BlockState state, Mirror mirrorIn) {
		return state.withProperty(getFacingProperty(), mirrorIn.mirror(state.getValue(getFacingProperty())));
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isFullCube (BlockState state) {
		return false;
	}

	@Override
	@Nonnull
	@SuppressWarnings("deprecation")
	public AxisAlignedBB getBoundingBox (BlockState state, IBlockAccess source, BlockPos pos) {
		Direction facing = state.getValue(getFacingProperty());
		if (facing == Direction.UP) {
			return new AxisAlignedBB(0.35, 0.0, 0.35, 0.65, 1.0, 0.65);
		} else if (facing == Direction.DOWN) {
			return new AxisAlignedBB(0.35, 0.0, 0.35, 0.65, 1.0, 0.65);
		} else if (facing == Direction.SOUTH) {
			return new AxisAlignedBB(0.35, 0.35, 0.0, 0.65, 0.65, 1.0);
		} else if (facing == Direction.NORTH) {
			return new AxisAlignedBB(0.35, 0.35, 0.0, 0.65, 0.65, 1.0);
		} else if (facing == Direction.EAST) {
			return new AxisAlignedBB(0.0, 0.35, 0.35, 1.0, 0.65, 0.65);
		} else {
			return new AxisAlignedBB(0.0, 0.35, 0.35, 1.0, 0.65, 0.65);
		}
	}

	@Override
	@SuppressWarnings("deprecation")
	public BlockFaceShape getBlockFaceShape (IBlockAccess worldIn, BlockState state, BlockPos pos, Direction face) {
		return BlockFaceShape.UNDEFINED;
	}

	@Nullable
	@Override
	@SuppressWarnings("deprecation")
	public AxisAlignedBB getCollisionBoundingBox (BlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return NULL_AABB;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isOpaqueCube (BlockState state) {
		return false;
	}

	@Override
	public BlockState getStateForPlacement (World world, BlockPos pos, Direction facing, float hitX, float hitY, float hitZ, int meta, LivingEntity placer, Hand hand) {
		return this.getDefaultState().withProperty(getFacingProperty(), facing);
	}

	@Override
	public int getMetaFromState (BlockState state) {
		return state.getValue(getFacingProperty()).getIndex();
	}

	@Override
	public BlockState getStateFromMeta (int meta) {
		BlockState iblockstate = this.getDefaultState();
		iblockstate = iblockstate.withProperty(getFacingProperty(), Direction.byIndex(meta));
		return iblockstate;
	}

	@Override
	protected BlockStateContainer createBlockState () {
		return new BlockStateContainer(this, getFacingProperty());
	}

	@Override
	public boolean hasOBJModel () {
		return true;
	}
}

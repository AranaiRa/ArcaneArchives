package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.blocks.templates.BlockDirectionalTemplate;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class QuartzSliver extends BlockDirectionalTemplate implements IHasModel {
  public static final String name = "quartz_sliver";

  public QuartzSliver() {
    super(name, Material.GLASS);
    setLightLevel(16 / 16f);
    setHardness(0.0f);
    setTickRandomly(true);
    setDefaultState(this.getDefaultState().withProperty(FACING, Direction.DOWN));
  }

  @Override
  public boolean hasOBJModel() {
    return true;
  }

  @Override
  public boolean canPlaceTorchOnTop(BlockState state, IBlockAccess world, BlockPos pos) {
    return false;
  }

  @Override
  public Item getItemDropped(BlockState state, Random rand, int fortune) {
    return getItemBlock();
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
    tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.item.quartz_sliver"));
  }

  @Override
  @SuppressWarnings("deprecation")
  public BlockState withRotation(BlockState state, Rotation rot) {
    return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
  }

  @Override
  @SuppressWarnings("deprecation")
  public BlockState withMirror(BlockState state, Mirror mirrorIn) {
    return state.withProperty(FACING, mirrorIn.mirror(state.getValue(FACING)));
  }

  @Override
  @SuppressWarnings("deprecation")
  public boolean isFullCube(BlockState state) {
    return false;
  }

	/*@Override
	public void onNeighborChange (IBlockAccess world, BlockPos pos, BlockPos neighbor) {
		IBlockState state = world.getBlockState(pos);
		EnumFacing facing = state.getValue(FACING).getOpposite();
		if (pos.offset(facing).equals(neighbor) && (world.isAirBlock(neighbor))) {
			((World) world).setBlockToAir(pos);
			this.dropBlockAsItem((World) world, pos, state, 0);
		}
	}*/

  @Override
  @Nonnull
  @SuppressWarnings("deprecation")
  public AxisAlignedBB getBoundingBox(BlockState state, IBlockAccess source, BlockPos pos) {
    Direction facing = state.getValue(FACING);
    if (facing == Direction.UP) {
      return new AxisAlignedBB(0.4, 0.0, 0.4, 0.6, 0.5, 0.6);
    } else if (facing == Direction.DOWN) {
      return new AxisAlignedBB(0.4, 0.5, 0.4, 0.6, 1.0, 0.6);
    } else if (facing == Direction.SOUTH) {
      return new AxisAlignedBB(0.4, 0.4, 0.0, 0.6, 0.6, 0.5);
    } else if (facing == Direction.NORTH) {
      return new AxisAlignedBB(0.4, 0.4, 0.5, 0.6, 0.6, 1.0);
    } else if (facing == Direction.EAST) {
      return new AxisAlignedBB(0.0, 0.4, 0.4, 0.5, 0.6, 0.6);
    } else {
      return new AxisAlignedBB(0.5, 0.4, 0.4, 1.0, 0.6, 0.6);
    }
  }

  @Override
  @SuppressWarnings("deprecation")
  public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, BlockState state, BlockPos pos, Direction face) {
    return BlockFaceShape.UNDEFINED;
  }

  @Nullable
  @Override
  @SuppressWarnings("deprecation")
  public AxisAlignedBB getCollisionBoundingBox(BlockState blockState, IBlockAccess worldIn, BlockPos pos) {
    return NULL_AABB;
  }

  @Override
  @SuppressWarnings("deprecation")
  public boolean isOpaqueCube(BlockState state) {
    return false;
  }

  @Override
  public BlockState getStateForPlacement(World world, BlockPos pos, Direction facing, float hitX, float hitY, float hitZ, int meta, LivingEntity placer, Hand hand) {
    return this.getDefaultState().withProperty(FACING, facing);
  }

  @Override
  public int getMetaFromState(BlockState state) {
    return state.getValue(FACING).getIndex();
  }

  @Override
  public BlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty(FACING, Direction.byIndex(meta));
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, FACING);
  }
}

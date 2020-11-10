/*package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.blocks.templates.OmniTemplateBlock;
import com.aranaira.arcanearchives.tileentities.MonitoringCrystalTile;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings("deprecation")
public class MonitoringCrystalBlock extends OmniTemplateBlock {
  public MonitoringCrystalBlock(Material material) {
    super(material);
  }

  @Override
  public boolean isFullCube(BlockState state) {
    return false;
  }

  @Override
  @Nonnull
  public AxisAlignedBB getBoundingBox(BlockState state, IBlockAccess source, BlockPos pos) {
    Direction facing = state.get(getFacingProperty());
    if (facing == Direction.UP) {
      return new AxisAlignedBB(0.37, -0.1, 0.35, 0.61, 0.04, 0.64);
    } else if (facing == Direction.DOWN) {
      return new AxisAlignedBB(0.37, 1.1, 0.35, 0.61, 0.94, 0.64);
    } else if (facing == Direction.SOUTH) {
      return new AxisAlignedBB(0.61, 0.64, -0.1, 0.35, 0.35, 0.04);
    } else if (facing == Direction.NORTH) {
      return new AxisAlignedBB(0.63, 0.64, 1.1, 0.39, 0.35, 0.94);
    } else if (facing == Direction.EAST) {
      return new AxisAlignedBB(0.05, 0.63, 0.39, -0.1, 0.35, 0.63);
    } else {
      return new AxisAlignedBB(0.95, 0.63, 0.37, 1.1, 0.35, 0.62);
    }
  }

  @Override
  public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, BlockState state, BlockPos pos, Direction face) {
    return BlockFaceShape.UNDEFINED;
  }

  @Nullable
  @Override
  public AxisAlignedBB getCollisionBoundingBox(BlockState blockState, IBlockAccess worldIn, BlockPos pos) {
    return NULL_AABB;
  }

  @Override
  public boolean isOpaqueCube(BlockState state) {
    return false;
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(World world, BlockState state) {
    return new MonitoringCrystalTile();
  }

  @Override
  public BlockState getStateForPlacement(World world, BlockPos pos, Direction facing, float hitX, float hitY, float hitZ, int meta, LivingEntity placer, Hand hand) {
    return this.getDefaultState().with(getFacingProperty(), facing);
  }
}*/

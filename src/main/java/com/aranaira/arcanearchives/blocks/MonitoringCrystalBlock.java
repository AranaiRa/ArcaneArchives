package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.blocks.templates.OmniTemplateBlock;
import com.aranaira.arcanearchives.tileentities.MonitoringCrystalTile;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
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
  public boolean isFullCube(IBlockState state) {
    return false;
  }

  @Override
  @Nonnull
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    EnumFacing facing = state.getValue(getFacingProperty());
    if (facing == EnumFacing.UP) {
      return new AxisAlignedBB(0.37, -0.1, 0.35, 0.61, 0.04, 0.64);
    } else if (facing == EnumFacing.DOWN) {
      return new AxisAlignedBB(0.37, 1.1, 0.35, 0.61, 0.94, 0.64);
    } else if (facing == EnumFacing.SOUTH) {
      return new AxisAlignedBB(0.61, 0.64, -0.1, 0.35, 0.35, 0.04);
    } else if (facing == EnumFacing.NORTH) {
      return new AxisAlignedBB(0.63, 0.64, 1.1, 0.39, 0.35, 0.94);
    } else if (facing == EnumFacing.EAST) {
      return new AxisAlignedBB(0.05, 0.63, 0.39, -0.1, 0.35, 0.63);
    } else {
      return new AxisAlignedBB(0.95, 0.63, 0.37, 1.1, 0.35, 0.62);
    }
  }

  @Override
  public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
    return BlockFaceShape.UNDEFINED;
  }

  @Nullable
  @Override
  public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
    return NULL_AABB;
  }

  @Override
  public boolean isOpaqueCube(IBlockState state) {
    return false;
  }

  @Override
  public boolean hasTileEntity(IBlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(World world, IBlockState state) {
    return new MonitoringCrystalTile();
  }

  @Override
  public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
    return this.getDefaultState().withProperty(getFacingProperty(), facing);
  }
}

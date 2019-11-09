package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.blocks.templates.BlockDirectionalTemplate;
import com.aranaira.arcanearchives.init.ItemRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
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
import net.minecraftforge.fml.common.Optional;
import thaumcraft.api.crafting.IInfusionStabiliserExt;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@SuppressWarnings("deprecation")
@Optional.Interface(modid = "thaumcraft", iface = "thaumcraft.api.crafting.IInfusionStabiliserExt")
public class RawQuartzCluster extends BlockDirectionalTemplate implements IInfusionStabiliserExt {

  public static final String name = "raw_quartz_cluster";

  public RawQuartzCluster() {
    super(name, Material.ROCK);
    setLightLevel(16 / 16f);
    setHardness(1.4f);
    setDefaultState(this.blockState.getBaseState().withProperty(FACING, Direction.UP));
    setHarvestLevel("pickaxe", 0);
  }

  @Override
  public boolean canSilkHarvest() {
    return true;
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
  public BlockState getStateFromMeta(int meta) {
    BlockState iblockstate = this.getDefaultState();
    iblockstate = iblockstate.withProperty(FACING, Direction.byIndex(meta));
    return iblockstate;
  }

  @Override
  public int getMetaFromState(BlockState state) {
    return state.getValue(FACING).getIndex();
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
    tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.item.raw_quartz"));
  }

  @Override
  @SuppressWarnings("deprecation")
  public boolean isFullCube(BlockState state) {
    return false;
  }

  @Override
  @Nonnull
  @SuppressWarnings("deprecation")
  public AxisAlignedBB getBoundingBox(BlockState state, IBlockAccess source, BlockPos pos) {
    return new AxisAlignedBB(0.2, 0.0, 0.2, 0.8, 1.0, 0.8);
  }

  @Override
  @SuppressWarnings("deprecation")
  public boolean isOpaqueCube(BlockState state) {
    return false;
  }

  @Override
  public void getDrops(@Nonnull NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, @Nonnull BlockState state, int fortune) {
    drops.add(new ItemStack(ItemRegistry.RAW_RADIANT_QUARTZ, 1));
  }

  @Override
  public boolean hasOBJModel() {
    return true;
  }

  @Override
  public float getStabilizationAmount(World world, BlockPos blockPos) {
    return 0.15f;
  }

  @Override
  public boolean canStabaliseInfusion(World world, BlockPos blockPos) {
    return true;
  }

  @Override
  public BlockState getStateForPlacement(World world, BlockPos pos, Direction facing, float hitX, float hitY, float hitZ, int meta, LivingEntity placer, Hand hand) {
    return getDefaultState().withProperty(FACING, facing);
  }
}

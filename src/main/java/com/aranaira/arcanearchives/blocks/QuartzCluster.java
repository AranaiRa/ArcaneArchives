package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.blocks.templates.HorizontalTemplateBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@SuppressWarnings("deprecation")
//@Optional.Interface(modid = "thaumcraft", iface = "thaumcraft.api.crafting.IInfusionStabiliserExt")
public class QuartzCluster extends HorizontalTemplateBlock /*implements IInfusionStabiliserExt*/ {

	public static final String name = "raw_quartz_cluster";

	public QuartzCluster () {
		super(Material.ROCK);
		setLightLevel(16 / 16f);
		setHardness(1.4f);
		setDefaultState(this.blockState.getBaseState().withProperty(getFacingProperty(), EnumFacing.UP));
		setHarvestLevel("pickaxe", 0);
	}

	@Override
	public boolean canSilkHarvest () {
		return true;
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
	public IBlockState getStateFromMeta (int meta) {
		IBlockState iblockstate = this.getDefaultState();
		iblockstate = iblockstate.withProperty(getFacingProperty(), EnumFacing.byIndex(meta));
		return iblockstate;
	}

	@Override
	public int getMetaFromState (IBlockState state) {
		return state.getValue(getFacingProperty()).getIndex();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation (ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.item.raw_quartz"));
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
		return new AxisAlignedBB(0.2, 0.0, 0.2, 0.8, 1.0, 0.8);
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isOpaqueCube (IBlockState state) {
		return false;
	}

	@Override
	public void getDrops (@Nonnull NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, @Nonnull IBlockState state, int fortune) {
		// TODO: Embed network ID
		//drops.add(new ItemStack(ItemRegistry.RAW_RADIANT_QUARTZ, 1));
	}

/*	@Override
	public float getStabilizationAmount (World world, BlockPos blockPos) {
		return 0.15f;
	}

	@Override
	public boolean canStabaliseInfusion (World world, BlockPos blockPos) {
		return true;
	}*/

	@Override
	public IBlockState getStateForPlacement (World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return getDefaultState().withProperty(getFacingProperty(), facing);
	}
}

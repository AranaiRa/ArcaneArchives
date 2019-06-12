package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.blocks.templates.BlockDirectionalTemplate;
import com.aranaira.arcanearchives.blocks.templates.BlockTemplate;
import com.aranaira.arcanearchives.init.ItemRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@SuppressWarnings("deprecation")
public class RawQuartzCluster extends BlockDirectionalTemplate {

	//public static final PropertyDirection DIRECTION = PropertyDirection.create("facing");
	public static final String name = "raw_quartz_cluster";

	public RawQuartzCluster() {
		super(name, Material.ROCK);
		setLightLevel(16 / 16f);
		setHardness(1.4f);
		//setDefaultState(this.blockState.getBaseState().withProperty(DIRECTION,  EnumFacing.NORTH));
		setHarvestLevel("pickaxe", 0);
	}

	@Override
	public boolean canSilkHarvest(){
		return true;
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
	public IBlockState getStateFromMeta (int meta) {
		IBlockState iblockstate = this.getDefaultState();
		iblockstate = iblockstate.withProperty(FACING, EnumFacing.byIndex(meta));
		return iblockstate;
	}

	@Override
	public int getMetaFromState (IBlockState state) {
		return state.getValue(FACING).getIndex();
	}

	@Override
	public void addInformation (ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.item.rawquartz"));
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
		drops.add(new ItemStack(ItemRegistry.RAW_RADIANT_QUARTZ, 1));
	}

	@Override
	public boolean hasOBJModel () {
		return true;
	}
}

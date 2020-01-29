package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.blocks.templates.DirectionalBlock;
import com.aranaira.arcanearchives.tileentities.MonitoringCrystalTileEntity;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
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

public class MonitoringCrystal extends DirectionalBlock {

	public static final String NAME = "monitoring_crystal";

	public MonitoringCrystal () {
		super(NAME, Material.GLASS);
		setLightLevel(16 / 16f);
		setHardness(0.8f);
		setHarvestLevel("pickaxe", 0);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation (ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.device.monitoring_crystal"));
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

	@Override
	@Nonnull
	@SuppressWarnings("deprecation")
	public AxisAlignedBB getBoundingBox (IBlockState state, IBlockAccess source, BlockPos pos) {
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
	public boolean onBlockActivated (World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		//ArcaneArchives.logger.info(state.getValue(BlockDirectionalTemplate.getFacingProperty()).getName().toLowerCase());

		return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
	}

	@Override
	public boolean hasTileEntity (IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity (World world, IBlockState state) {
		return new MonitoringCrystalTileEntity();
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
	public IBlockState getStateForPlacement (World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return this.getDefaultState().withProperty(getFacingProperty(), facing);
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

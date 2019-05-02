package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.blocks.templates.BlockTemplate;
import com.aranaira.arcanearchives.config.ConfigHandler;
import com.aranaira.arcanearchives.tileentities.RadiantResonatorTileEntity;
import com.aranaira.arcanearchives.util.WorldUtil;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Random;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class RadiantResonator extends BlockTemplate
{
	public static final String name = "radiant_resonator";

	public RadiantResonator() {
		super(name, Material.IRON);
		setPlaceLimit(ConfigHandler.ResonatorLimit);
		setHardness(1.1f);
		setHarvestLevel("axe", 0);
		setEntityClass(RadiantResonatorTileEntity.class);
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.device.radiantresonator"));
	}

	@Override
	public boolean hasOBJModel() {
		return true;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean causesSuffocation(IBlockState state) {
		return false;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		super.updateTick(worldIn, pos, state, rand);
	}

	@Override
	public void onPlayerDestroy(World worldIn, BlockPos pos, IBlockState state) {
		if(worldIn.getBlockState(pos.up()).getBlock() instanceof RawQuartz) {
			worldIn.destroyBlock(pos.up(), true);
		}
		super.onPlayerDestroy(worldIn, pos, state);
	}

	@Override
	public void onExplosionDestroy(World worldIn, BlockPos pos, Explosion explosion) {
		if(worldIn.getBlockState(pos.up()).getBlock() instanceof RawQuartz) {
			worldIn.destroyBlock(pos.up(), false);
		}
		super.onExplosionDestroy(worldIn, pos, explosion);
	}

	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}

	@Override
	@SuppressWarnings("deprecation")
	public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
		RadiantResonatorTileEntity te = WorldUtil.getTileEntity(RadiantResonatorTileEntity.class, worldIn, pos);
		if(te == null) return 0;

		if(te.canTick() == RadiantResonatorTileEntity.TickResult.HARVEST_WAITING) {
			return 15;
		}

		return Math.min((int) Math.floor(te.getPercentageComplete() / 6.6d), 14);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new RadiantResonatorTileEntity();
	}
}

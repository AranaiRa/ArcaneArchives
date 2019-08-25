package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.blocks.templates.BlockTemplate;
import com.aranaira.arcanearchives.config.ServerSideConfig;
import com.aranaira.arcanearchives.tileentities.RadiantResonatorTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantResonatorTileEntity.TickResult;
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
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Random;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class RadiantResonator extends BlockTemplate {
	public static final String name = "radiant_resonator";

	public RadiantResonator () {
		super(name, Material.IRON);
		setPlaceLimit(ServerSideConfig.ResonatorLimit);
		setHardness(3f);
		setHarvestLevel("pickaxe", 0);
		setEntityClass(RadiantResonatorTileEntity.class);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation (ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.device.radiant_resonator"));
	}

	@Override
	public boolean hasOBJModel () {
		return true;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean causesSuffocation (IBlockState state) {
		return false;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isFullCube (IBlockState state) {
		return false;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isOpaqueCube (IBlockState state) {
		return false;
	}

	@Override
	public void updateTick (World worldIn, BlockPos pos, IBlockState state, Random rand) {
		super.updateTick(worldIn, pos, state, rand);
	}

	@Override
	public BlockRenderLayer getRenderLayer () {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean hasComparatorInputOverride (IBlockState state) {
		return true;
	}

	@Override
	@SuppressWarnings("deprecation")
	public int getComparatorInputOverride (IBlockState blockState, World worldIn, BlockPos pos) {
		RadiantResonatorTileEntity te = WorldUtil.getTileEntity(RadiantResonatorTileEntity.class, worldIn, pos);
		if (te == null) {
			return 0;
		}

		TickResult tr = te.canTick();

		if (tr == TickResult.OFFLINE || tr == TickResult.OBSTRUCTION) {
			return 0;
		}

		if (te.canTick() == RadiantResonatorTileEntity.TickResult.HARVEST_WAITING) {
			return 15;
		}

		return Math.max(1, Math.min((int) Math.floor(te.getPercentageComplete() / 7.14) + 1, 14));
	}

	@Override
	public boolean hasTileEntity (IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity (World world, IBlockState state) {
		return new RadiantResonatorTileEntity();
	}

	@Override
	public void breakBlock (World world, BlockPos pos, IBlockState state) {
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof RadiantResonatorTileEntity) {
			((RadiantResonatorTileEntity) te).breakBlock(state, true);
		}

		super.breakBlock(world, pos, state);
	}
}

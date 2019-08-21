package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.blocks.templates.BlockDirectionalTemplate;
import com.aranaira.arcanearchives.client.render.LineHandler;
import com.aranaira.arcanearchives.tileentities.RadiantFurnaceTileEntity;
import com.aranaira.arcanearchives.util.DropUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.List;

public class RadiantFurnace extends BlockDirectionalTemplate {
	public static final AxisAlignedBB BB_MAIN = new AxisAlignedBB(0, 0, 0, 1, 1, 1);
	public static final AxisAlignedBB BB_ACCESSOR = new AxisAlignedBB(0, 0, 0, 1, 1.5, 1);

	public static final String name = "radiant_furnace";

	public RadiantFurnace () {
		super(name, Material.ROCK);
		setLightLevel(16 / 16f);
		setHardness(3f);
		setSize(2, 1, 1);
		setHarvestLevel("pickaxe", 0);
		this.setDefaultState(this.getDefaultState().withProperty(ACCESSOR, false));
	}

	@Override
	@SuppressWarnings("deprecation")
	public AxisAlignedBB getBoundingBox (IBlockState state, IBlockAccess source, BlockPos pos) {
		boolean accessor = state.getValue(ACCESSOR);
		if (accessor) {
			return BB_ACCESSOR;
		}

		return BB_MAIN;
	}

	@Override
	@SuppressWarnings("deprecation")
	public IBlockState getStateFromMeta (int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.byIndex(meta >> 1)).withProperty(ACCESSOR, (meta & 1) != 0);
	}

	@Override
	public int getMetaFromState (IBlockState state) {
		return state.getValue(FACING).getIndex() << 1 ^ (state.getValue(ACCESSOR) ? 1 : 0);
	}

	@Override
	public boolean hasOBJModel () {
		return true;
	}

	@Override
	public void breakBlock (World world, BlockPos pos, IBlockState state) {
		if (state.getValue(ACCESSOR)) {
			return;
		}

		LineHandler.removeLine(pos, world.provider.getDimension());

		TileEntity te = world.getTileEntity(pos);
		if (te instanceof RadiantFurnaceTileEntity) {
			IItemHandler inv = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			DropUtils.dropInventoryItems(world, pos, inv);
		}

		super.breakBlock(world, pos, state);
	}

	public BlockPos getConnectedPos (BlockPos pos, IBlockState state) {
		return pos.offset(state.getValue(FACING));
	}

	@Override
	@SuppressWarnings("deprecation")
	public void neighborChanged (IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
		if (state.getValue(ACCESSOR)) {
			if (world.isAirBlock(getConnectedPos(pos, state))) {
				world.setBlockToAir(pos);
			}
		} else {
			BlockPos thingy = pos.offset(EnumFacing.fromAngle(state.getValue(FACING).getHorizontalAngle() - 180));
			if (world.isAirBlock(thingy)) {
				// TODO: PARTICLES
				this.breakBlock(world, pos, state);
				world.setBlockToAir(pos);
			}
		}

		super.neighborChanged(state, world, pos, blockIn, fromPos);
	}

	@Override
	public boolean onBlockActivated (World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (state.getValue(ACCESSOR)) {
			pos = getConnectedPos(pos, state);
		}

		LineHandler.removeLine(pos, playerIn.dimension);

		if (worldIn.isRemote) {
			return true;
		}

		//playerIn.openGui(ArcaneArchives.instance, AAGuiHandler.GEMCUTTERS_TABLE, worldIn, pos.getX(), pos.getY(), pos.getZ());

		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation (ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.RED + "" + TextFormatting.BOLD + I18n.format("arcanearchives.tooltip.notimplemented1"));
		tooltip.add(TextFormatting.RED + "" + TextFormatting.ITALIC + I18n.format("arcanearchives.tooltip.notimplemented2"));
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
	public BlockRenderLayer getRenderLayer () {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	protected BlockStateContainer createBlockState () {
		return new BlockStateContainer(this, FACING, ACCESSOR);
	}
}

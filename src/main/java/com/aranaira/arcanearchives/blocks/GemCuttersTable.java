package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.AAGuiHandler;
import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.blocks.templates.BlockDirectionalTemplate;
import com.aranaira.arcanearchives.events.LineHandler;
import com.aranaira.arcanearchives.tileentities.GemCuttersTableTileEntity;
import com.aranaira.arcanearchives.util.DropHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.List;

public class GemCuttersTable extends BlockDirectionalTemplate {
	public static final String name = "gemcutters_table";

	public GemCuttersTable () {
		super(name, Material.IRON);
		this.setHardness(2.5f);
		setSize(2, 1, 1);
		setLightLevel(16f / 16f);
		this.setDefaultState(this.getDefaultState().withProperty(ACCESSOR, false));
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
	@SideOnly(Side.CLIENT)
	public void addInformation (ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.device.gemcutters_table"));
	}

	@Override
	public boolean hasOBJModel () {
		return true;
	}

	@Override
	public void breakBlock (World world, BlockPos pos, IBlockState state) {
		if (state.getValue(ACCESSOR)) return;

		LineHandler.removeLine(pos, world.provider.getDimension());

		TileEntity te = world.getTileEntity(pos);
		if (te instanceof GemCuttersTableTileEntity) {
			IItemHandler inv = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			DropHelper.dropInventoryItems(world, pos, inv);
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
	public BlockRenderLayer getRenderLayer () {
		return BlockRenderLayer.CUTOUT;
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

		playerIn.openGui(ArcaneArchives.instance, AAGuiHandler.GEMCUTTERS_TABLE, worldIn, pos.getX(), pos.getY(), pos.getZ());

		return true;
	}

	@Override
	public boolean hasTileEntity (IBlockState state) {
		return !state.getValue(ACCESSOR);
	}

	@Override
	public TileEntity createTileEntity (World world, IBlockState state) {
		if (state.getValue(ACCESSOR)) return null;

		return new GemCuttersTableTileEntity();
	}

	@Override
	protected BlockStateContainer createBlockState () {
		return new BlockStateContainer(this, FACING, ACCESSOR);
	}
}

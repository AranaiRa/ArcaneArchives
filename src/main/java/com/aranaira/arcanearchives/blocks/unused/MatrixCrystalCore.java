package com.aranaira.arcanearchives.blocks.unused;

import com.aranaira.arcanearchives.blocks.templates.BlockDirectionalTemplate;
import com.aranaira.arcanearchives.tileentities.unused.MatrixCoreTileEntity;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
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

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Random;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MatrixCrystalCore extends BlockDirectionalTemplate
{
	public static final String name = "matrix_crystal_core";

	public MatrixCrystalCore() {
		super(name, Material.ROCK);
		setLightLevel(16 / 16f);
		setPlaceLimit(1);
		setSize(3, 4, 3);
		setEntityClass(MatrixCoreTileEntity.class);
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
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		return false;

		/*TileEntity te = world.getTileEntity(pos);
		if(te instanceof ImmanenceTileEntity)
{
			if(!((ImmanenceTileEntity) te).networkId.equals(player.getUniqueID())) return false;
			player.openGui(ArcaneArchives.instance, AAGuiHandler.TOME_OF_REQUISITION, world, pos.getX(), pos.getY(), pos.getZ());
}
		return true;*/
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.RED + "" + TextFormatting.BOLD + I18n.format("arcanearchives.tooltip.notimplemented1"));
		tooltip.add(TextFormatting.RED + "" + TextFormatting.ITALIC + I18n.format("arcanearchives.tooltip.notimplemented2"));
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new MatrixCoreTileEntity();
	}
}

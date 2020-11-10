/*package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.AAGuiHandler;
import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.blocks.templates.DirectionalBlock;
import com.aranaira.arcanearchives.tileentities.MatrixCoreTileEntity;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;



public class MatrixCrystalCore extends DirectionalBlock {
	public static final String name = "matrix_crystal_core";

	public MatrixCrystalCore () {
		super(name, Material.ROCK);
		setLightLevel(16 / 16f);
		setPlaceLimit(1);
		setSize(3, 4, 3);
		setEntityClass(MatrixCoreTileEntity.class);
	}

	@Override
	public boolean hasOBJModel () {
		return true;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean causesSuffocation (BlockState state) {
		return false;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isFullCube (BlockState state) {
		return false;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isOpaqueCube (BlockState state) {
		return false;
	}

	@Override
	public void updateTick (World worldIn, BlockPos pos, BlockState state, Random rand) {
		super.updateTick(worldIn, pos, state, rand);
	}

	@Override
	public BlockRenderLayer getRenderLayer () {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public boolean onBlockActivated (World world, BlockPos pos, BlockState state, PlayerEntity player, Hand hand, Direction side, float hitX, float hitY, float hitZ) {
		return false;

		TileEntity te = world.getTileEntity(pos);
		if(te instanceof ImmanenceTileEntity)
{
			if(!((ImmanenceTileEntity) te).networkId.equals(player.getUniqueID())) return false;
			player.openGui(ArcaneArchives.instance, AAGuiHandler.TOME_OF_REQUISITION, world, pos.getX(), pos.getY(), pos.getZ());
}
		return true;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation (ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.RED + "" + TextFormatting.BOLD + I18n.format("arcanearchives.tooltip.notimplemented1"));
		tooltip.add(TextFormatting.RED + "" + TextFormatting.ITALIC + I18n.format("arcanearchives.tooltip.notimplemented2"));
	}

	@Override
	public boolean hasTileEntity (BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity (World world, BlockState state) {
		return new MatrixCoreTileEntity();
	}
}*/

package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.blocks.templates.BlockTemplate;
import com.aranaira.arcanearchives.tileentities.BrazierTileEntity;
import com.aranaira.arcanearchives.util.WorldUtil;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
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
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.crafting.IInfusionStabiliserExt;

import javax.annotation.Nonnull;
import java.util.List;

@Optional.Interface(modid = "thaumcraft", iface = "thaumcraft.api.crafting.IInfusionStabiliserExt")
public class Brazier extends BlockTemplate implements IInfusionStabiliserExt {

	public static final String name = "brazier_of_hoarding";

	public Brazier () {
		super(name, Material.IRON);
		setLightLevel(16 / 16f);
		setHardness(3f);
		setHarvestLevel("pickaxe", 0);
	}

	@Override
	public boolean hasOBJModel () {
		return true;
	}

	@Override
	@Nonnull
	@SuppressWarnings("deprecation")
	public AxisAlignedBB getBoundingBox (IBlockState state, IBlockAccess source, BlockPos pos) {
		return new AxisAlignedBB(0.15, 0.0, 0.15, 0.85, 0.75, 0.85);
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isOpaqueCube (IBlockState state) {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation (ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.device.brazier"));
	}

	@Override
	public BlockRenderLayer getRenderLayer () {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public boolean onBlockActivated (World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (hand == EnumHand.MAIN_HAND && !worldIn.isRemote) {
			BrazierTileEntity te = WorldUtil.getTileEntity(BrazierTileEntity.class, playerIn.dimension, pos);
			if (te != null) {
				te.beginInsert(playerIn, hand, facing, false);
			}
		}

		return true;
	}

	@Override
	public void onEntityCollision (World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		if (entityIn instanceof EntityItem) {
			BrazierTileEntity te = WorldUtil.getTileEntity(BrazierTileEntity.class, worldIn, pos);
			if (te != null) {
				te.beginInsert((EntityItem) entityIn);
			}
		}
	}

	@Override
	public boolean hasTileEntity (IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity (World world, IBlockState state) {
		return new BrazierTileEntity();
	}

	@Override
	public boolean canStabaliseInfusion (World world, BlockPos blockPos) {
		return true;
	}

	@Override
	public float getStabilizationAmount (World world, BlockPos blockPos) {
		return 0.2f;
	}

	@Override
	public boolean causesSuffocation (IBlockState state) {
		return false;
	}
}

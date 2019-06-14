package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.blocks.templates.BlockTemplate;
import com.aranaira.arcanearchives.init.BlockRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

@SuppressWarnings("deprecation")
public class LecternAccessor extends BlockTemplate {
	public LecternAccessor () {
		super("lecternaccessor", Material.GLASS);
		setTranslationKey("lecternaccessor");
		setLightLevel(16f / 16f);
	}

	@Override
	// Called before the tile entity itself is removed
	public void breakBlock (World world, BlockPos pos, IBlockState state) {
		super.breakBlock(world, pos, state);
		world.destroyBlock(pos.down(), true);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getRenderLayer () {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public float getBlockHardness (IBlockState blockState, World world, BlockPos pos) {
		return BlockRegistry.LECTERN_MANIFEST.getBlockHardness(world.getBlockState(pos.down()), world, pos.down());
	}

	@Override
	public Item getItemDropped (IBlockState state, Random rand, int fortune) {
		return Items.AIR;
	}

	@Override
	public float getPlayerRelativeBlockHardness (IBlockState state, EntityPlayer player, World world, BlockPos pos) {
		return BlockRegistry.LECTERN_MANIFEST.getPlayerRelativeBlockHardness(world.getBlockState(pos.down()), player, world, pos.down());
	}

	@Override
	public boolean onBlockActivated (World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		return BlockRegistry.LECTERN_MANIFEST.onBlockActivated(world, pos.down(), world.getBlockState(pos.down()), playerIn, hand, facing, hitX, hitY, hitZ);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation (ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.RED + "" + TextFormatting.ITALIC + I18n.format("arcanearchives.tooltip.shouldnothave"));
	}

	@Override
	public boolean isSideSolid (IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return false;
	}

	@Override
	public float getExplosionResistance (World world, BlockPos pos, @Nullable Entity exploder, Explosion explosion) {
		return BlockRegistry.LECTERN_MANIFEST.getExplosionResistance(world, pos, exploder, explosion);
	}

	@Override
	public ItemStack getPickBlock (IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return BlockRegistry.LECTERN_MANIFEST.getPickBlock(world.getBlockState(pos.down()), target, world, pos.down(), player);
	}

	@Nullable
	@Override
	public String getHarvestTool (IBlockState state) {
		return BlockRegistry.LECTERN_MANIFEST.getHarvestTool(BlockRegistry.LECTERN_MANIFEST.getDefaultState());
	}

	@Override
	public int getHarvestLevel (IBlockState state) {
		return BlockRegistry.LECTERN_MANIFEST.getHarvestLevel(BlockRegistry.LECTERN_MANIFEST.getDefaultState());
	}
}

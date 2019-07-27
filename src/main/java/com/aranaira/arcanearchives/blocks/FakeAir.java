package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.blocks.templates.BlockTemplate;
import com.aranaira.arcanearchives.tileentities.FakeAirTileEntity;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

@SuppressWarnings("deprecation")
public class FakeAir extends BlockTemplate {
	protected static final AxisAlignedBB AABB = new AxisAlignedBB(0, 0, 0, 0, 0, 0);

	public FakeAir () {
		super("fake_air", Material.CLOTH);
	}


	@Override
	public AxisAlignedBB getBoundingBox (IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB;
	}

	@Override
	public boolean isNormalCube (IBlockState state, IBlockAccess world, BlockPos pos) {
		return false;
	}

	@Override
	public boolean isOpaqueCube (IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube (IBlockState state) {
		return false;
	}

	@Override
	public boolean causesSuffocation (IBlockState state) {
		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public BlockRenderLayer getRenderLayer () {
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	public EnumBlockRenderType getRenderType (IBlockState state) {
		return EnumBlockRenderType.INVISIBLE;
		//return super.getRenderType(state);
	}

	@Override
	public void addCollisionBoxToList (IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
	}

	@Override
	public Item getItemDropped (IBlockState state, Random rand, int fortune) {
		return Items.AIR;
	}

	@Override
	public boolean isAir (IBlockState state, IBlockAccess world, BlockPos pos) {
		return true;
	}

	@Override
	public boolean hasTileEntity (IBlockState state) {
		return true;
	}

	@Override
	public boolean isReplaceable (IBlockAccess worldIn, BlockPos pos) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity (World world, IBlockState state) {
		return new FakeAirTileEntity();
	}
}

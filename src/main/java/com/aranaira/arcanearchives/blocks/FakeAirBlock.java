package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.blocks.templates.TemplateBlock;
import com.aranaira.arcanearchives.tileentities.FakeAirTileEntity;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.block.BlockRenderType;
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
public class FakeAirBlock extends TemplateBlock {
	protected static final AxisAlignedBB AABB = new AxisAlignedBB(0, 0, 0, 0, 0, 0);

	public FakeAirBlock () {
		super(Material.CLOTH);
	}


	@Override
	public AxisAlignedBB getBoundingBox (BlockState state, IBlockAccess source, BlockPos pos) {
		return AABB;
	}

	@Override
	public boolean isNormalCube (BlockState state, IBlockAccess world, BlockPos pos) {
		return false;
	}

	@Override
	public boolean isOpaqueCube (BlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube (BlockState state) {
		return false;
	}

	@Override
	public boolean causesSuffocation (BlockState state) {
		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public BlockRenderLayer getRenderLayer () {
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	public BlockRenderType getRenderType (BlockState state) {
		return BlockRenderType.INVISIBLE;
		//return super.getRenderType(state);
	}

	@Override
	public void addCollisionBoxToList (BlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
	}

	@Override
	public Item getItemDropped (BlockState state, Random rand, int fortune) {
		return Items.AIR;
	}

	@Override
	public boolean isAir (BlockState state, IBlockAccess world, BlockPos pos) {
		return true;
	}

	@Override
	public boolean hasTileEntity (BlockState state) {
		return true;
	}

	@Override
	public boolean isReplaceable (IBlockAccess worldIn, BlockPos pos) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity (World world, BlockState state) {
		return new FakeAirTileEntity();
	}
}

package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.blocks.templates.BlockTemplate;
import com.aranaira.arcanearchives.init.ItemLibrary;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nonnull;

public class RawQuartz extends BlockTemplate
{

	//public static final PropertyDirection DIRECTION = PropertyDirection.create("facing");
	public static final String name = "raw_quartz";

	public RawQuartz()
	{
		super(name, Material.ROCK, true);
		setLightLevel(16 / 16f);
		setHardness(1.4f);
		//setDefaultState(this.blockState.getBaseState().withProperty(DIRECTION,  EnumFacing.NORTH));
		setHarvestLevel("pickaxe", 0);
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean hasOBJModel()
	{
		return true;
	}

	@Override
	@Nonnull
	@SuppressWarnings("deprecation")
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return new AxisAlignedBB(0.2, 0.0, 0.2, 0.8, 1.0, 0.8);
	}

	@Override
	public void getDrops(@Nonnull NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, @Nonnull IBlockState state, int fortune)
	{
		drops.add(new ItemStack(ItemLibrary.RAW_RADIANT_QUARTZ, 1));
	}
}

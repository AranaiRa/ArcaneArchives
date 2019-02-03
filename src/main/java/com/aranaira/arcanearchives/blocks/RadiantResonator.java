package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.tileentities.RadiantResonatorTileEntity;
import com.aranaira.arcanearchives.util.handlers.ConfigHandler;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class RadiantResonator extends BlockTemplate
{
	public static final String name = "radiant_resonator";

	public RadiantResonator()
	{
		super(name, Material.IRON);
		setPlaceLimit(ConfigHandler.values.iRadiantResonatorLimit);
		setHardness(1.1f);
		setHarvestLevel("axe", 0);
		setEntityClass(RadiantResonatorTileEntity.class);
	}

	@Override
	public boolean hasOBJModel()
	{
		return true;
	}

	@Override
	public void onPlayerDestroy(World worldIn, BlockPos pos, IBlockState state)
	{
		if(worldIn.getBlockState(pos.up()).getBlock() instanceof RawQuartz)
		{
			worldIn.destroyBlock(pos.up(), true);
		}
		super.onPlayerDestroy(worldIn, pos, state);
	}

	@Override
	public void onExplosionDestroy(World worldIn, BlockPos pos, Explosion explosion)
	{
		if(worldIn.getBlockState(pos.up()).getBlock() instanceof RawQuartz)
		{
			worldIn.destroyBlock(pos.up(), false);
		}
		super.onExplosionDestroy(worldIn, pos, explosion);
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		super.updateTick(worldIn, pos, state, rand);
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean causesSuffocation(IBlockState state)
	{
		return false;
	}

	@Override
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) { return true; }

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) { return new RadiantResonatorTileEntity(); }
}

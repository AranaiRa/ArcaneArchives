package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.init.BlockLibrary;
import com.aranaira.arcanearchives.tileentities.AccessorTileEntity;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings("deprecation")
// TODO: Are there issues with deriving from this?
// TODO: Break textures
// TODO: Breaking parent = children
// TODO: Breaking child = parent + all other children
// TODO: WAILA, TUMAT, TOP, etc support
public class AccessorBlock extends BlockTemplate implements ITileEntityProvider
{
	public static final PropertyInteger TYPE = PropertyInteger.create("type", 0, 15);

	public AccessorBlock()
	{
		super("accessorblock", Material.ROCK);
		setTranslationKey("accessorblock");
		BlockLibrary.BLOCKS.add(this);
	}

	public IBlockState fromBlock (BlockTemplate block) {
		return getDefaultState().withProperty(TYPE, block.getAccessorId());
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(TYPE);
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(TYPE, meta);
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new ExtendedBlockState(this, new IProperty[]{TYPE}, new IUnlistedProperty[]{});
	}

	@Override
	public boolean hasAccessors()
	{
		return false;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.INVISIBLE;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isTranslucent(IBlockState state)
	{
		return true;
	}

	@Override
	public boolean isTopSolid(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean causesSuffocation(IBlockState state)
	{
		return true;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		super.onBlockAdded(worldIn, pos, state);
	}

	@Override
	public boolean hasTileEntity()
	{
		return true;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		TileEntity te = world.getTileEntity(pos);

		if(te instanceof AccessorTileEntity)
		{
			return ((AccessorTileEntity) te).onBlockActivated(pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
		}

		return super.onBlockActivated(world, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
	}

	public BlockTemplate getBlock(IBlockState state)
	{
		return BlockTemplate.getByType(state.getValue(TYPE));
	}

	/* -------------------------------- */
	/* Overriden calls via block state	*/
	@Override
	public boolean isFullBlock(IBlockState state)
	{
		return true;
	}

	@Override
	public boolean canEntitySpawn(IBlockState state, Entity entityIn)
	{
		return false;
	}

	@Override
	public int getLightOpacity(IBlockState state)
	{
		return 0;
	}

	@Override
	public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return true;
	}

	// This one may need to be customised
	@Override
	public float getExplosionResistance(World world, BlockPos pos, @Nullable Entity exploder, Explosion explosion)
	{
		return getBlock(world.getBlockState(pos)).getExplosionResistance(exploder);
	}

	/* ---------------------------- */
	/* Passed through to block type */
	@Override
	public int getLightValue(IBlockState state)
	{
		return getBlock(state).getLightValue(state);
	}

	@Override
	public boolean getUseNeighborBrightness(IBlockState state)
	{
		return getBlock(state).getUseNeighborBrightness(state);
	}

	@Override
	public Material getMaterial(IBlockState state)
	{
		return getBlock(state).getMaterial(state);
	}

	@Override
	public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		return getBlock(state).getMapColor(state, worldIn, pos);
	}

	@Override
	public boolean isBlockNormalCube(IBlockState state)
	{
		return getBlock(state).isBlockNormalCube(state);
	}

	@Override
	public boolean isNormalCube(IBlockState state)
	{
		return getBlock(state).isNormalCube(state);
	}

	@Override
	public boolean isFullCube(IBlockState state)
	{
		return getBlock(state).isFullCube(state);
	}

	@Override
	public boolean hasCustomBreakingProgress(IBlockState state)
	{
		return getBlock(state).hasCustomBreakingProgress(state);
	}

	@Override
	public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos)
	{
		return getBlock(blockState).getBlockHardness(blockState, worldIn, pos);
	}

	@Override
	public int getPackedLightmapCoords(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return getBlock(state).getPackedLightmapCoords(state, source, pos);
	}

	@Override
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		return getBlock(blockState).shouldSideBeRendered(blockState, blockAccess, pos, side);
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
	{
		return getBlock(state).getBlockFaceShape(worldIn, state, pos, face);
	}

	@Override
	public float getPlayerRelativeBlockHardness(IBlockState state, EntityPlayer player, World worldIn, BlockPos pos)
	{
		return getBlock(state).getPlayerRelativeBlockHardness(state, player, worldIn, pos);
	}

	@Override
	public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		return getBlock(blockState).getWeakPower(blockState, blockAccess, pos, side);
	}

	@Override
	public boolean canProvidePower(IBlockState state)
	{
		return getBlock(state).canProvidePower(state);
	}

	@Override
	public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		return getBlock(blockState).getStrongPower(blockState, blockAccess, pos, side);
	}

	@Override
	public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param)
	{
		return getBlock(state).eventReceived(state, worldIn, pos, id, param);
	}

	@Override
	public EnumPushReaction getPushReaction(IBlockState state)
	{
		return getBlock(state).getPushReaction(state);
	}

	@Override
	public float getAmbientOcclusionLightValue(IBlockState state)
	{
		return getBlock(state).getAmbientOcclusionLightValue(state);
	}

	@Override
	public boolean hasComparatorInputOverride(IBlockState state)
	{
		return getBlock(state).hasComparatorInputOverride(state);
	}

	@Override
	public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos)
	{
		return getBlock(blockState).getComparatorInputOverride(blockState, worldIn, pos);
	}

	@Nullable
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new AccessorTileEntity();
	}
}


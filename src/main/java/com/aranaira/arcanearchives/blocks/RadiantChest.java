package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.common.AAGuiHandler;
import com.aranaira.arcanearchives.tileentities.RadiantChestTileEntity;
import com.aranaira.arcanearchives.util.NetworkHelper;
import com.aranaira.arcanearchives.util.handlers.AATickHandler;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class RadiantChest extends BlockTemplate implements ITileEntityProvider
{

	public static final String NAME = "radiant_chest";


	public RadiantChest()
	{
		super(NAME, Material.GLASS);
		setLightLevel(16 / 16f);
		setHardness(1.7f);
		setResistance(6000F);
		setHarvestLevel("axe", 0);
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
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		for(Vec3d vec : AATickHandler.GetInstance().mBlockPositions)
		{
			Vec3d bpos = new Vec3d(pos.getX(), pos.getY(), pos.getZ());

			if(vec.equals(bpos))
			{
				AATickHandler.GetInstance().mBlockPositionsToRemove.add(bpos);
			}
		}

		playerIn.openGui(ArcaneArchives.instance, AAGuiHandler.RADIANT_CHEST, worldIn, pos.getX(), pos.getY(), pos.getZ());

		return true;
	}

	@Override
	@ParametersAreNonnullByDefault
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		TileEntity te = worldIn.getTileEntity(pos);
		if (!(te instanceof RadiantChestTileEntity)) {
			// There needs to be an error emssage here TODO
		} else
		{
			NetworkHelper.getArcaneArchivesNetwork(((RadiantChestTileEntity) te).NetworkID).triggerUpdate();

			for(Vec3d vec : AATickHandler.GetInstance().mBlockPositions)
			{
				Vec3d bpos = new Vec3d(pos.getX(), pos.getY(), pos.getZ());

				if(vec.equals(bpos))
				{
					AATickHandler.GetInstance().mBlockPositionsToRemove.add(bpos);
				}
			}

			TileEntity tileentity = worldIn.getTileEntity(pos);

			if(tileentity instanceof IInventory)
			{
				InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory) tileentity);
				worldIn.updateComparatorOutputLevel(pos, this);
			}
		}
		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta)
	{
		return new RadiantChestTileEntity();
	}

	@Override
	public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity)
	{
		return super.canEntityDestroy(state, world, pos, entity);
	}

	@Override
	@ParametersAreNonnullByDefault
	public boolean canBeReplacedByLeaves(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return false;
	}
}

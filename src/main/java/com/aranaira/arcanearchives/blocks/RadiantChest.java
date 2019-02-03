package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.common.AAGuiHandler;
import com.aranaira.arcanearchives.tileentities.RadiantChestTileEntity;
import com.aranaira.arcanearchives.data.NetworkHelper;
import com.aranaira.arcanearchives.util.DropHelper;
import com.aranaira.arcanearchives.util.handlers.AATickHandler;
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
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class RadiantChest extends BlockTemplate
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
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		RemoveChestLines(pos);

		playerIn.openGui(ArcaneArchives.instance, AAGuiHandler.RADIANT_CHEST, worldIn, pos.getX(), pos.getY(), pos.getZ());

		return true;
	}

	@Override
	@ParametersAreNonnullByDefault
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		RemoveChestLines(pos);

		if (!worldIn.isRemote)
		{
			TileEntity te = worldIn.getTileEntity(pos);
			if(te instanceof RadiantChestTileEntity)
			{
				NetworkHelper.getArcaneArchivesNetwork(((RadiantChestTileEntity) te).NetworkID).triggerUpdate();

				// This is never an IInventory
				IItemHandler inv = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
				DropHelper.dropInventoryItems(worldIn, pos, inv);
			}
		}
		super.breakBlock(worldIn, pos, state);
	}

	public static void RemoveChestLines(BlockPos pos)
	{
		for(Vec3d vec : AATickHandler.GetInstance().mBlockPositions)
		{
			Vec3d bpos = new Vec3d(pos.getX(), pos.getY(), pos.getZ());

			if(vec.equals(bpos))
			{
				AATickHandler.GetInstance().mBlockPositionsToRemove.add(bpos);
			}
		}
	}

	@Override
	public boolean hasTileEntity(IBlockState state) { return true; }

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) { return new RadiantChestTileEntity(); }

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

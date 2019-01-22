package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.common.AAGuiHandler;
import com.aranaira.arcanearchives.tileentities.MatrixRepositoryTileEntity;
import com.aranaira.arcanearchives.util.handlers.AATickHandler;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class MatrixRepository extends BlockTemplate
{

	public static final String name = "matrix_repository";

	public MatrixRepository()
	{
		super(name, Material.GLASS);
		setLightLevel(16 / 16f);
		setSize(1, 3, 1);
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

		playerIn.openGui(ArcaneArchives.instance, AAGuiHandler.MATRIX_REPOSITORY, worldIn, pos.getX(), pos.getY(), pos.getZ());

		return true;
	}

	@Override
	public void addInformation(ItemStack stack, @Nonnull World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		//TODO: Add real tooltip
	}

	@Override
	public boolean hasOBJModel()
	{
		return true;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new MatrixRepositoryTileEntity();
	}
}

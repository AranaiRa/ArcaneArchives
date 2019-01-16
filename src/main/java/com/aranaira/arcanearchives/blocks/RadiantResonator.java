package com.aranaira.arcanearchives.blocks;

import java.util.Random;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.data.AAWorldSavedData;
import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantResonatorTileEntity;
import com.aranaira.arcanearchives.util.NetworkHelper;
import com.aranaira.arcanearchives.util.Placeable;
import com.aranaira.arcanearchives.util.handlers.ConfigHandler;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.b3d.B3DLoader;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;

public class RadiantResonator extends BlockTemplate 
{
	public static final PropertyDirection FACING = PropertyDirection.create("facing");
	public static final String name = "radiant_resonator";
	public RadiantResonator() 
	{
		super(name, Material.IRON);
		PlaceLimit = ConfigHandler.values.iRadiantResonatorLimit;
		setHardness(1.1f);
		setHarvestLevel("axe", 0);
	}
	
	@Override
	public boolean hasOBJModel()
	{
		return true;
	}

	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
		if (worldIn.getBlockState(pos.up()).getBlock() instanceof RawQuartz)
		{
			worldIn.destroyBlock(pos.up(), true);
		}
		super.onBlockDestroyedByPlayer(worldIn, pos, state);
	}

	@Override
	public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosion) {
		if (worldIn.getBlockState(pos.up()).getBlock() instanceof RawQuartz)
		{
			worldIn.destroyBlock(pos.up(), false);
		}
		super.onBlockDestroyedByExplosion(worldIn, pos, explosion);
	}
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		super.updateTick(worldIn, pos, state, rand);
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new RadiantResonatorTileEntity();
	}
	
	 @Override
     public boolean isOpaqueCube(IBlockState state)
     {
         return false;
     }

     @Override
     public boolean isFullCube(IBlockState state)
     {
         return false;
     }

     @Override
     public boolean causesSuffocation(IBlockState state)
     {
         return false;
     }

     @Override
     public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
     {
         return this.getDefaultState().withProperty(FACING, getFacingFromEntity(world, pos, placer));
     }

     @Override
     public IBlockState getStateFromMeta(int meta)
     {
         return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta));
     }

     @Override
     public int getMetaFromState(IBlockState state)
     {
         return state.getValue(FACING).getIndex();
     }

     @Override
     public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
     {
         
         return false;
     }

     @Override
     public BlockStateContainer createBlockState()
     {
         return new ExtendedBlockState(this, new IProperty[]{FACING}, new IUnlistedProperty[]{Properties.AnimationProperty});
     }

     public static EnumFacing getFacingFromEntity(World worldIn, BlockPos clickedBlock, EntityLivingBase entityIn)
     {
         if (MathHelper.abs((float) entityIn.posX - (float) clickedBlock.getX()) < 2.0F && MathHelper.abs((float) entityIn.posZ - (float) clickedBlock.getZ()) < 2.0F)
         {
             double d0 = entityIn.posY + (double) entityIn.getEyeHeight();

             if (d0 - (double) clickedBlock.getY() > 2.0D)
             {
                 return EnumFacing.UP;
             }

             if ((double) clickedBlock.getY() - d0 > 0.0D)
             {
                 return EnumFacing.DOWN;
             }
         }

         return entityIn.getHorizontalFacing().getOpposite();
     }
     
     @Override
     public BlockRenderLayer getBlockLayer()
     {
    	 return BlockRenderLayer.CUTOUT;
     }
}

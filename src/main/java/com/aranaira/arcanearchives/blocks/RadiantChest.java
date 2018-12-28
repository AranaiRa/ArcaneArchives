package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.common.AAGuiHandler;
import com.aranaira.arcanearchives.common.ContainerRadiantChest;
import com.aranaira.arcanearchives.tileentities.RadiantChestTileEntity;
import com.aranaira.arcanearchives.util.NetworkHelper;
import com.aranaira.arcanearchives.util.handlers.AATickHandler;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

public class RadiantChest extends BlockTemplate implements ITileEntityProvider{

	public static final String NAME = "radiant_chest"; 
	
	
	public RadiantChest() {
		super(NAME, Material.GLASS);
		setLightLevel(16/16f);
		setHardness(1.7f);
		setResistance(6000F);
		setHarvestLevel("axe", 0);
	}

    @Override
    public boolean isFullCube(IBlockState state) 
    {
        return false;
    }

    @Override
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
		if (AATickHandler.GetInstance().mIsDrawingLine)
		{
			Vec3d bpos = new Vec3d(pos.getX(), pos.getY(), pos.getZ());

			if (AATickHandler.GetInstance().mBlockPosition.equals(bpos))
			{
				AATickHandler.GetInstance().mIsDrawingLine = false;
			}
		}
		

		playerIn.openGui(ArcaneArchives.Instance, AAGuiHandler.RADIANT_CHEST, worldIn, pos.getX(), pos.getY(), pos.getZ());
		
		return true;
	}
	
	@Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
		NetworkHelper.getArcaneArchivesNetwork(((RadiantChestTileEntity)worldIn.getTileEntity(pos)).NetworkID).RemoveBlockFromNetwork(tileEntityInstance);;
		
		if (AATickHandler.GetInstance().mIsDrawingLine)
		{
			Vec3d bpos = new Vec3d(pos.getX(), pos.getY(), pos.getZ());

			if (AATickHandler.GetInstance().mBlockPosition.equals(bpos))
			{
				AATickHandler.GetInstance().mIsDrawingLine = false;
			}
		}
		
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof IInventory)
        {
            InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)tileentity);
            worldIn.updateComparatorOutputLevel(pos, this);
        }

        super.breakBlock(worldIn, pos, state);
    }

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return tileEntityInstance = new RadiantChestTileEntity();
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) 
	{
		return tileEntityInstance = new RadiantChestTileEntity();
	}
	
	@Override
	public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
		// TODO Auto-generated method stub
		return super.canEntityDestroy(state, world, pos, entity);
	}
	
	@Override
	public boolean canBeReplacedByLeaves(IBlockState state, IBlockAccess world, BlockPos pos) {
		return false;
	}
}

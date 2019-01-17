package com.aranaira.arcanearchives.items;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.blocks.RadiantChest;
import com.aranaira.arcanearchives.init.BlockLibrary;
import com.aranaira.arcanearchives.tileentities.RadiantChestTileEntity;
import com.aranaira.arcanearchives.util.NetworkHelper;

import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockWorkbench;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RawQuartzItem extends ItemTemplate
{
	public static final String NAME = "item_rawquartz";
	
	public RawQuartzItem()
	{
		super(NAME);
	}
	
	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) 
	{
		if (!player.isSneaking())
			return EnumActionResult.PASS;
		
		ItemStack itemstack = player.getHeldItem(hand);
		
		if ((world.getBlockState(pos).getBlock() instanceof BlockChest))
		{
			if (world.isRemote)
				return EnumActionResult.FAIL;
			
			if (!(world.getBlockState(pos).getBlock() instanceof BlockChest))
			{
				return EnumActionResult.FAIL;
			}
			
			BlockPos secondaryChestPos = new BlockPos(0, 0, 0);
			boolean secondaryChest = false;
			
			if (world.getBlockState(pos.add(0, 0, 1)).getBlock() instanceof BlockChest)
			{
				secondaryChestPos = pos.add(0, 0, 1);
				secondaryChest = true;
			}
			else if (world.getBlockState(pos.add(0, 0, -1)).getBlock() instanceof BlockChest)
			{
				secondaryChestPos = pos.add(0, 0, -1);
				secondaryChest = true;
			}
			else if (world.getBlockState(pos.add(1, 0, 0)).getBlock() instanceof BlockChest)
			{
				secondaryChestPos = pos.add(1, 0, 0);
				secondaryChest = true;
			}
			else if (world.getBlockState(pos.add(-1, 0, 0)).getBlock() instanceof BlockChest)
			{
				secondaryChestPos = pos.add(-1, 0, 0);
				secondaryChest = true;
			}
			
			TileEntity te = world.getTileEntity(pos);
			
			RadiantChestTileEntity newchest = new RadiantChestTileEntity();
			
			ItemStack[] chestContents = new ItemStack[27];
			ItemStack[] secondaryChestContents = new ItemStack[27];
			
			EnumFacing chestFacing = EnumFacing.DOWN;
			
			if (te instanceof TileEntityChest)
            {
                if (((TileEntityChest) te).numPlayersUsing > 0)
                    return EnumActionResult.FAIL;
                IBlockState chestState = world.getBlockState(pos);
                chestFacing = chestState.getValue(BlockChest.FACING);
                chestContents = new ItemStack[((TileEntityChest) te).getSizeInventory()];
                for (int i = 0; i < chestContents.length; i++)
                {
                    chestContents[i] = ((TileEntityChest)te).getStackInSlot(i);
                }

                if (secondaryChest)
                {
                    TileEntity ste = world.getTileEntity(secondaryChestPos);

                    if (ste != null) {
						secondaryChestContents = new ItemStack[((TileEntityChest) ste).getSizeInventory()];
						for (int i = 0; i < secondaryChestContents.length; i++) {
							secondaryChestContents[i] = ((TileEntityChest) ste).getStackInSlot(i);
						}
					}
                }
            }
            else
            {
                return EnumActionResult.FAIL;
            }

			te.updateContainingBlockInfo();

            ((TileEntityChest) te).checkForAdjacentChests();
			
			world.removeTileEntity(pos);
			world.setBlockToAir(pos);
			
			if (secondaryChest)
			{
				world.removeTileEntity(secondaryChestPos);
				world.setBlockToAir(secondaryChestPos);
			}
			
			IBlockState iblockstate = BlockLibrary.RADIANT_CHEST.getDefaultState();
			
			world.setTileEntity(pos, newchest);
			world.setBlockState(pos, iblockstate);
			
			world.notifyBlockUpdate(pos, iblockstate, iblockstate, 3);
			
			TileEntity te2 = world.getTileEntity(pos);
			
			if (te2 instanceof RadiantChestTileEntity)
			{
				((RadiantChestTileEntity) te2).setContents(chestContents, secondaryChestContents, secondaryChest);
				((RadiantChestTileEntity) te2).setFacing(chestFacing);
				NetworkHelper.getArcaneArchivesNetwork(player.getUniqueID()).AddTileToNetwork((RadiantChestTileEntity) te2);
				((RadiantChestTileEntity) te2).NetworkID = player.getUniqueID();
			}
			
			if (!player.capabilities.isCreativeMode)
			{
				itemstack.shrink(1);
			}
			
			return EnumActionResult.PASS;
			
			//return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
		} else if ((world.getBlockState(pos).getBlock() instanceof BlockWorkbench))
		{
			world.setBlockToAir(pos);
			IBlockState iblockstate = BlockLibrary.RADIANT_CRAFTING_TABLE.getDefaultState();
			world.setBlockState(pos, iblockstate);
			itemstack.shrink(1);
		}
		
		return EnumActionResult.PASS;
	}
}

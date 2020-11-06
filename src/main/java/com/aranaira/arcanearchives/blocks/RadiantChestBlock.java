package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.AAGuiHandler;
import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.blocks.interfaces.INetworkBlock;
import com.aranaira.arcanearchives.blocks.templates.TemplateBlock;
import com.aranaira.arcanearchives.client.tracking.LineHandler;
import com.aranaira.arcanearchives.tileentities.RadiantChestTile;
import com.aranaira.arcanearchives.util.ItemUtils;
import com.aranaira.arcanearchives.util.WorldUtil;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

@SuppressWarnings("deprecation")
public class RadiantChestBlock extends TemplateBlock implements INetworkBlock {
  public static final String NAME = "radiant_chest";

  public RadiantChestBlock(Material materialIn) {
    super(materialIn);
  }

  @Override
  @SuppressWarnings("deprecation")
  public boolean isFullCube(BlockState state) {
    return false;
  }

  @Override
  @SuppressWarnings("deprecation")
  public boolean isOpaqueCube(BlockState state) {
    return false;
  }

  @Override
  public boolean onBlockActivated(World worldIn, BlockPos pos, BlockState state, PlayerEntity playerIn, Hand hand, Direction facing, float hitX, float hitY, float hitZ) {
    LineHandler.removeLine(pos, playerIn.dimension);


/*    ItemStack mainHand = playerIn.getHeldItemMainhand();
    ItemStack offHand = playerIn.getHeldItemOffhand();
    ItemStack displayStack = ItemStack.EMPTY;

    if (mainHand.getItem() == ItemRegistry.SCEPTER_MANIPULATION && !offHand.isEmpty()) {
      displayStack = offHand;
    } else if (offHand.getItem() == ItemRegistry.SCEPTER_MANIPULATION && !mainHand.isEmpty()) {
      displayStack = mainHand;
    }


    boolean clearDisplayed = false;

    if ((mainHand.getItem() == ItemRegistry.SCEPTER_MANIPULATION && offHand.isEmpty() || offHand.getItem() == ItemRegistry.SCEPTER_MANIPULATION && mainHand.isEmpty()) && playerIn.isSneaking()) {
      clearDisplayed = true;
    }*/


    int dimension = worldIn.provider.getDimension();

    //if (false) { //!displayStack.isEmpty() || clearDisplayed) {
/*      if (worldIn.isRemote) {
        if (clearDisplayed) {
          UnsetItem packet = new UnsetItem(pos, dimension);
          Networking.CHANNEL.sendToServer(packet);
        } else if (!displayStack.isEmpty()) {
          SetItemAndFacing packet = new SetItemAndFacing(pos, dimension, displayStack, facing);
          Networking.CHANNEL.sendToServer(packet);
        }

      }*/
    //} else {
      if (!worldIn.isRemote) {
        playerIn.openGui(ArcaneArchives.instance, AAGuiHandler.GuiType.RadiantChest.ordinal(), worldIn, pos.getX(), pos.getY(), pos.getZ());
      }
    //}

    return true;
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(World world, BlockState state) {
    return new RadiantChestTile();
  }

  @Override
  public void breakBlock(World worldIn, BlockPos pos, BlockState state) {
    LineHandler.removeLine(pos, worldIn.provider.getDimension());

    if (!worldIn.isRemote) {
      TileEntity te = worldIn.getTileEntity(pos);
      if (te instanceof RadiantChestTile) {
        // This is never an IInventory
        IItemHandler inv = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        ItemUtils.dropInventoryItems(worldIn, pos, inv);
      }
    }

    worldIn.updateComparatorOutputLevel(pos, this);
    super.breakBlock(worldIn, pos, state);
  }

  @Override
  public boolean hasComparatorInputOverride(BlockState state) {
    return true;
  }

  @Override
  public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
    RadiantChestTile te = WorldUtil.getTileEntity(RadiantChestTile.class, worldIn, pos);
    if (te != null) {
      return te.getInventory().calcRedstone();
    }
    return 0;
  }
}

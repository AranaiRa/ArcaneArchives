package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.blocks.templates.TemplateBlock;
import com.aranaira.arcanearchives.tiles.RadiantChestTileEntity;
import com.aranaira.arcanearchives.util.ItemUtils;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.List;

@SuppressWarnings("deprecation")
public class RadiantChestBlock extends TemplateBlock {
  public static final String NAME = "radiant_chest";

  public RadiantChestBlock() {
    super(Material.GLASS);
    setLightLevel(16 / 16f);
    setHardness(3f);
    setResistance(6000F);
    setHarvestLevel("pickaxe", 0);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
    tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.device.radiant_chest"));
  }

  @Override
  @SuppressWarnings("deprecation")
  public boolean isFullCube(IBlockState state) {
    return false;
  }

  @Override
  @SuppressWarnings("deprecation")
  public boolean isOpaqueCube(IBlockState state) {
    return false;
  }

  @Override
  public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
    /*		LineHandler.removeLine(pos, playerIn.dimension);*/

    ItemStack mainHand = playerIn.getHeldItemMainhand();
    ItemStack offHand = playerIn.getHeldItemOffhand();
    ItemStack displayStack = ItemStack.EMPTY;

/*		if (mainHand.getItem() == ItemRegistry.SCEPTER_MANIPULATION && !offHand.isEmpty()) {
			displayStack = offHand;
		} else if (offHand.getItem() == ItemRegistry.SCEPTER_MANIPULATION && !mainHand.isEmpty()) {
			displayStack = mainHand;
		}*/

    boolean clearDisplayed = false;

/*		if ((mainHand.getItem() == ItemRegistry.SCEPTER_MANIPULATION && offHand.isEmpty() || offHand.getItem() == ItemRegistry.SCEPTER_MANIPULATION && mainHand.isEmpty()) && playerIn.isSneaking()) {
			clearDisplayed = true;
		}*/

    int dimension = worldIn.provider.getDimension();

    if (!displayStack.isEmpty() || clearDisplayed) {
      if (worldIn.isRemote) {
/*				if (clearDisplayed) {
					UnsetItem packet = new UnsetItem(pos, dimension);
					Networking.CHANNEL.sendToServer(packet);
				} else if (!displayStack.isEmpty()) {
					SetItemAndFacing packet = new SetItemAndFacing(pos, dimension, displayStack, facing);
					Networking.CHANNEL.sendToServer(packet);
				}*/
      }
    } else {
      if (!worldIn.isRemote) {
        /*				playerIn.openGui(ArcaneArchives.instance, AAGuiHandler.RADIANT_CHEST, worldIn, pos.getX(), pos.getY(), pos.getZ());*/
      }
    }

    return true;
  }

  @Override
  public boolean hasTileEntity(IBlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(World world, IBlockState state) {
    return new RadiantChestTileEntity();
  }

  @Override
  public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
    /*		LineHandler.removeLine(pos, worldIn.provider.getDimension());*/

    if (!worldIn.isRemote) {
      TileEntity te = worldIn.getTileEntity(pos);
      if (te instanceof RadiantChestTileEntity) {
        /*				ServerNetwork network = DataHelper.getServerNetwork(((RadiantChestTileEntity) te).networkId);*/

        // This is never an IInventory
        IItemHandler inv = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        ItemUtils.dropInventoryItems(worldIn, pos, inv);
      }
    }

    worldIn.updateComparatorOutputLevel(pos, this);
    super.breakBlock(worldIn, pos, state);
  }

/*	@Override
	public boolean hasComparatorInputOverride (IBlockState state) {
		return true;
	}*/

/*	@Override
	public int getComparatorInputOverride (IBlockState blockState, World worldIn, BlockPos pos) {
		RadiantChestTileEntity te = WorldUtil.getTileEntity(RadiantChestTileEntity.class, worldIn, pos);
		if (te != null) {
			return te.getInventory().calcRedstone();
		}
		return 0;
	}*/
}
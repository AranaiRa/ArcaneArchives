package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.AAGuiHandler;
import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.blocks.templates.BlockTemplate;
import com.aranaira.arcanearchives.client.render.LineHandler;
import com.aranaira.arcanearchives.data.DataHelper;
import com.aranaira.arcanearchives.data.ServerNetwork;
import com.aranaira.arcanearchives.init.ItemRegistry;
import com.aranaira.arcanearchives.network.Networking;
import com.aranaira.arcanearchives.network.PacketRadiantChest.SetItemAndFacing;
import com.aranaira.arcanearchives.network.PacketRadiantChest.UnsetItem;
import com.aranaira.arcanearchives.tileentities.RadiantChestTileEntity;
import com.aranaira.arcanearchives.util.DropUtils;
import com.aranaira.arcanearchives.util.WorldUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.List;

@SuppressWarnings("deprecation")
public class RadiantChest extends BlockTemplate {
  public static final String NAME = "radiant_chest";

  public RadiantChest() {
    super(NAME, Material.GLASS);
    setLightLevel(16 / 16f);
    setHardness(3f);
    setResistance(6000F);
    setHarvestLevel("pickaxe", 0);
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
    tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.device.radiant_chest"));
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

    ItemStack mainHand = playerIn.getHeldItemMainhand();
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
    }

    int dimension = worldIn.provider.getDimension();

    if (!displayStack.isEmpty() || clearDisplayed) {
      if (worldIn.isRemote) {
        if (clearDisplayed) {
          UnsetItem packet = new UnsetItem(pos, dimension);
          Networking.CHANNEL.sendToServer(packet);
        } else if (!displayStack.isEmpty()) {
          SetItemAndFacing packet = new SetItemAndFacing(pos, dimension, displayStack, facing);
          Networking.CHANNEL.sendToServer(packet);
        }
      }
    } else {
      if (!worldIn.isRemote) {
        playerIn.openGui(ArcaneArchives.instance, AAGuiHandler.RADIANT_CHEST, worldIn, pos.getX(), pos.getY(), pos.getZ());
      }
    }

    return true;
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(World world, BlockState state) {
    return new RadiantChestTileEntity();
  }

  @Override
  public boolean hasOBJModel() {
    return true;
  }

  @Override
  public void breakBlock(World worldIn, BlockPos pos, BlockState state) {
    LineHandler.removeLine(pos, worldIn.provider.getDimension());

    if (!worldIn.isRemote) {
      TileEntity te = worldIn.getTileEntity(pos);
      if (te instanceof RadiantChestTileEntity) {
        ServerNetwork network = DataHelper.getServerNetwork(((RadiantChestTileEntity) te).networkId);

        // This is never an IInventory
        IItemHandler inv = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        DropUtils.dropInventoryItems(worldIn, pos, inv);
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
    RadiantChestTileEntity te = WorldUtil.getTileEntity(RadiantChestTileEntity.class, worldIn, pos);
    if (te != null) {
      return te.getInventory().calcRedstone();
    }
    return 0;
  }
}
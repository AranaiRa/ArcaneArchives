package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.AAGuiHandler;
import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.blocks.templates.BlockTemplate;
import com.aranaira.arcanearchives.tileentities.RadiantCraftingTableTileEntity;
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

import javax.annotation.Nullable;
import java.util.List;

public class RadiantCraftingTable extends BlockTemplate {

  public static final String NAME = "radiant_crafting_table";

  public RadiantCraftingTable() {
    super(NAME, Material.GLASS);
    setLightLevel(16 / 16f);
    setHardness(3f);
    setHarvestLevel("pickaxe", 0);
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
    tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.device.radiant_crafting_table"));
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
    // ArcaneArchives.logger.info("TRYING TO OPEN GUI");
    playerIn.openGui(ArcaneArchives.instance, AAGuiHandler.RADIANT_CRAFTING_TABLE, worldIn, pos.getX(), pos.getY(), pos.getZ());

    return true;
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(World world, BlockState state) {
    return new RadiantCraftingTableTileEntity();
  }

  @Override
  public boolean hasOBJModel() {
    return true;
  }

  @Override
  public void breakBlock(World world, BlockPos pos, BlockState state) {
    RadiantCraftingTableTileEntity te = WorldUtil.getTileEntity(RadiantCraftingTableTileEntity.class, world, pos);
    if (te != null) {
      te.blockBroken();
    }
    super.breakBlock(world, pos, state);
  }
}

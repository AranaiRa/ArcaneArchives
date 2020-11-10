/*package com.aranaira.arcanearchives.items;

import com.aranaira.arcanearchives.items.templates.ItemTemplate;
import com.aranaira.arcanearchives.items.upgrades.IUpgrade;
import com.aranaira.arcanearchives.types.UpgradeType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class DevouringCharmItem extends ItemTemplate implements IUpgrade {



  @Override
  public boolean doesSneakBypassUse(ItemStack stack, IBlockAccess world, BlockPos pos, PlayerEntity player) {
    return true;
  }

  @Override
  @Nonnull
  public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
    if (world.isRemote) {
      return new ActionResult<>(ActionResultType.SUCCESS, player.getHeldItem(hand));
    }
    //player.openGui(ArcaneArchives.instance, AAGuiHandler.DEVOURING_CHARM, world, (int) player.posX, (int) player.posY, (int) player.posZ);
    return new ActionResult<>(ActionResultType.SUCCESS, player.getHeldItem(hand));
  }

  @Override
  public UpgradeType getType(ItemStack stack) {
    return UpgradeType.VOID;
  }

  @Override
  public int getUpgradeSize(ItemStack stack) {
    return IUpgrade.NO_SIZE;
  }

  //public static List<Class<?>> UPGRADE_FOR = Arrays.asList(RadiantTankTileEntity.class, RadiantTroveTileEntity.class);

*//*  @Override
  public List<Class<?>> upgradeFor() {
    return UPGRADE_FOR;
  }*//*
}*/

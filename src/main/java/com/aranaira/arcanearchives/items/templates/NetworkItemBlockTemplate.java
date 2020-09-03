package com.aranaira.arcanearchives.items.templates;

import com.aranaira.arcanearchives.data.storage.ClientDataStorage;
import com.aranaira.arcanearchives.util.NetworkItemUtil;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class NetworkItemBlockTemplate extends ItemBlock {
  public NetworkItemBlockTemplate(Block block) {
    super(block);
  }

  @Override
  public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
    return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
  }

  @Override
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
    super.addInformation(stack, worldIn, tooltip, flagIn);

    UUID network = NetworkItemUtil.getNetworkId(stack);
    if (network == null) {
      return;
    }

    tooltip.add("");
    tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.network.name.network", ClientDataStorage.getStringFor(network)));
  }
}

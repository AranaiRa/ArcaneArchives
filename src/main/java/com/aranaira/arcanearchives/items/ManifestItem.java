package com.aranaira.arcanearchives.items;

import com.aranaira.arcanearchives.client.Keybinds;
import com.aranaira.arcanearchives.items.templates.ItemTemplate;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;


public class ManifestItem extends ItemTemplate {
  public ManifestItem() {
    setMaxStackSize(1);
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
    openManifest(worldIn, playerIn);

    return new ActionResult<>(ActionResultType.SUCCESS, playerIn.getHeldItem(handIn));
  }

  public static void openManifest(World worldIn, PlayerEntity playerIn) {
/*    if (playerIn.isSneaking()) {
      ManifestTrackingUtils.clear();
    } else {
      ClientNetwork network = DataHelper.getClientNetwork(playerIn.getUniqueID());
      network.manifestItems.clear();
      network.synchroniseManifest();

      playerIn.openGui(ArcaneArchives.instance, AAGuiHandler.MANIFEST, worldIn, (int) playerIn.posX, (int) playerIn.posY, (int) playerIn.posZ);
    }*/
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
    tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.item.manifest1"));

    if (Keybinds.manifestKey.getKeyCode() != 0) {
      tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.item.manifest2", I18n.format("arcanearchives.tooltip.item.manifest.combiner", Keybinds.manifestKey.getDisplayName(), TextFormatting.BOLD + I18n.format("arcanearchives.tooltip.item.manifest2.main_key") + TextFormatting.RESET + TextFormatting.GOLD)));
      tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.item.manifest3", I18n.format("arcanearchives.tooltip.item.manifest.combiner", I18n.format("arcanearchives.tooltip.item.manifest3.secondary_key") + Keybinds.manifestKey.getDisplayName(), TextFormatting.BOLD + I18n.format("arcanearchives.tooltip.item.manifest3.main_key") + TextFormatting.RESET + TextFormatting.GOLD)));
    } else {
      tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.item.manifest2", TextFormatting.BOLD + I18n.format("arcanearchives.tooltip.item.manifest2.main_key") + TextFormatting.RESET + TextFormatting.GOLD));
      tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.item.manifest3", TextFormatting.BOLD + I18n.format("arcanearchives.tooltip.item.manifest3.main_key") + TextFormatting.RESET + TextFormatting.GOLD));
    }
  }
}

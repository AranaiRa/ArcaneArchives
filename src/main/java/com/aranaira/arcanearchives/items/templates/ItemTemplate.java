package com.aranaira.arcanearchives.items.templates;

import com.aranaira.arcanearchives.items.upgrades.IUpgrade;
import com.aranaira.arcanearchives.items.upgrades.UpgradeInfo;
import com.aranaira.arcanearchives.types.enums.UpgradeType;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class ItemTemplate extends Item implements IUpgrade {
  protected String tooltip = null;
  protected String formatting = "";
  protected UpgradeInfo upgradeInfo = null;

  public ItemTemplate() {
  }

  public ItemTemplate setTooltip(String text) {
    return setTooltip(text, "");
  }

  public ItemTemplate setTooltip(String text, TextFormatting formatting) {
    return setTooltip(text, "" + formatting);
  }

  public ItemTemplate setTooltip(String text, String formatting) {
    this.tooltip = text;
    this.formatting = formatting;
    return this;
  }

  @Override
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
    super.addInformation(stack, worldIn, tooltip, flagIn);

    if (tooltip != null) {
      tooltip.add("");
      tooltip.add(formatting + I18n.format(this.tooltip));
    }
  }

  @Override
  public ItemTemplate setMaxStackSize(int maxStackSize) {
    return (ItemTemplate) super.setMaxStackSize(maxStackSize);
  }

  @Override
  public ItemTemplate setHasSubtypes(boolean hasSubtypes) {
    return (ItemTemplate) super.setHasSubtypes(hasSubtypes);
  }

  @Override
  public ItemTemplate setMaxDamage(int maxDamageIn) {
    return (ItemTemplate) super.setMaxDamage(maxDamageIn);
  }

  @Override
  public ItemTemplate setTranslationKey(String key) {
    return (ItemTemplate) super.setTranslationKey(key);
  }

  @Override
  public ItemTemplate setContainerItem(Item containerItemTemplate) {
    return (ItemTemplate) super.setContainerItem(containerItemTemplate);
  }

  @Override
  public ItemTemplate setCreativeTab(CreativeTabs tab) {
    return (ItemTemplate) super.setCreativeTab(tab);
  }

  @Override
  public ItemTemplate setNoRepair() {
    return (ItemTemplate) super.setNoRepair();
  }

  public ItemTemplate setUpgradeInfo(UpgradeInfo info) {
    this.upgradeInfo = info;
    return this;
  }

  @Override
  public int getUpgradeSlot(ItemStack stack) {
    if (!isUpgrade()) {
      return -1;
    }

    return upgradeInfo.getUpgradeSlot(stack);
  }

  @Override
  public int getUpgradeSize(ItemStack stack) {
    if (!isUpgrade()) {
      return -1;
    }

    return upgradeInfo.getUpgradeSize(stack);
  }

  @Override
  public List<Class<? extends TileEntity>> getUpgradeClasses(ItemStack stack) {
    if (!isUpgrade()) {
      return Collections.emptyList();
    }

    return upgradeInfo.getUpgradeClasses(stack);
  }

  @Override
  public boolean isUpgrade() {
    return upgradeInfo != null;
  }

  @Override
  public UpgradeType getType(ItemStack stack) {
    return upgradeInfo.getType(stack);
  }
}

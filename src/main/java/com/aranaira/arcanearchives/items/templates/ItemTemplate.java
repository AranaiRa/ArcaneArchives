package com.aranaira.arcanearchives.items.templates;

import com.aranaira.arcanearchives.items.upgrades.IUpgrade;
import com.aranaira.arcanearchives.items.upgrades.UpgradeInfo;
import com.aranaira.arcanearchives.types.UpgradeType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;

import java.util.Collections;
import java.util.List;

public class ItemTemplate extends Item implements IUpgrade {
  protected List<String> tooltip = null;
  protected String formatting = "";
  protected UpgradeInfo upgradeInfo = null;

  public ItemTemplate(Item.Properties properties) {
    super(properties);
  }

  public ItemTemplate setTooltip(String text) {
    return setTooltip(text, "");
  }

  public ItemTemplate setTooltip(List<String> text) {
    return setTooltip(text, "");
  }

  public ItemTemplate setTooltip(String text, TextFormatting formatting) {
    return setTooltip(text, "" + formatting);
  }

  public ItemTemplate setTooltip(List<String> text, TextFormatting formatting) {
    return setTooltip(text, "" + formatting);
  }

  public ItemTemplate setTooltip(String text, String formatting) {
    this.tooltip = Collections.singletonList(text == null ? "" : text);
    this.formatting = formatting;
    return this;
  }

  public ItemTemplate setTooltip(List<String> text, String formatting) {
    this.tooltip = text;
    this.formatting = formatting;
    return this;
  }

/*  @Override
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
    super.addInformation(stack, worldIn, tooltip, flagIn);

    if (tooltip != null) {
      tooltip.add("");
      for (String line : this.tooltip) {
        if (line == null || line.isEmpty()) {
          tooltip.add("");
        } else {
          tooltip.add(formatting + I18n.format(line));
        }
      }
    }
  }*/

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

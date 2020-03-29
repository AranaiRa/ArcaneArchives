package com.aranaira.arcanearchives.blocks.templates;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

@SuppressWarnings({"UnusedReturnValue", "WeakerAccess", "NullableProblems"})
public class TemplateBlock extends Block {
  protected ItemBlock itemBlock = null;
  protected String tooltip = null;
  protected String formatting = "";

  public TemplateBlock(Material materialIn) {
    super(materialIn);
  }

  @Nullable
  public ItemBlock getItemBlock() {
    return itemBlock;
  }

  public TemplateBlock setItemBlock(ItemBlock itemBlock) {
    this.itemBlock = itemBlock;
    return this;
  }

  public TemplateBlock setTooltip(String text) {
    return setTooltip(text, "");
  }

  public TemplateBlock setTooltip(String text, TextFormatting formatting) {
    return setTooltip(text, "" + formatting);
  }

  public TemplateBlock setTooltip(String text, String formatting) {
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
  protected TemplateBlock setSoundType(SoundType sound) {
    return (TemplateBlock) super.setSoundType(sound);
  }

  @Override
  public TemplateBlock setLightOpacity(int opacity) {
    return (TemplateBlock) super.setLightOpacity(opacity);
  }

  @Override
  public TemplateBlock setLightLevel(float value) {
    return (TemplateBlock) super.setLightLevel(value);
  }

  @Override
  public TemplateBlock setResistance(float resistance) {
    return (TemplateBlock) super.setResistance(resistance);
  }

  @Override
  public TemplateBlock setHardness(float hardness) {
    return (TemplateBlock) super.setHardness(hardness);
  }

  @Override
  public TemplateBlock setBlockUnbreakable() {
    return (TemplateBlock) super.setBlockUnbreakable();
  }

  @Override
  public TemplateBlock setTickRandomly(boolean shouldTick) {
    return (TemplateBlock) super.setTickRandomly(shouldTick);
  }

  @Override
  public TemplateBlock setTranslationKey(String key) {
    return (TemplateBlock) super.setTranslationKey(key);
  }

  @Override
  public TemplateBlock setCreativeTab(CreativeTabs tab) {
    return (TemplateBlock) super.setCreativeTab(tab);
  }

  public TemplateBlock setDefault(IBlockState state) {
    setDefaultState(state);
    return this;
  }

  @SuppressWarnings("deprecation")
  public TemplateBlock setSlipperiness(float s) {
    this.setDefaultSlipperiness(s);
    return this;
  }

  @SuppressWarnings("DeprecatedIsStillUsed")
  @Override
  @Deprecated
  public void setDefaultSlipperiness(float slipperiness) {
    super.setDefaultSlipperiness(slipperiness);
  }

  @SuppressWarnings("deprecation")
  public TemplateBlock setHarvestTool(String toolClass, int level) {
    setHarvestLevel(toolClass, level);
    return this;
  }

  @SuppressWarnings("deprecation")
  public TemplateBlock setHarvestTool(String toolClass, int level, IBlockState state) {
    setHarvestLevel(toolClass, level, state);
    return this;
  }

  @Override
  @Deprecated
  public void setHarvestLevel(String toolClass, int level) {
    super.setHarvestLevel(toolClass, level);
  }

  @Override
  @Deprecated
  public void setHarvestLevel(String toolClass, int level, IBlockState state) {
    super.setHarvestLevel(toolClass, level, state);
  }
}



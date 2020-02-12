package com.aranaira.arcanearchives.blocks.templates;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public abstract class TemplateBlock extends Block {
  private ItemBlock itemBlock = null;

  public TemplateBlock(Material materialIn) {
    super(materialIn);
  }

  public ItemBlock getItemBlock() {
    return itemBlock;
  }

  public void setItemBlock(ItemBlock itemBlock) {
    this.itemBlock = itemBlock;
  }
}



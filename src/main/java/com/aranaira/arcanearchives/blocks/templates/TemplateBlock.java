package com.aranaira.arcanearchives.blocks.templates;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.tileentities.AATileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Objects;

public abstract class TemplateBlock extends Block {
	private ItemBlock itemBlock = null;

	public TemplateBlock (Material materialIn) {
		super(materialIn);
	}

	public ItemBlock getItemBlock () {
		return itemBlock;
	}

	public void setItemBlock (ItemBlock itemBlock) {
		this.itemBlock = itemBlock;
	}

	public ITextComponent getNameComponent () {
		return new TextComponentTranslation(String.format("%s.name", getTranslationKey()));
	}
}



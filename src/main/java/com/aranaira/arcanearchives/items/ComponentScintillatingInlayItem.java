package com.aranaira.arcanearchives.items;

import com.aranaira.arcanearchives.items.templates.ItemTemplate;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ComponentScintillatingInlayItem extends ItemTemplate {
	public static final String NAME = "item_component_scintillatinginlay";

	public ComponentScintillatingInlayItem () {
		super(NAME);
	}

	@Override
	public void addInformation (ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.component.scintillatinginlay"));
	}

	@Override
	@SuppressWarnings("deprecation")
	public EnumRarity getRarity (ItemStack stack) {
		return EnumRarity.EPIC;
	}
}
